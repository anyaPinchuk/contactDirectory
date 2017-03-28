package commands;

import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;

public class ImageCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {
        String[] strings = request.getParameter("name").split(";");
        byte[] bytes;
        Long contact_id = 0L;
        try {
            contact_id = Long.valueOf(strings[1]);
        } catch (NumberFormatException e) {
            LOG.error("Error getting document because of contact_id ");
        }
        bytes = FileUploadDocuments.readDocument(strings[0], true, contact_id);
        response.getOutputStream().write(bytes);
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
