package utilities;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailSender {
    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);

    private static Properties getProperty() {
        LOG.info("get properties of mail starting");
        Properties prop = new Properties();
        String filename = "mail.properties";
        InputStream input = null;
        try {
            input = FileUploadDocuments.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                LOG.info("Sorry, unable to find " + filename);
                return null;
            }
            prop.load(input);
        } catch (IOException ex) {
            LOG.error("properties {} was not loaded", filename);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        return prop;
    }

    public static void sendMail(String emailAddress, String message, String subject) {
        LOG.info("send email starting to contact {}", emailAddress);
        Email email = new SimpleEmail();
        Properties properties = getProperty();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(properties.getProperty("mail.login"),
                properties.getProperty("mail.password")));
        email.setDebug(false);
        email.setHostName(properties.getProperty("mail.host.name"));
        email.setSSLOnConnect(true);
        email.setStartTLSEnabled(true);
        try {
            email.setFrom(properties.getProperty("mail.sender"));
            email.setSubject(subject);
            email.addTo(emailAddress);
            email.setMsg(message);
            email.send();
        } catch (EmailException e) {
            LOG.error("email was not sent to {}", emailAddress);
        }
    }

    static void sendMailToAdmin(String message) {
        LOG.info("send email to admin starting");
        Email email = new SimpleEmail();
        Properties properties = getProperty();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(properties.getProperty("mail.login"),
                properties.getProperty("mail.password")));
        email.setDebug(false);
        email.setHostName(properties.getProperty("mail.host.name"));
        email.setSSLOnConnect(true);
        email.setStartTLSEnabled(true);
        try {
            email.setFrom(properties.getProperty("mail.sender"));
            email.setSubject("People with birthday");
            email.addTo(properties.getProperty("mail.admin.address"));
            email.setMsg(message);
            email.send();
        } catch (EmailException e) {
            LOG.error("email was not sent to administrator");
        }
    }

}
