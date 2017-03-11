package controllers;

import commands.ContactsCommand;
import commands.FrontCommand;
import commands.UnknownCommand;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/*")
public class FrontControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.init(getServletContext(), request, response);
        command.process();
    }

    private FrontCommand getCommand(HttpServletRequest request) {
        try {
            String command = request.getRequestURI().substring(5);
            Class type = Class.forName(String.format(
                    "commands.%sCommand", command.substring(0, 1).toUpperCase() + command.substring(1)));

            return (FrontCommand) type
                    .asSubclass(FrontCommand.class)
                    .newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            return new UnknownCommand();
        }
    }

}
