package co.develhope.loginSystem1.notification.services;

import co.develhope.loginSystem1.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("development@develhope.co");
        sms.setReplyTo("development@develhope.co");
        sms.setSubject("Ti sei iscritto alla piattaforma");
        sms.setText("Il codice di attivazione è: " + user.getActivationCode());
        // sms.setText("Clicca qui per attivare https://www.miosito.it/signup/activation/" + user.getActivationCode());
        mailSender.send(sms);
    }

    public void sendPasswordResetMail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("development@develhope.co");
        sms.setReplyTo("development@develhope.co");
        sms.setSubject("Ti sei iscritto alla piattaforma");
        sms.setText("Il codice di Reset è: " + user.getPasswordResetCode());
        // sms.setText("Clicca qui per attivare https://www.miosito.it/signup/activation/" + user.getActivationCode());
        mailSender.send(sms);
    }
}
