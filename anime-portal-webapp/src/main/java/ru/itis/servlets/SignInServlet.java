package ru.itis.servlets;

import ru.itis.dto.SignInForm;
import ru.itis.dto.UserDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.models.User;
import ru.itis.repositories.user.UserRepository;
import ru.itis.services.AuthService;
import ru.itis.services.ProfileService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {

    AuthService authService;
    UserRepository userRepository;
    ProfileService profileService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/signIn.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login == null || login.isEmpty() || password == null || password.isEmpty()){
            req.setAttribute("error", "Все поля обязательны для заполнения!");
            req.getRequestDispatcher("/jsp/user/signIn.jsp").forward(req, resp);
            return;
        }

        SignInForm user = SignInForm.builder()
                .login(login)
                .password(password)
                .build();

        try {

            Boolean isSignIn = authService.signIn(user, req);

            if(isSignIn){

                UserDto userDto = profileService.getUserProfileInfo(login);
                User userFromStorage = userRepository.findByEmail(login);
                String roleName = userRepository.getUserRole(userFromStorage.getRoleId());

                HttpSession session = req.getSession(true);

                session.setAttribute("authenticated", true);
                session.setAttribute("userEmail", login);
                session.setAttribute("roleName", roleName);
                session.setAttribute("profile", userDto);

                session.setAttribute("userId", userDto.getId());

                resp.sendRedirect("/mainPage");
            } else {
                req.getRequestDispatcher("/jsp/user/signIn.jsp").forward(req, resp);
            }
        } catch (EntityNotFoundException e) {
            req.setAttribute("error", "Пользователь не найден");
            req.getRequestDispatcher("/jsp/user/signIn.jsp").forward(req, resp);
        } catch (DatabaseException e){
            req.setAttribute("error", "Внутренняя ошибка сервера. Попробуйте позже.");
            req.getRequestDispatcher("/jsp/user/signIn.jsp").forward(req, resp);
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        authService = (AuthService) config.getServletContext().getAttribute("authService");
        userRepository = (UserRepository)  config.getServletContext().getAttribute("userRepository");
        profileService = (ProfileService) config.getServletContext().getAttribute("profileService");
    }
}
