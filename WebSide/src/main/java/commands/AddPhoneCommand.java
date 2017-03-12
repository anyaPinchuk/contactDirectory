package commands;

import entities.PhoneNumber;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AddPhoneCommand extends  FrontCommand{
    public static List<PhoneNumber> numbers = new LinkedList<>();

    @Override
    public void processGet() throws ServletException, IOException {

    }

    @Override
    public void processPost() throws ServletException, IOException {
        String countryCode = request.getParameter("countryCode");
        String operatorCode = request.getParameter("operatorCode");
        String number = request.getParameter("number");
        String type = request.getParameter("type");
        String comment = request.getParameter("comment");

        PhoneNumber phoneNumber = new PhoneNumber(null, countryCode, operatorCode, number, type, comment, null);
        numbers.add(phoneNumber);
        request.setAttribute("numbers", numbers);
        AddContactCommand addContactCommand = new AddContactCommand();
        addContactCommand.init(context, request, response);
        addContactCommand.processGet();
    }
}
