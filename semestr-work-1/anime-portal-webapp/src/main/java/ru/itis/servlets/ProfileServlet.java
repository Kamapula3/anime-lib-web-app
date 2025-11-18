package ru.itis.servlets;

import ru.itis.dto.AnimeDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityAlreadyExistsException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.user.UserFavoritesListRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/signIn");
            return;
        }

        session.setAttribute("inProfile", true);

        req.getRequestDispatcher("/jsp/user/profile.jsp").forward(req, resp);
    }
}
