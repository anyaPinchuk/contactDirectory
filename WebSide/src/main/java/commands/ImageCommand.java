package commands;
import org.apache.commons.io.IOUtils;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

public class ImageCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {
        byte[] bytes = FileUploadDocuments.readDocument(request.getParameter("name"), true, null);
        response.getOutputStream().write(bytes);
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
