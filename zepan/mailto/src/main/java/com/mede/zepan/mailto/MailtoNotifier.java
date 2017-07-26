package com.mede.zepan.mailto;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.mede.zepan.Notifier;
import com.mede.zepan.NotifierFactory;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.exceptions.NotifyException;

/**
 * Created by JWang on 6/26/2017.
 */
public class MailtoNotifier implements Notifier {


    protected NotificationConfig config;

    public MailtoNotifier(NotificationConfig config) {
        this.config = config;
    }

    @Override
    public void notify(String type, String processInfo) throws NotifyException {
        String to = config.getPath();
        String from = config.getMailFrom();
        String host = config.getHost();
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Notification From Zepan");
            message.setText("Zepan ProcessInfo " + type + ":\n" + processInfo);
            Transport.send(message);
        }
        catch (MessagingException ex) {
            log.warn("Messaging Exception", ex);
        }
    }


    public static class MailtoNotifierFactory implements NotifierFactory {

        @Override
        public Notifier newNotifier(NotificationConfig config) {
            return new MailtoNotifier(config);
        }
    }
}
