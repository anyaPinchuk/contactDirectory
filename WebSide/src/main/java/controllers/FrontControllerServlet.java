package controllers;

import commands.FrontCommand;
import commands.UnknownCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/*")
public class FrontControllerServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(FrontControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.init(getServletContext(), request, response);
        command.processGet();

    }

    private FrontCommand getCommand(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
            String command = request.getRequestURI().substring(5);
            System.out.println(command);
            LOG.info("handle request from user at URL " + command + " starting ");
            Class type = Class.forName(String.format(
                    "commands.%sCommand", command.substring(0, 1).toUpperCase() + command.substring(1)));

            return (FrontCommand) type.asSubclass(FrontCommand.class).newInstance();

        } catch (Exception e) {
            LOG.error("such URL doesn't exist, redirect to UnknownCommand", e);
            return new UnknownCommand();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.init(getServletContext(), request, response);
        command.processPost();
    }

}
