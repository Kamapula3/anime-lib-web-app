package ru.itis.listeners;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.itis.repositories.anime.AnimeRepository;
import ru.itis.repositories.anime.impl.AnimeRepositoryImpl;
import ru.itis.repositories.character.CharacterRepository;
import ru.itis.repositories.character.impl.CharacterRepositoryImpl;
import ru.itis.repositories.comment.CommentRepository;
import ru.itis.repositories.comment.impl.CommentRepositoryJdbcImpl;
import ru.itis.repositories.files.FileRepository;
import ru.itis.repositories.files.FileRepositoryJdbcImpl;
import ru.itis.repositories.genres.GenresRepository;
import ru.itis.repositories.genres.impl.GenresRepositoryJdbcImpl;
import ru.itis.repositories.studio.StudioRepository;
import ru.itis.repositories.studio.impl.StudioRepositoryJdbcImpl;
import ru.itis.repositories.user.UserFavoritesListRepository;
import ru.itis.repositories.user.UserRepository;
import ru.itis.repositories.user.impl.UserRepositoryJdbcImpl;
import ru.itis.services.*;
import ru.itis.services.impl.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_USERNAME = dotenv.get("DB_USERNAME");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");
    private static final String DB_URL = dotenv.get("DB_URL");

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){
        ServletContext servletContext = servletContextEvent.getServletContext();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setUrl(DB_URL);

        UserRepository userRepository = new UserRepositoryJdbcImpl(dataSource);
        AnimeRepository animeRepository = new AnimeRepositoryImpl(dataSource);
        StudioRepository studioRepository = new StudioRepositoryJdbcImpl(dataSource);
        CharacterRepository characterRepository = new CharacterRepositoryImpl(dataSource);
        CommentRepository commentRepository = new CommentRepositoryJdbcImpl(dataSource);
        FileRepository fileRepository = new FileRepositoryJdbcImpl(dataSource);
        GenresRepository genresRepository = new GenresRepositoryJdbcImpl(dataSource);
        UserFavoritesListRepository userFavoritesListRepository = new UserFavoritesListRepository(dataSource, animeRepository, studioRepository, genresRepository);
        AuthService authService = new AuthServiceImpl(userRepository);
        AnimeService animeService = new AnimeServiceImpl(animeRepository, studioRepository, genresRepository);
        ProfileService profileService = new ProfileServiceImpl(userRepository);
        CommentServiceImpl commentService = new CommentServiceImpl(commentRepository);
        FileService fileService = new FileServiceImpl(fileRepository);
        UserService userService = new UserServiceImpl(userRepository);

        servletContext.setAttribute("animeRepository", animeRepository);
        servletContext.setAttribute("studioRepository", studioRepository);
        servletContext.setAttribute("commentRepository", commentRepository);
        servletContext.setAttribute("userRepository", userRepository);
        servletContext.setAttribute("characterRepository", characterRepository);
        servletContext.setAttribute("userFavoritesListRepository", userFavoritesListRepository);
        servletContext.setAttribute("fileRepository", fileRepository);
        servletContext.setAttribute("genresRepository", genresRepository);
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("animeService", animeService);
        servletContext.setAttribute("profileService", profileService);
        servletContext.setAttribute("commentService", commentService);
        servletContext.setAttribute("fileService", fileService);
        servletContext.setAttribute("userService", userService);

        servletContext.setAttribute("authenticated", false);
        servletContext.setAttribute("inProfile", false);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){

    }
}
