package com.gmail.yeatz0408.backToshokan;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class BackToshokanApplication {

	public static void main(String[] args) {

		SpringApplication.run(BackToshokanApplication.class, args);
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("https://yeatz0408.github.io/", "http://yeatz0408.github.io/"));
//                    cors.setAllowedOrigins(List.of(""));
                    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("Origin", "Access-Control", "Allow-Origin", "Content-Type", "Accept", "Authorization", "Origin", "Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Header"));
                    return cors;
                }).and().csrf().disable()
                .authorizeRequests()
                .antMatchers( "/**").permitAll()
                .anyRequest().permitAll();
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("https://yeatz0408.github.io/", "http://yeatz0408.github.io/"));
//        corsConfiguration.setAllowedOrigins(Arrays.asList(""));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control", "Allow-Origin", "Content-Type", "Accept", "Authorization", "Origin", "Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Header")); // this allows all headers
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Request-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
