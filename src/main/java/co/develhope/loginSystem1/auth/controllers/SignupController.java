package co.develhope.loginSystem1.auth.controllers;

import co.develhope.loginSystem1.auth.entities.SignupActivationDTO;
import co.develhope.loginSystem1.auth.entities.SignupDTO;
import co.develhope.loginSystem1.auth.services.SignupService;
import co.develhope.loginSystem1.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SignupController {
    @Autowired
    private SignupService signupService;
    @PostMapping("/signup")
    public User signup(@RequestBody SignupDTO signupDTO) throws Exception {

        return signupService.signup1(signupDTO);
    }
    @PostMapping("/signup/{role}")
    public User signup(@RequestBody SignupDTO signupDTO, @PathVariable String role) throws Exception {

        return signupService.signup1(signupDTO,role);
    }

    @PostMapping("/signup/activation")
    public User signup(@RequestBody SignupActivationDTO signupActivationDTO) throws Exception {
        return signupService.activate(signupActivationDTO);
    }

}
