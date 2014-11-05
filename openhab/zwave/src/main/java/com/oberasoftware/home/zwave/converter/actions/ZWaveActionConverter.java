package com.oberasoftware.home.zwave.converter.actions;

import com.oberasoftware.home.api.MessageConverter;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.converter.ZWaveConverter;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author renarj
 */
public class ZWaveActionConverter implements MessageConverter<ZWaveAction, ZWaveRawMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ZWaveActionConverter.class);

    private Map<String, ZWaveConverter> converterMap = new HashMap<>();

    public ZWaveActionConverter() {
        ServiceLoader<ZWaveConverter> l = ServiceLoader.load(ZWaveConverter.class);
        l.forEach(m -> addSupportedTypes(m, m.getSupportedTypeNames()));
    }

    private void addSupportedTypes(ZWaveConverter c, Set<String> supportedTypes) {
        supportedTypes.forEach(s -> addSupportedType(c, s));
    }

    private void addSupportedType(ZWaveConverter c, String typeName) {
        LOG.debug("Added message converter: {} for type: {}", c, typeName);
        converterMap.put(typeName, c);
    }

    @Override
    public ZWaveRawMessage convert(ZWaveAction source) throws HomeAutomationException {
        ZWaveConverter converter = converterMap.get(source.getClass().getSimpleName());
        LOG.debug("Converting action of type: {} converter available for this type: {}", source.getClass().getSimpleName(), converter);

        Optional<ZWaveRawMessage> convertedMessage = convert(Optional.of(converter), source);
        if(convertedMessage.isPresent()) {
            LOG.debug("Action converted to ZWave message: {}", convertedMessage.get());
            return convertedMessage.get();
        } else {
            LOG.debug("There is no specific converter for action: {} finding a generic converter", source);
            //TODO: implement basic converter
            return null;
        }
    }

    private Optional<ZWaveRawMessage> convert(Optional<ZWaveConverter> actionConverter, ZWaveAction source) throws HomeAutomationException {
        if(actionConverter.isPresent()) {
            ZWaveConverter<ZWaveAction, ZWaveRawMessage> c = actionConverter.get();
            LOG.debug("Converting action: {} using converter: {}", source, c);
            return Optional.of(c.convert(source));
        }
        return Optional.empty();
    }
}
