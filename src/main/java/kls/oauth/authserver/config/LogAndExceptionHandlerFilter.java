package kls.oauth.authserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogAndExceptionHandlerFilter extends OncePerRequestFilter {
    private Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            LOGGER.info(httpServletRequestToString(request));
            LOGGER.info(request.getSession().getId());
            filterChain.doFilter(request, response);
            LOGGER.info(String.valueOf(response.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            response.setStatus(401);
            Map <String, Object> body = new LinkedHashMap <>();
            body.put("error", "access_denied");
            ObjectMapper om = new ObjectMapper();
            om.writeValueAsString(body);
            response.getOutputStream().println();
        }
    }

    private String httpServletRequestToString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Request Method = [").append(request.getMethod()).append("], ");
        sb.append("Request URL Path = [").append(request.getRequestURL()).append("], ");
        String headers =
                Collections.list(request.getHeaderNames()).stream()
                        .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)) )
                        .collect(Collectors.joining(", "));

        if (headers.isEmpty()) {
            sb.append("Request headers: NONE,");
        } else {
            sb.append("Request headers: [").append(headers).append("],");
        }
//
//        String parameters =
//                Collections.list(request.getParameterNames()).stream()
//                        .map(p -> p + " : " + Arrays.asList( request.getParameterValues(p)) )
//                        .collect(Collectors.joining(", "));
//
//        if (parameters.isEmpty()) {
//            sb.append("Request parameters: NONE.");
//        } else {
//            sb.append("Request parameters: [").append(parameters).append("].");
//        }

        return sb.toString();
    }
}