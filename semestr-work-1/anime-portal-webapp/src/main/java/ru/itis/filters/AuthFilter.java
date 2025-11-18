package ru.itis.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/mainPage", "/profile", "/animeInfo"})
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession(false);

        boolean isAuthenticated =  session != null && Boolean.TRUE.equals(session.getAttribute("authenticated"));

        if (isAuthenticated){
            filterChain.doFilter(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/signUp");
        }
    }

    @Override
    public void destroy() {}
}
