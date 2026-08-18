package kg.attractor.jobsearch.config;

import kg.attractor.jobsearch.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/errors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registration").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vacancies/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/resumes").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.PUT, "/resumes/**").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.DELETE, "/resumes/**").hasRole("APPLICANT")

                        .requestMatchers(HttpMethod.GET, "/resumes/category/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/resumes/applicant/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/resumes/*").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/resumes").hasRole("EMPLOYER")

                        .requestMatchers(HttpMethod.POST, "/vacancies").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.PUT, "/vacancies/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.DELETE, "/vacancies/**").hasRole("EMPLOYER")
                        .requestMatchers("/pages/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pages/vacancies").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pages/companies").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.GET, "/pages/resumes").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/pages/resumes/create").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.POST, "/pages/resumes/create").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.GET, "/pages/resumes/*/edit").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.POST, "/pages/resumes/*/edit").hasRole("APPLICANT")
                        .requestMatchers(HttpMethod.GET, "/pages/vacancies/create").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/pages/vacancies/create").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/pages/vacancies/*/edit").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/pages/vacancies/*/edit").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/pages/vacancies/*/respond").hasRole("APPLICANT")

                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(handling -> handling.accessDeniedHandler(accessDeniedHandler()))
                .formLogin(form -> form
                        .loginPage("/pages/auth/login")
                        .loginProcessingUrl("/pages/auth/login")
                        .defaultSuccessUrl("/pages/cabinet", true)
                        .failureUrl("/pages/auth/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/pages/vacancies")
                        .permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                request.getRequestDispatcher("/errors/403").forward(request, response);
    }
}
