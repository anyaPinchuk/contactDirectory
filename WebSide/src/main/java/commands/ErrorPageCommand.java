package commands;

import javax.servlet.ServletException;
import java.io.IOException;

public class ErrorPageCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {
        forward("errorPage");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        forward("errorPage");
    }
}
