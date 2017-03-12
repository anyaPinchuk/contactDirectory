package commands;

import converters.AddressConverter;
import converters.ContactConverter;

import javax.servlet.ServletException;
import java.io.IOException;

public class AddContactCommand extends FrontCommand{
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;

    @Override
    public void processGet() throws ServletException, IOException {
        forward("addContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {
        String contactName = request.getParameter("name");
        forward("main");
    }
}
