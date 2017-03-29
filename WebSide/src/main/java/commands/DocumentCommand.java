package commands;

import org.apache.commons.lang3.StringUtils;
import utilities.FileUploadDocuments;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class DocumentCommand extends FrontCommand {

    @Override
    public void processGet() throws ServletException, IOException {
        String fileName = request.getParameter("name");
        LOG.info("load document by filename {}", fileName);
        byte[] bytes = null;
        Long contact_id;
        String[] strings;
        if (StringUtils.isNotEmpty(fileName)) {
            try {
                strings = fileName.split(";");
                contact_id = Long.valueOf(strings[1]);
                bytes = FileUploadDocuments.readDocument(strings[0], false, contact_id);
                String MIMEType = Files.probeContentType(Paths.get(FileUploadDocuments.getFileDirectory(false) +
                        strings[0]));
                if (MIMEType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                    response.setContentType(MIMEType);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                bytes = "<h1>Unable to load content</h1>".getBytes();
                LOG.error("Error getting document");
            }
        }
        if (bytes == null){
            bytes = "<h1>Wrong parameter name</h1>".getBytes();
        }
        response.getOutputStream().write(bytes);
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
