package pl.coderslab.cryptomanagement;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.coderslab.cryptomanagement.service.UserService;

@Configuration
@AllArgsConstructor
public class ApplicationSecurityConfig {
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/fonts/**", "/img/**", "/js/**", "/css/**", "/scss/**", "/vendor/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .permitAll())
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}
