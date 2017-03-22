package commands;

import exceptions.GenericDAOException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

public class DeleteContactCommand extends FrontCommand{
    @Override
    public void processGet() throws ServletException, IOException {
    }

    @Override
    public void processPost() throws ServletException, IOException {
        String[] values = request.getParameterValues("chosenContacts");
        Long[] ids = Arrays.stream(values).map(Long::valueOf).toArray(Long[]::new);
        Arrays.stream(ids).forEach(obj -> {
            try {
                contactDAO.deleteById(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing  delete by id in DeleteContactCommand");
                e.printStackTrace();
            }
        });

    }
}
