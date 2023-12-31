package com.novianto.challange6.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true)
public class Oauth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    /**
     * Manage resource server.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }
// private static final String SECURED_PATTERN = "/api/**";

    /**
     * Manage endpoints.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/showFile/**", "/v1/showFile/**", "/v1/upload",
                        "/v1/user-register/**", "/v1/register/**" ,"/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/user-login/**", "/v1/forget-password/**", "/oauth/authorize**", "/login**", "/error**")
                .permitAll()
                .antMatchers("/v1/user/get/**", "/v1/user/all-users").hasAnyAuthority("ROLE_READ")
                .antMatchers("/v1/role-test-global/post-barang").hasAnyAuthority("ROLE_WRITE")
                .antMatchers("/v1/user/update/**", "/v1/user/delete/**", "/v1/user/get/**", "/v1/user/all-users").hasAnyAuthority("ROLE_CUSTOMER")
                .antMatchers("/v1/role-test-global/post-barang-admin").hasAnyAuthority("ROLE_MERCHANT")
                .and().authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }
}
