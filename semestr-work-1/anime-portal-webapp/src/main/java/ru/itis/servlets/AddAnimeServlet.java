package ru.itis.servlets;

import ru.itis.dto.AnimeDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.models.Anime;
import ru.itis.models.Studio;
import ru.itis.repositories.anime.AnimeRepository;
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

@WebServlet("/addAnime")
public class AddAnimeServlet extends HttpServlet {

    private GenresRepository genresRepository;
    private AnimeRepository animeRepository;
    private StudioRepository studioRepository;
    private AnimeService animeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("genres") == null) {
            session.setAttribute("genres", genresRepository.getAllGenres());
            session.setAttribute("studios", studioRepository.findAll());
        }

        try {
            List<AnimeDto> animeList = animeService.getAllAnimeList();
            req.setAttribute("animeList", animeList);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось найти список аниме");
            doGet(req, resp);
            return;
        }

        req.getRequestDispatcher("/jsp/admin/addAnime.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String yearParam = req.getParameter("year");
        String studioName = req.getParameter("studio");
        String[] genresArray = req.getParameterValues("genres");

        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("error", "Название обязательно");
            doGet(req, resp);
            return;
        }

        if (description == null || description.trim().isEmpty()){
            req.setAttribute("error", "Описание обязательно");
            doGet(req, resp);
            return;
        }

        if (studioName == null || studioName.trim().isEmpty()){
            req.setAttribute("error", "Выбор студии обязателен");
            doGet(req, resp);
            return;
        }

        if (genresArray == null){
            req.setAttribute("error", "Выбирете минимум один жанр");
            doGet(req, resp);
            return;
        } else if(genresArray.length == 0){
            req.setAttribute("error", "Выбирете минимум один жанр");
            doGet(req, resp);
            return;
        }


        Integer year = null;
        if (yearParam != null && !yearParam.isEmpty()) {
            try {
                year = Integer.parseInt(yearParam);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Некорректный год");
                doGet(req, resp);
                return;
            }
        }

        Integer studioId = null;
        if (studioName != null && !studioName.trim().isEmpty()) {
            studioId = studioRepository.getStudioId(studioName);
        }

        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        try {
            Anime anime = Anime.builder()
                    .title(title.trim())
                    .description(description != null ? description.trim() : null)
                    .releaseYear(year)
                    .studioId(studioId)
                    .userId(userId)
                    .build();


            if (animeRepository.findByTitle(title.trim()) != null){
                req.setAttribute("error", "Аниме с таким названием уже существует");
                doGet(req, resp);
                return;
            }

            animeRepository.save(anime);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось сохранить аниме");
            doGet(req, resp);
            return;
        }


        Anime savedAnime = animeRepository.findByTitle(title.trim());
        Integer animeId = savedAnime.getId();

        if (genresArray != null) {
            for (String genreName : genresArray) {
                Integer genreId = genresRepository.getGenreId(genreName.trim());
                genresRepository.addGenresToAnime(genreId, animeId);
            }
        }

        resp.sendRedirect("/uploadAnimeCover?success=added");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        genresRepository = (GenresRepository) config.getServletContext().getAttribute("genresRepository");
        animeRepository = (AnimeRepository) config.getServletContext().getAttribute("animeRepository");
        studioRepository = (StudioRepository) config.getServletContext().getAttribute("studioRepository");
        animeService = (AnimeService) config.getServletContext().getAttribute("animeService");
    }
}
