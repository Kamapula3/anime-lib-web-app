package ru.itis.servlets;

import ru.itis.dto.AnimeDto;
import ru.itis.dto.CommentDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityAlreadyExistsException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.models.Anime;
import ru.itis.models.CharacterModel;
import ru.itis.models.Comment;
import ru.itis.models.FileInfo;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.character.CharacterRepository;
import ru.itis.repositories.comment.CommentRepository;
import ru.itis.repositories.files.FileRepository;
import ru.itis.repositories.genres.GenresRepository;
import ru.itis.repositories.studio.StudioRepository;
import ru.itis.repositories.user.UserFavoritesListRepository;
import ru.itis.services.CommentService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/animeInfo")
public class AnimeInformationServlet extends HttpServlet {

    private AnimeRepository animeRepository;
    private CharacterRepository characterRepository;
    private UserFavoritesListRepository userFavoritesListRepository;
    private CommentRepository commentRepository;
    private CommentService commentService;
    private FileRepository fileRepository;
    private StudioRepository studioRepository;
    private GenresRepository genresRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            HttpSession session = req.getSession();

            Integer userId = (Integer) session.getAttribute("userId");

            String idParam = req.getParameter("id");
            Integer animeId = null;
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    animeId = Integer.parseInt(idParam);
                } catch (NumberFormatException e) {
                    animeId = null;
                }
            }

            Anime anime = animeRepository.findById(animeId);

            AnimeDto animeDto = AnimeDto.builder()
                    .id(anime.getId())
                    .title(anime.getTitle())
                    .description(anime.getDescription())
                    .genres(genresRepository.getGenresByAnimeId(animeId))
                    .releaseYear(anime.getReleaseYear())
                    .studio(studioRepository.getStudio(anime.getStudioId()))
                    .rating(anime.getRating())
                    .build();

            req.setAttribute("anime", animeDto);

            boolean inFavorite = userFavoritesListRepository.isInFavorite(animeId, userId);

            req.setAttribute("inFavorite", inFavorite);


            List<CharacterModel> charactersList = characterRepository.findAllByAnimeId(animeId);

            List<CommentDto> commentList = commentService.getAllByAnimeId(animeId);

            if(inFavorite){
                int score = userFavoritesListRepository.getScore(animeId, userId);
                req.setAttribute("score", score);
            }

            FileInfo cover = fileRepository.findById(animeId);
            boolean hasCover = (cover != null);
            req.setAttribute("hasCover", hasCover);
            if (hasCover) {
                req.setAttribute("coverFileId", cover.getId());
            }

            req.setAttribute("commentList", commentList);
            req.setAttribute("charactersList", charactersList);

            req.getRequestDispatcher("/jsp/anime.jsp").forward(req, resp);

        } catch (DatabaseException e){
            resp.sendError(404, "Аниме не найдено");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            HttpSession session = req.getSession();

            Integer userId = (Integer) session.getAttribute("userId");

            String idParam = req.getParameter("animeId");
            Integer animeId = null;
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    animeId = Integer.parseInt(idParam);
                } catch (NumberFormatException e) {
                    animeId = null;
                }
            }

            String action = req.getParameter("action");
            String commentText = req.getParameter("comment_text");

            if(req.getParameter("rating") != null){

                Integer rating = Integer.valueOf(req.getParameter("rating"));

                animeRepository.countRating(animeId, rating, userId);
            } else if("favorite".equals(action)){
                userFavoritesListRepository.addAnimeToFavorites(animeId, userId);
            } else if("delete_favorite".equals(action)){
                userFavoritesListRepository.removeAnimeFromFavorite(animeId, userId);
            } else if("write_comment".equals(action)){
                if (commentText != null){
                    commentService.createComment(userId, animeId, commentText);
                } else {
                    req.setAttribute("error", "Поле не может быть пустым");
                    req.getRequestDispatcher("/jsp/anime.jsp").forward(req, resp);
                    return;
                }
            } else if("delete_comment".equals(action)){
                Comment comment = commentRepository.getCommentById(animeId, userId);
                System.out.println();
                commentService.removeComment(comment.getId(), userId, animeId);
            }

            resp.sendRedirect(req.getContextPath() + "/animeInfo?id=" + animeId);

        } catch (EntityAlreadyExistsException e) {
            req.setAttribute("error", "Аниме уже в избранном");
            req.getRequestDispatcher("/jsp/anime.jsp").forward(req, resp);
        } catch (EntityNotFoundException e) {
            resp.sendError(404, "Запрашиваемый объект не найден");
        } catch (DatabaseException e) {
            req.setAttribute("error", "Произошла ошибка при работе с базой данных");
            req.getRequestDispatcher("/jsp/anime.jsp").forward(req, resp);
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        animeRepository = (AnimeRepository) config.getServletContext().getAttribute("animeRepository");
        characterRepository = (CharacterRepository) config.getServletContext().getAttribute("characterRepository");
        userFavoritesListRepository = (UserFavoritesListRepository) config.getServletContext().getAttribute("userFavoritesListRepository");
        commentRepository = (CommentRepository) config.getServletContext().getAttribute("commentRepository");
        commentService = (CommentService) config.getServletContext().getAttribute("commentService");
        fileRepository = (FileRepository) config.getServletContext().getAttribute("fileRepository");
        studioRepository = (StudioRepository) config.getServletContext().getAttribute("studioRepository");
        genresRepository = (GenresRepository) config.getServletContext().getAttribute("genresRepository");
    }
}