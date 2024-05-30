package kr.co.ssalon.filer;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class LoginDefaultFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LoginDefaultFilter");
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/login$")) {
            chain.doFilter(request, response);
            return ;
        }
        sendResponse(response, "No AccessToken Oauth2 Login please",HttpServletResponse.SC_UNAUTHORIZED);
    }
    private void sendResponse(HttpServletResponse response, String message,int status) {
        log.info(message);
        response.setStatus(status);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(message);
            writer.flush();
        } catch (IOException e) {
            log.error("Error writing to response", e);
        }
    }
}
