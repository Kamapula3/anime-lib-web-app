package ru.itis.servlets;

import ru.itis.exceptions.DatabaseException;
import ru.itis.models.Anime;
import ru.itis.models.Studio;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.genres.GenresRepository;
import ru.itis.repositories.studio.StudioRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/addStudio")
public class AddStudioServlet extends HttpServlet {

    private StudioRepository studioRepository;
    private GenresRepository genresRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<Studio> studios = studioRepository.findAll();
            req.setAttribute("studios", studios);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось найти список студий");
            doGet(req, resp);
            return;
        }


        req.getRequestDispatcher("/jsp/admin/addStudio.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();

        String name = req.getParameter("name");
        String country = req.getParameter("country");

        if (name == null || name.trim().isEmpty()){
            req.setAttribute("error", "Название обязательно");
            doGet(req, resp);
            return;
        }

        if (country == null || country.trim().isEmpty()){
            req.setAttribute("error", "Название обязательно");
            doGet(req, resp);
            return;
        }

        try {
            Studio studio = Studio.builder()
                    .name(name)
                    .country(country)
                    .build();
            
            if(studioRepository.getStudioId(name) != null){
                req.setAttribute("error", "Студия с таким названием уже существует");
                doGet(req, resp);
                return;
            }

            studioRepository.save(studio);

            List<Studio> studios = studioRepository.findAll();
            session.setAttribute("studios", studios);

        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось сохранить студию");
            doGet(req, resp);
            return;
        }


        resp.sendRedirect("/adminPage?success=added&type=studio");

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        genresRepository = (GenresRepository) config.getServletContext().getAttribute("genresRepository");
        studioRepository = (StudioRepository) config.getServletContext().getAttribute("studioRepository");
    }
}
