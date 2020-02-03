package pl.coderslab.rentier.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class adminURLFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(adminURLFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = ((HttpServletRequest) servletRequest).getSession();

        LOGGER.info("loggedId: " + session.getAttribute("loggedId") + " admin: " + session.getAttribute("loggedAdmin") + " user: " + session.getAttribute("loggedUser"));
        if (session.getAttribute("loggedAdmin") == null) {

            response.sendRedirect("/login");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
