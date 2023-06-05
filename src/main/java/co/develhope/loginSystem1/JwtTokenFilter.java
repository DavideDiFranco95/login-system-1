package co.develhope.loginSystem1;

import co.develhope.loginSystem1.auth.services.LoginService;
import co.develhope.loginSystem1.users.entities.User;
import co.develhope.loginSystem1.users.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Collection<? extends GrantedAuthority> getAuthorities(User user){
        if (user==null||!user.isActive()) return List.of();
        //Per Java 8 in su
        return Arrays.asList(user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
                .toArray(SimpleGrantedAuthority[]::new));
        // Per Java 17 in su
        //return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName())).toList();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        //Se da 403(forbidden) cambiare un bearer token su POSTMAN
        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //Get jwt token and validate
        final String token; // from header Bearer 1234567
        try {
            token = header.split(" ")[1].trim();
        }catch (JWTVerificationException e){
            filterChain.doFilter(request,response);
            return;
        }

        DecodedJWT decoded;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(LoginService.JWT_SECRET))
                    .withIssuer("develhope-demo").build();
            decoded = verifier.verify(token);
        }catch (JWTVerificationException e){
            filterChain.doFilter(request,response);
            return;
        }


        if (decoded==null){
            filterChain.doFilter(request,response);
            return;
        }

        //Get user identity and set it on the spring security context
        Optional<User> userDetails = userRepository.findById(decoded.getClaim("id").asLong());
        if (!userDetails.isPresent() || !userDetails.get().isActive()){
            filterChain.doFilter(request,response);
            return;
        }

        User user = userDetails.get();
        user.setPassword(null);
        user.setActivationCode(null);
        user.setPasswordResetCode(null);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                user,null, getAuthorities(user)
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }


}
