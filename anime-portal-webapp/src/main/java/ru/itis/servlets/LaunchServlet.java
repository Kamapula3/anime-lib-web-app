package ru.itis.servlets;

import ru.itis.dto.AnimeDto;
import ru.itis.dto.AnimeFilter;
import ru.itis.exceptions.DatabaseException;
import ru.itis.models.FileInfo;
import ru.itis.models.Studio;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.files.FileRepository;
import ru.itis.repositories.genres.GenresRepository;
import ru.itis.repositories.studio.StudioRepository;
import ru.itis.services.AnimeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("")
public class LaunchServlet extends HttpServlet {

    private AnimeService animeService;
    private StudioRepository studioRepository;
    private GenresRepository genresRepository;


    @Override
    public void init(ServletConfig config) throws ServletException {
        animeService = (AnimeService) config.getServletContext().getAttribute("animeService");
        studioRepository = (StudioRepository) config.getServletContext().getAttribute("studioRepository");
        genresRepository = (GenresRepository) config.getServletContext().getAttribute("genresRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            HttpSession session = req.getSession();
            session.setAttribute("authenticated", false);


            if(session.getAttribute("genres") == null){
                List<String> genres = genresRepository.getAllGenres();
                List<Studio> studios = studioRepository.findAll();
                session.setAttribute("genres", genres);
                session.setAttribute("studios", studios);
            }

            List<AnimeDto> animeFullList = animeService.getAllAnimeList();
            int minYear = animeFullList.stream().mapToInt(AnimeDto::getReleaseYear).min().orElse(1980);
            int maxYear = animeFullList.stream().mapToInt(AnimeDto::getReleaseYear).max().orElse(2025);

            session.setAttribute("minYear", minYear);
            session.setAttribute("maxYear", maxYear);

        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось загрузить список аниме. Попробуйте обновить страницу позже.");
            req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        }

        req.getRequestDispatcher("/jsp/startPage.jsp").forward(req, resp);
    }
}
