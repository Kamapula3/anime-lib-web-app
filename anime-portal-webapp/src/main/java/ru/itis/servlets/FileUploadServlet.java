package ru.itis.servlets;

import ru.itis.exceptions.DatabaseException;
import ru.itis.services.FileService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/files")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private FileService fileService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/admin/fileUpload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("file");
        String idParam = req.getParameter("id");
        Integer id = Integer.parseInt(idParam);

        try {
            fileService.saveFileToStorage(part.getInputStream(),
                    id,
                    part.getSubmittedFileName(),
                    part.getContentType(),
                    part.getSize());
        } catch (DatabaseException e){
            req.setAttribute("error", "Не удалось сохранить файл");
            resp.sendRedirect(req.getContextPath() + "/adminPage");
        }


        resp.sendRedirect(req.getContextPath() + "/adminPage");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        fileService = (FileService) config.getServletContext().getAttribute("fileService");
    }
}
