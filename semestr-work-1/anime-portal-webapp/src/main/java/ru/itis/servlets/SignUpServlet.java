package ru.itis.servlets;

import ru.itis.dto.SignUpForm;
import ru.itis.dto.UserDto;
import ru.itis.exceptions.DatabaseException;
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

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

    UserRepository userRepository;
    AuthService authService;
    ProfileService profileService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if(username == null || username.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()){
            req.setAttribute("error", "Все поля обязательны для заполнения!");
            req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, resp);
            return;
        }

        SignUpForm user = SignUpForm.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        try {

            Boolean isSignUp = authService.signUp(user, req);

            if (isSignUp){

                UserDto userDto = profileService.getUserProfileInfo(email);
                User userFromStorage = userRepository.findByEmail(email);
                String roleName = userRepository.getUserRole(userFromStorage.getRoleId());

                HttpSession session = req.getSession(true);

                session.setAttribute("authenticated", true);
                session.setAttribute("profile", userDto);
                session.setAttribute("roleName", roleName);
                session.setAttribute("userId", userDto.getId());

                resp.sendRedirect("/mainPage");
            } else {
                req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, resp);
            }

        } catch (DatabaseException e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка сервера. Попробуйте позже.");
            req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, resp);
        }


    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userRepository = (UserRepository)  config.getServletContext().getAttribute("userRepository");
        authService = (AuthService) config.getServletContext().getAttribute("authService");
        profileService = (ProfileService) config.getServletContext().getAttribute("profileService");
    }
}
