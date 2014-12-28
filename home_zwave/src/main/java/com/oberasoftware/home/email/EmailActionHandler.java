package com.oberasoftware.home.email;

import com.oberasoftware.home.zwave.api.events.EventListener;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class EmailActionHandler implements EventListener<EmailAction> {
    private static final Logger LOG = getLogger(EmailActionHandler.class);

    @Override
    public void receive(EmailAction event) throws Exception {
        LOG.debug("Going to send out an email to: {} with message: {}", event.getTo(), event.getMessage());

        String from = "rdevries@sdl.com";
        String host = "smtp.tridion.global.sdl.corp";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(event.getTo()));

            message.setSubject("Alert from HomeAutomation");
            message.setText(event.getMessage());

            Transport.send(message);
            LOG.debug("Sent a succesfull email to: {}", event.getTo());
        }catch (MessagingException e) {
            LOG.error("", e);
        }
    }
}
