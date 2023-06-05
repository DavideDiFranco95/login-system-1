package co.develhope.loginSystem1.auth.entities;

import lombok.Data;

@Data
public class LoginDTO {
    /** this is the user email */
    private String email;
    /** This is the password CLEAR */
    private String password;
}
