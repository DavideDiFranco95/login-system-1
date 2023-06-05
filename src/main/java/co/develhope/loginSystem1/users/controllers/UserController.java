package co.develhope.loginSystem1.users.controllers;

import co.develhope.loginSystem1.auth.entities.LoginRTO;
import co.develhope.loginSystem1.auth.services.LoginService;
import co.develhope.loginSystem1.users.entities.User;
import co.develhope.loginSystem1.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
    @Autowired
    private LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    public LoginRTO getProfile(Principal principal){
        User user =(User) ((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        LoginRTO rto = new LoginRTO();
        rto.setUser(user);
        rto.setJWT(loginService.generateJWT(user));
        return rto;
    }
}
