package utilities;

import entities.Attachment;
import entities.Contact;
import entities.PhoneNumber;
import exceptions.MessageError;
import org.apache.commons.collections4.CollectionUtils;
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
        if (StringUtils.isEmpty(contact.getEmail().trim()) || StringUtils.isEmpty(contact.getName().trim()) ||
                StringUtils.isEmpty(contact.getSurname().trim())) {
            error.addMessage("First name, second name and email can not be empty");
            return false;
        } else return true;
    }

    public boolean validPhones(List<PhoneNumber> numbers) {
        for (PhoneNumber obj : numbers) {
            if (StringUtils.isEmpty(obj.getNumberType().trim())) {
                error.addMessage("Number type should be specified");
                return false;
            }
            try {
                Long.parseLong(obj.getCountryCode());
                Long.parseLong(obj.getOperatorCode());
                Long.parseLong(obj.getOperatorCode());
            } catch (NumberFormatException e) {
                error.addMessage("Phone can only have number characters");
                return false;
            }
        }
        return true;
    }

    public boolean validAttachments(List<Attachment> attachments) {
        if (CollectionUtils.isEmpty(attachments)) return true;
        for (Attachment obj : attachments) {
            if (StringUtils.isEmpty(obj.getFileName().trim())) {
                error.addMessage("File name and date of download should be specified");
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
