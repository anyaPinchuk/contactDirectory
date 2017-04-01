package utilities;

import entities.Attachment;
import entities.Contact;
import entities.PhoneNumber;
import exceptions.MessageError;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Validator {
    private MessageError error;

    public Validator(MessageError error) {
        this.error = error;
    }

    public boolean validContact(Contact contact) {
        return !(StringUtils.isEmpty(contact.getEmail().trim()) || StringUtils.isEmpty(contact.getName().trim()) ||
                StringUtils.isEmpty(contact.getSurname().trim()));
    }

    public boolean validPhones(List<PhoneNumber> numbers) {
        for (PhoneNumber obj : numbers) {
            try {
                Long.parseLong(obj.getCountryCode());
                Long.parseLong(obj.getOperatorCode());
                Long.parseLong(obj.getOperatorCode());
            } catch (NumberFormatException e) {
                error.addMessage("Phone can only have number characters");
                return false;
            }
            if (StringUtils.isEmpty(obj.getComment().trim()) || StringUtils.isEmpty(obj.getNumberType().trim())) {
                return false;
            }
        }
        return true;
    }

    public boolean validAttachments(List<Attachment> attachments) {
        for (Attachment obj : attachments) {
            if (StringUtils.isEmpty(obj.getComment().trim()) || StringUtils.isEmpty(obj.getFileName().trim())) {
                return false;
            }
        }
        return true;
    }

    public MessageError getError() {
        return error;
    }

    public void setError(MessageError error) {
        this.error = error;
    }
}
