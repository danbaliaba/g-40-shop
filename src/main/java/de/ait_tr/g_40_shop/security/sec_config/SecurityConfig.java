package de.ait_tr.g_40_shop.security.sec_config;

import de.ait_tr.g_40_shop.security.sec_filter.TokenFilter;
import org.hibernate.mapping.Any;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/products/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/system/products").hasRole("SUPPLIER")
                        .requestMatchers(HttpMethod.PUT, "/products").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/restore").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/products/quantity").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/products/total-price").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/customers").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/customers/restore").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/customers/number").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/customers/cart-cost").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/customers/avg-product-cost").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/customers/add-product").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/customers/delete-product").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/customers/clear-cart").hasAnyRole("ADMIN", "USER")

                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}


/*
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZW4iLCJleHAiOjE3MTg1Nzg4OTMsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifSx7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sIm5hbWUiOiJCZW4ifQ.TSrHEL0A9OAPaNhpoXlkfycJInK-JF3VNrC9UlXns90",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZW4iLCJleHAiOjE3MjA1NjYwOTN9.AFmA01YhNC8Hq2L3yssNid_9ODcqh_Vnyd9jABUm_l0"
* */
