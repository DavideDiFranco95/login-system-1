package co.develhope.loginSystem1.auth.entities;

import co.develhope.loginSystem1.users.entities.User;
import lombok.Data;

@Data
public class LoginRTO {
    User user;
    private String JWT;
}
