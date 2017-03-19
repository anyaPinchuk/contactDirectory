package commands;

import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;

public class DocumentCommand extends FrontCommand{

    @Override
    public void processGet() throws ServletException, IOException {
        String[] strings = request.getParameter("name").split(";");
        byte[] bytes;
        Long contact_id = 0L;
        try{
            contact_id = Long.valueOf(strings[1]);
        }
        catch (NumberFormatException e){
            LOG.error("Error getting document because of contact_id = null");
        }
        bytes = FileUploadDocuments.readDocument(strings[0],false, contact_id);
        if (bytes == null){
            bytes = "<h1>Unable to load content</h1>".getBytes();
        }
        response.getOutputStream().write(bytes);

    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
