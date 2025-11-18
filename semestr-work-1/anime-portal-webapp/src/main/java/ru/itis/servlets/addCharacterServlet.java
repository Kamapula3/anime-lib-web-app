package ru.itis.servlets;

import ru.itis.dto.AnimeDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.models.CharacterModel;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.character.CharacterRepository;
import ru.itis.services.AnimeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/addCharacter")
public class addCharacterServlet extends HttpServlet {

    private AnimeRepository animeRepository;
    private CharacterRepository characterRepository;
    private AnimeService animeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<AnimeDto> animeList = animeService.getAllAnimeList();
            List<CharacterModel> characters = characterRepository.findAll();
            req.setAttribute("animeList", animeList);
            req.setAttribute("characters", characters);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось найти список аниме");
            doGet(req, resp);
            return;
        }

        req.getRequestDispatcher("/jsp/admin/addCharacter.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");


        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String description = req.getParameter("description");
        String animeName = req.getParameter("animeName");

        if (name == null || name.trim().isEmpty()){
            req.setAttribute("error", "Имя обязательно");
            doGet(req, resp);
            return;
        }

        if (surname == null || surname.trim().isEmpty()){
            req.setAttribute("error", "Фамилия обязательна");
            doGet(req, resp);
            return;
        }

        if (description == null || description.trim().isEmpty()){
            req.setAttribute("error", "Описание обязательно");
            doGet(req, resp);
            return;
        }

        if (animeName == null || animeName.trim().isEmpty()){
            req.setAttribute("error", "Аниме обязательно");
            doGet(req, resp);
            return;
        }


        try {
            CharacterModel characterModel = CharacterModel.builder()
                    .name(name)
                    .surname(surname)
                    .description(description)
                    .anime_id(animeRepository.getAnimeId(animeName))
                    .build();

            characterRepository.save(characterModel);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось сохранить персонажа");
            doGet(req, resp);
            return;
        }

        resp.sendRedirect("/adminPage?success=added&type=character");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        animeRepository = (AnimeRepository) config.getServletContext().getAttribute("animeRepository");
        characterRepository = (CharacterRepository) config.getServletContext().getAttribute("characterRepository");
        animeService = (AnimeService) config.getServletContext().getAttribute("animeService");
    }
}
