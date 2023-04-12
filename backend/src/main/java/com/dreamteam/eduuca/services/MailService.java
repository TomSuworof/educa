package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.config.MailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailService {
    private final MailConfig mailConfig;

    public void sendPasswordChangeCode(String emailAddress, String code) throws EmailException {
        // TODO: decide to hide them or not
        log.debug("sendPasswordChangeCode() called. Email: {}, code: {}", () -> emailAddress, () -> code);
        HtmlEmail email = getEmailTemplate(emailAddress);

        email.setSubject("Password reset");
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>You sent request for password reset.</h1\n" +
                "<p>" +
                "Here is your code: " + code +
                "\n" +
                "</p>\n" +
                "<p>If you did not send the request, ignore this message or contact us via reply on the message.</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
        log.trace("sendPasswordChangeCode(). Email sent");
    }

    public void sendRoleChanged(String emailAddress, String role) throws EmailException {
        // TODO: decide to hide or not
        log.debug("sendRoleChanged() called. Email: {}, role: {}", () -> emailAddress, () -> role);
        HtmlEmail email = getEmailTemplate(emailAddress);

        email.setSubject("Your role was changed");
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Your role was changed.</h1\n" +
                "<p>Heads of universe decided that you should be " + role + ".</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
        log.trace("sendRoleChanged(). Email sent");
    }

    private HtmlEmail getEmailTemplate(String emailAddressReceiver) throws EmailException {
        log.debug("getEmailTemplate() called. Email address receiver: {}", () -> emailAddressReceiver);
        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailConfig.getHost());
        email.setSmtpPort(mailConfig.getPort());
        email.setAuthenticator(new DefaultAuthenticator(
                mailConfig.getUsername(),
                mailConfig.getPassword())
        );
        email.setSSLOnConnect(true);
        email.setFrom(mailConfig.getUsername(), "Eduuca Inc");
        email.setCharset("utf-8");
        email.addTo(emailAddressReceiver);
        log.trace("getEmailTemplate(). Email: {}", () -> email);
        return email;
    }
}
