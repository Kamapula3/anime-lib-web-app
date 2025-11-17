package ru.itis.servlets;

import ru.itis.dto.AnimeDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.models.FileInfo;
import ru.itis.repositories.files.FileRepository;
import ru.itis.services.AnimeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/uploadAnimeCover")
public class UploadAnimeCoverServlet extends HttpServlet {

    private AnimeService animeService;
    private FileRepository fileRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        animeService = (AnimeService) config.getServletContext().getAttribute("animeService");
        fileRepository = (FileRepository) config.getServletContext().getAttribute("fileRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<AnimeDto> allAnime = animeService.getAllAnimeList();

            List<AnimeDto> animeWithoutCover = allAnime.stream()
                    .filter(anime -> {
                        FileInfo cover = fileRepository.findById(anime.getId());
                        return cover == null;
                    })
                    .collect(Collectors.toList());

            String success = req.getParameter("success");
            String added = "Аниме успешно добавлено";

            if (success != null && success.equals("added")){
                req.setAttribute("added", added);
            }

            req.setAttribute("animeWithoutCover", animeWithoutCover);
            req.getRequestDispatcher("/jsp/admin/uploadAnimeCover.jsp").forward(req, resp);

        } catch (DatabaseException e) {
            req.setAttribute("error", "Ошибка при загрузке списка аниме");
            req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        }
    }
}
