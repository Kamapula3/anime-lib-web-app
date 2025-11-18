package ru.itis.filters;


import javax.servlet.annotation.WebFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/addAnime", "/adminPage", "/addGenre", "/uploadAnimeCover", "/addStudio", "/addUser", "/uploadAnimeCover", "/addCharacter"})
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession(false);

        boolean isAuthenticated =  session != null && Boolean.TRUE.equals(session.getAttribute("authenticated"));
        String roleName = (String) session.getAttribute("roleName");

        if (isAuthenticated && roleName.equals("ADMIN")){
            filterChain.doFilter(req, resp);
        }  else if (isAuthenticated){
            resp.sendRedirect(req.getContextPath() + "/mainPage");
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    @Override
    public void destroy() {}
}
