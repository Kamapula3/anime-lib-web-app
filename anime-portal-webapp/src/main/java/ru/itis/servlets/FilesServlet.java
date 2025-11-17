package ru.itis.servlets;

import ru.itis.models.FileInfo;
import ru.itis.services.FileService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/uploaded/files")
public class FilesServlet extends HttpServlet {

    private FileService fileService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileIdParam = req.getParameter("id");

        Integer fileId  = Integer.parseInt(fileIdParam);

        FileInfo fileInfo = fileService.getFileInfo(fileId);

        resp.setContentType(fileInfo.getType());

        resp.setContentLength(fileInfo.getSize().intValue());

        resp.setHeader("Content-Disposition", "filename=\"" + fileInfo.getOriginalFileName() + "\"");

        fileService.writeFileFromStorage(fileId, resp.getOutputStream());
        resp.flushBuffer();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        fileService = (FileService) config.getServletContext().getAttribute("fileService");
    }
}
