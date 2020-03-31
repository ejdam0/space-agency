package pl.strzelecki.spaceagency.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REALM = "SPACE_AGENCY";

    private DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled " +
                        "from person where username=?")
                .authoritiesByUsernameQuery("select username, authority " +
                        "from person where username=?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").hasRole("CM")
                .antMatchers("/").hasRole("CUS")
                .antMatchers("/missions/**").hasRole("CM")
                .antMatchers("/orders/new").hasAnyRole("CM", "CUS")
                .antMatchers("/orders/history").hasRole("CM")
                .antMatchers("/orders/top-products").hasRole("CM")
                .antMatchers("/orders/top-missions").hasRole("CM")
                .antMatchers("/products/add").hasRole("CM")
                .antMatchers("/products/remove/**").hasRole("CM")
                .antMatchers("/products/search-by-name").hasAnyRole("CM", "CUS")
                .antMatchers("/products/search-by-type").hasAnyRole("CM", "CUS")
                .antMatchers("/products/search-by-date").hasAnyRole("CM", "CUS")
                .antMatchers("/db-console/**").permitAll()
                .and()
                .httpBasic().realmName(REALM)
                .authenticationEntryPoint(getBasicAuthEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint();
    }

    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}

