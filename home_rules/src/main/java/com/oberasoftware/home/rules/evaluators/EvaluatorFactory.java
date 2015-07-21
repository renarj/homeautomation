package com.oberasoftware.home.rules.evaluators;

import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class EvaluatorFactory {
    private static final Logger LOG = getLogger(EvaluatorFactory.class);

    @Autowired(required = false)
    private List<Evaluator<?, ?>> evaluators;

    private Map<String, Evaluator<?, ?>> evaluatorMap = new HashMap<>();

    @PostConstruct
    public void processEvaluators() {
        evaluators.forEach(this::processEvaluator);
    }

    private void processEvaluator(Evaluator<?, ?> evaluator) {
        Class<?> evaluatorClass = evaluator.getClass();
        stream(evaluatorClass.getMethods())
                .filter(m -> m.getName().equals("eval"))
                .filter(m -> !m.isBridge())
                .forEach(m -> addTypeEvaluator(evaluator, m));

    }

    private void addTypeEvaluator(Evaluator<?, ?> evaluator, Method method) {
        LOG.debug("Loading parameter type on method: {}", method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterTypes.length == 1) {
            LOG.debug("Interested in message type: {}", parameterTypes[0].getName());
            Class<?> parameterType = parameterTypes[0];

            evaluatorMap.put(parameterType.getName(), evaluator);
        }
    }

//    public ConditionEvaluator<Condition> getEvaluator(Condition condition) {
//        LOG.debug("Finding condition evaluator for action: {}", condition);
//
//        return (ConditionEvaluator<Condition>) getEvaluator(condition.getClass());
//    }
//
//    public ActionEvaluator<Action> getEvaluator(Action action) {
//        LOG.debug("Finding action evaluator for action: {}", action);
//
//        return (ActionEvaluator<Action>) getEvaluator(action.getClass());
//    }
//
//    public ValueEvaluator<ResolvableValue> getEvaluator(ResolvableValue value) {
//        LOG.debug("Finding value evaluator for value: {}", value);
//
//        return (ValueEvaluator < ResolvableValue >) getEvaluator(value.getClass());
//    }

    public <T extends Evaluator<E, ?>, E> T getEvaluator(E input) {
        LOG.debug("Finding evaluator for type: {}", input.getClass());

        return (T) getEvaluator(input.getClass());
    }

    private Evaluator getEvaluator(Class<?> evalType) {
        for(TypeToken t : TypeToken.of(evalType).getTypes()) {
            String typeName = t.getRawType().getName();
            LOG.debug("Checking an evaluator for type: {}", typeName);

            Optional<Evaluator> evaluator = ofNullable(evaluatorMap.get(typeName));
            if (evaluator.isPresent()) {
                LOG.debug("Found Evaluator: {} for type: {}", evaluator.get(), evalType);

                return evaluator.get();
            }
        }

        throw new EvalException("Could not find evaluator for type: " + evalType);
    }

}
