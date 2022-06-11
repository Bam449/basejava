package ru.javawebinar.basejava.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/resume")
public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset-UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write("" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "  border:1px solid black;\n" +
                "}\n" +
                "</style>\n" +
                "<body>\n" +
                "\n" +
                "<h2>Resume table</h2>\n" +
                "\n" +
                "<table style=\"width:100%\">\n" +
                "  <tr>\n" +
                "    <th>UUID</th>\n" +
                "    <th>Full Name</th>\n" +
                "  </tr>\n" +
                "  <tr>\n");
        for (Resume r : storage.getAllSorted()) {
            writer.write(
                    "    <td>" + r.getUuid() + "</td>\n" +
                            "    <td>" + r.getFullName() + "</td>\n" +
                            "  </tr>\n"
            );
        }
        writer.write(
                "</table>\n" +
                        "\n" +
                        "<p>Честно содрал это из ссылки из домашнего задания.</p>\n" +
                        "<p>Здесь может быть Ваша реклама!</p>\n" +
                        "<p>Недорого!!!!</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    public void init(){
        storage = Config.getINSTANCE().getStorage();
    }
}

