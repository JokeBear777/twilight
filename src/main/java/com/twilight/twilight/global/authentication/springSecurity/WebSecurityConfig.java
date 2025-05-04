package com.twilight.twilight.global.authentication.springSecurity;

import com.twilight.twilight.global.authentication.springSecurity.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    //시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                //.requestMatchers(toH2Console())
                .requestMatchers( new AntPathRequestMatcher( "/static/**" ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup")
                        ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/home"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (개발 시)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService); // 사용자 정보 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호 인코더 설정
        return new ProviderManager(authProvider); // 인증 매니저로 반환
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
