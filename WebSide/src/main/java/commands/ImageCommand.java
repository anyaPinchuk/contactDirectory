package commands;

import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;

public class ImageCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {
        response.getOutputStream().write(FileUploadDocuments.readFile(request.getParameter("name")));
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
