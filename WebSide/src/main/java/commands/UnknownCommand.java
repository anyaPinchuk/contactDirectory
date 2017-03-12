package commands;

import javax.servlet.ServletException;
import java.io.IOException;

public class UnknownCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {
        forward("unknown");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        forward("unknown");
    }
}
