package ru.itis.servlets;

import ru.itis.exceptions.DatabaseException;
import ru.itis.models.Studio;
import ru.itis.repositories.genres.GenresRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/addGenre")
public class AddGenreServlet extends HttpServlet {

    private GenresRepository genresRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<String> genresList = genresRepository.getAllGenres();
            req.setAttribute("genresList", genresList);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось найти список жанров");
            doGet(req, resp);
            return;
        }

        req.getRequestDispatcher("/jsp/admin/addGenre.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();

        String name = req.getParameter("genreName");

        if (name == null || name.trim().isEmpty()){
            req.setAttribute("error", "Название обязательно");
            doGet(req, resp);
            return;
        }

        try {
            if (genresRepository.getGenreId(name) != null){
                req.setAttribute("error", "Жанр с таким названием уже существует");
                doGet(req, resp);
                return;
            }

            genresRepository.addGenre(name);

            List<String> genres = genresRepository.getAllGenres();
            session.setAttribute("genres", genres);

        } catch (DatabaseException e){
            e.printStackTrace();
            req.setAttribute("error", "Не удалось сохранить жанр");
            doGet(req, resp);
            return;
        }

        resp.sendRedirect("/adminPage?success=added&type=genre");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        genresRepository = (GenresRepository) config.getServletContext().getAttribute("genresRepository");
    }
}
