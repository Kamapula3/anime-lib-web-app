package ru.itis.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.AnimeDto;
import ru.itis.dto.AnimeFilter;
import ru.itis.models.FileInfo;
import ru.itis.repositories.files.FileRepository;
import ru.itis.services.AnimeService;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ajax/anime")
public class AnimeAjaxServlet extends HttpServlet {

    private AnimeService animeService;
    private FileRepository fileRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) {
        animeService = (AnimeService) config.getServletContext().getAttribute("animeService");
        fileRepository = (FileRepository) config.getServletContext().getAttribute("fileRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");
        String genre = req.getParameter("genre");
        String studio = req.getParameter("studio");

        Integer year = null;
        try { year = Integer.parseInt(req.getParameter("year")); } catch (Exception ignored) { }

        AnimeFilter filter = AnimeFilter.builder()
                .title(title)
                .genre(genre)
                .studio(studio)
                .releaseYear(year)
                .build();

        List<AnimeDto> list = animeService.getAnimeList(filter);

        for (AnimeDto anime : list) {
            FileInfo cover = fileRepository.findById(anime.getId());
            if (cover != null) anime.setCoverFileId(cover.getId());
        }

        resp.setContentType("application/json;charset=UTF-8");

        mapper.writeValue(resp.getWriter(), list);
    }
}
