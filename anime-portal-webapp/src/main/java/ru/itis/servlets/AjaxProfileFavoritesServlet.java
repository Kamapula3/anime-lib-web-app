package ru.itis.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.AnimeDto;
import ru.itis.repositories.user.UserFavoritesListRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/ajax/profile/favorites")
public class AjaxProfileFavoritesServlet  extends HttpServlet {

    private UserFavoritesListRepository userFavoritesListRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        try {
            List<AnimeDto> list = userFavoritesListRepository.getFavoritesList(userId);

            String json = new ObjectMapper().writeValueAsString(list);
            resp.getWriter().write(json);

        } catch (Exception e) {
            resp.setStatus(500);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userFavoritesListRepository = (UserFavoritesListRepository) config.getServletContext().getAttribute("userFavoritesListRepository");
    }
}
