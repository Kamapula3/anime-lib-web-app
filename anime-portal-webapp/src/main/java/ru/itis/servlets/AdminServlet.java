package ru.itis.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminPage")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String success = req.getParameter("success");
        String type = req.getParameter("type");

        if (success != null && type != null && "added".equals(success)){

            String added = "";

            switch (type.trim()) {
                case "studio":
                    added += "Студия ";
                    added += " успешно добавлена";
                    break;
                case "genre":
                    added += "Жанр ";
                    added += " успешно добавлен";
                    break;
                case "character":
                    added += "Персонаж ";
                    added += " успешно добавлен";
                    break;
                case "user":
                    added += "Пользователь ";
                    added += " успешно добавлен";
                    break;
            }

            req.setAttribute("added", added);
        }

        req.getRequestDispatcher("/jsp/admin/adminPage.jsp").forward(req, resp);
    }
}
