package co.develhope.loginSystem1;

import co.develhope.loginSystem1.users.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    /**
     * extends WebSecurityConfigurerAdapter è DEPRECATED.
     */
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()).httpBasic(withDefaults());
        //Add JWT token filter
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    protected void configure(HttpSecurity http)throws Exception{
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ROLE_"+ Roles.ADMIN,"ROLE_"+Roles.OWNER,"ROLE_"+Roles.SUPER_ADMIN)
                .antMatchers("/app/**").hasAnyRole("ROLE_"+ Roles.REGISTERED)
                .antMatchers("/admin/global/all-data-eraser").hasAnyRole("ROLE_SUPER_ADMIN")
                .antMatchers("/dev-tools/**").hasAnyAuthority("DEV_READ","DEV_DELETE")
                .antMatchers("/dev-tools-bis/**").hasAuthority("DO_DEV_TOOLS_READ")
                .anyRequest().authenticated();

        http.csrf().disable();
        http.headers().frameOptions().disable();



    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

}
