package com.oberasoftware.home.mqtt;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.storage.HomeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static nl.renarj.core.utilities.StringUtils.stringNotEmpty;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTExtension {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTExtension.class);

    private List<MQTTBroker> activeBrokers = new ArrayList<>();

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private HomeDAO homeDAO;

    @Autowired
    private AutomationBus automationBus;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void startUp() {
        LOG.info("Starting MQTT Connector");

        String brokers = environment.getProperty("mqtt.brokers");
        if(stringNotEmpty(brokers)) {
            asList(brokers.split(",")).forEach(this::loadBroker);
        } else {
            LOG.info("No MQTT Brokers configured, skipping initialization");
        }
    }

    @PreDestroy
    public void shutDownBrokers() {
        LOG.info("Shutting down MQTT connectors");
        activeBrokers.forEach(MQTTBroker::disconnect);
    }

    private void loadBroker(String brokerId) {
        String url = environment.getProperty("mqtt." + brokerId + ".url");
        String channels = environment.getProperty("mqtt." + brokerId + ".channels");
        LOG.info("Broker: {} on url: {} channels: {}", brokerId, url, channels);
        if(stringNotEmpty(url) && stringNotEmpty(channels)) {
            MQTTBroker broker = new MQTTBroker(url);
            activeBrokers.add(broker);
            try {
                broker.connect();

                asList(channels.split(",")).forEach(c -> loadChannels(broker, brokerId, c));
            } catch (HomeAutomationException e) {
                throw new RuntimeHomeAutomationException("Unable to connect", e);
            }
        } else {
            LOG.warn("Broker: {} has no url: {} or channels: {} configured", brokerId, url, channels);
        }
    }

    private void loadChannels(MQTTBroker broker, String brokerId, String channelId) {
        String baseId = brokerId + "." + channelId + ".";
        String topic = environment.getProperty(baseId + "subscribeTopic");
        String formatter = environment.getProperty(baseId + "formatter");
        String pattern = environment.getProperty(baseId + "pattern");

        LOG.info("Starting listening to channel: {} topic: {} formatter: {} pattern: {}", baseId, topic, formatter, pattern);

        MQTTFormatter messageFormatter = (MQTTFormatter) context.getBean(formatter);
        messageFormatter.configure(pattern);

        broker.subscribeTopic(topic, (topic1, payload) -> {
            MQTTMessage mqttMessage = messageFormatter.format(topic1, payload);
            LOG.debug("Received MQTT message: {}", mqttMessage);

            if (mqttMessage.getEvent() != null) {
                LOG.debug("We have an event to send to the bus: {}", mqttMessage.getEvent());
                ensureItems(mqttMessage);
                automationBus.publish(mqttMessage.getEvent());
            }
        });
    }

    private synchronized void ensureItems(MQTTMessage message) {
        String controllerId = message.getControllerId();

        try {

            Optional<ControllerItem> controllerItem = itemManager.findControllers()
                    .stream().filter(c -> c.getControllerId().equals(controllerId)).findAny();
            if(!controllerItem.isPresent()) {
                LOG.debug("Controller not existing, creating");
                itemManager.createOrUpdateController(controllerId);
                itemManager.createOrUpdatePlugin(controllerId, message.getPluginId(), "MQ TT Broker", new HashMap<>());
            }

            if(!homeDAO.findDevice(controllerId, message.getPluginId(), message.getDeviceId()).isPresent()) {
                itemManager.createOrUpdateDevice(controllerId, message.getPluginId(), message.getDeviceId(),
                        message.getDeviceId(), new HashMap<>());
            }
        } catch (HomeAutomationException e) {
            LOG.error("Could not persist", e);
        }
    }
}
