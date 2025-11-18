package ru.itis.servlets;

import ru.itis.repositories.user.UserFavoritesListRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ajax/profile/status")
public class AjaxProfileStatusServlet extends HttpServlet {

    private UserFavoritesListRepository userFavoritesListRepository;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        Integer userId = (Integer) session.getAttribute("userId");
        Integer animeId = Integer.valueOf(req.getParameter("animeId"));
        String status = req.getParameter("status");

        try {
            userFavoritesListRepository.updateStatus(animeId, userId, status);
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userFavoritesListRepository = (UserFavoritesListRepository) config.getServletContext().getAttribute("userFavoritesListRepository");
    }
}
