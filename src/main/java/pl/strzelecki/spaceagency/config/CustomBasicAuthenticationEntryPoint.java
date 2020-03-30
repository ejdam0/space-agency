package pl.strzelecki.spaceagency.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private static final Logger logger = LogManager.getLogger(CustomBasicAuthenticationEntryPoint.class);

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        //Authentication failed, send error response.
        logger.warn("Authentication failed, sending error response");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Access denied!");

        PrintWriter writer = response.getWriter();
        writer.println(HttpStatus.FORBIDDEN + " " + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("SPACE_AGENCY");
        super.afterPropertiesSet();
    }
}
