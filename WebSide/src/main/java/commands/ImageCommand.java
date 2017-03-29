package commands;

import org.apache.commons.lang3.StringUtils;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;

public class ImageCommand extends FrontCommand {
    @Override
    public void processGet() throws ServletException, IOException {
        String fileName = request.getParameter("name");
        String[] strings = new String[]{"", ""};
        Long contact_id = 0L;
        if (StringUtils.isNotEmpty(fileName)) {
            try {
                strings = fileName.split(";");
                contact_id = Long.valueOf(strings[1]);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                LOG.error("Error getting document because of contact_id ");
            }
        }
        byte[] bytes = FileUploadDocuments.readDocument(strings[0], true, contact_id);
        response.getOutputStream().write(bytes);

    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
