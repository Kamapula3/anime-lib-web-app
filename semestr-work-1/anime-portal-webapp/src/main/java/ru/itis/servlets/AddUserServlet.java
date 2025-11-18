package ru.itis.servlets;

import ru.itis.dto.SignUpForm;
import ru.itis.dto.UserDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<UserDto> users = userService.getAllUsers();
            req.setAttribute("users", users);
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось найти список пользователей");
        }

        req.getRequestDispatcher("/jsp/admin/addUser.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String roleParam = req.getParameter("role");
        Integer roleId = null;

        if (roleParam != null){
            roleId = Integer.parseInt(roleParam.trim());
        }

        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {

            req.setAttribute("error", "Все поля обязательны для заполнения!");

            try {
                List<UserDto> users = userService.getAllUsers();
                req.setAttribute("users", users);
            } catch (DatabaseException e) {
                req.setAttribute("error", "Не удалось загрузить список пользователей");
            }

            req.getRequestDispatcher("/jsp/admin/addUser.jsp").forward(req, resp);
            return;
        }

        SignUpForm user = SignUpForm.builder()
                .username(username)
                .email(email)
                .password(password)
                .roleId(roleId)
                .build();

        try {

            Boolean isSignUp = userService.addUser(user, req);

            if (isSignUp){
                resp.sendRedirect("/adminPage?success=added&type=user");
            } else {
                List<UserDto> users = userService.getAllUsers();
                req.setAttribute("users", users);
                req.getRequestDispatcher("/jsp/admin/addUser.jsp").forward(req, resp);
            }

        } catch (DatabaseException e) {
            try {
                List<UserDto> users = userService.getAllUsers();
                req.setAttribute("users", users);
            } catch (DatabaseException ex) {
                req.setAttribute("error", "Не удалось загрузить список пользователей");
            }
        }

        req.getRequestDispatcher("/jsp/admin/addUser.jsp").forward(req, resp);

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute("userService");
    }
}
