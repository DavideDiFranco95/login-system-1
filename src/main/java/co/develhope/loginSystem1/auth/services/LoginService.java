package co.develhope.loginSystem1.auth.services;

import co.develhope.loginSystem1.auth.entities.LoginDTO;
import co.develhope.loginSystem1.auth.entities.LoginRTO;
import co.develhope.loginSystem1.users.entities.User;
import co.develhope.loginSystem1.users.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LoginService {
    public static final String JWT_SECRET = "f53ed2ae-d414-4ea8-94ef-d48c750ee3a5";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public LoginRTO login(LoginDTO loginDTO){
        if (loginDTO == null) return null;
        User userFromDB = userRepository.findByEmail(loginDTO.getEmail());
        if (userFromDB == null || !userFromDB.isActive()) return null;

        boolean canLogin = this.canUserLogin(userFromDB, loginDTO.getPassword());
        if(!canLogin) return null;

        String JWT = getJWT(userFromDB);
        userFromDB.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(userFromDB);

        userFromDB.setPassword(null);
        LoginRTO out = new LoginRTO();
        out.setJWT(JWT);
        out.setUser(userFromDB);

        return out;
    }

    public boolean canUserLogin(User user, String password){
        return passwordEncoder.matches(password, user.getPassword());
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert){
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static String getJWT(User user){
        Date expiresAt = convertToDateViaInstant(LocalDateTime.now().plusDays(15));
        // mkyong = how to convert a stream to array
        //String [] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
        return JWT.create()
                .withIssuer("develhope-demo")
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                //.withClaim("roles",String.join(",",user.getRoles()
                //.stream().map(role->role.getName())).toList)
                .withClaim("id",
                        user.getId()).withClaim("email",
                        user.getEmail()).sign(Algorithm.HMAC512(JWT_SECRET));
        //Qui si può aggiungere una qualsiasi variabile, basta utilizzare il .withClaim("nomeVariabile",oggetto.getVariabile)
    }

    public String generateJWT(User user){
        String JWT = getJWT(user);

        user.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(user);

        return JWT;
    }
}
