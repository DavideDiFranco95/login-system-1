package co.develhope.loginSystem1.auth.services;

import co.develhope.loginSystem1.auth.entities.RequestPasswordDTO;
import co.develhope.loginSystem1.auth.entities.RestorePasswordDTO;
import co.develhope.loginSystem1.notification.services.MailNotificationService;
import co.develhope.loginSystem1.users.entities.User;
import co.develhope.loginSystem1.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public User request(RequestPasswordDTO requestPasswordDTO) throws Exception {
        User userFromDB = userRepository.findByEmail(requestPasswordDTO.getEmail());
        // qui si fa il ragionamento: se è nullo o inattivo, niente password
        //if (userFromDB==null || !userFromDB.isActive()) throw new Exception("User is null");
        if (userFromDB==null) throw new Exception("User is null");
        userFromDB.setPasswordResetCode(UUID.randomUUID().toString());
        mailNotificationService.sendPasswordResetMail(userFromDB);

        return userRepository.save(userFromDB);
    }

    public User restore(RestorePasswordDTO restorePasswordDTO) throws Exception {
        User userFromDB = userRepository.findByPasswordResetCode(restorePasswordDTO.getResetPasswordCode());
        if (userFromDB==null) throw new Exception("User is null");
        userFromDB.setPassword(passwordEncoder.encode(restorePasswordDTO.getNewPassword()));
        userFromDB.setPasswordResetCode(null);

        //I am activating the user
        userFromDB.setActive(true);
        userFromDB.setActivationCode(null);

        return userRepository.save(userFromDB);
    }
}
