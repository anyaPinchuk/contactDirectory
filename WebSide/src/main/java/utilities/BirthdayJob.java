package utilities;

import entities.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import services.ContactService;

import java.util.List;

public class BirthdayJob implements Job {

    /**
     * Find contacts with today's date of birth and sends found contacts to a system administrator
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ContactService contactService = new ContactService();
        List<Contact> contactList = contactService.findByDate();
        if (!CollectionUtils.isEmpty(contactList)){
            StringBuilder message = new StringBuilder("People who have birthday today: \n");
            contactList.forEach(contact -> {
                message.append(contact.getName()).append(" ").append(contact.getSurname()).append(" with email address ")
                .append(contact.getEmail()).append(";\n");
            });
            MailSender.sendMailToAdmin(message.toString());
        }
    }
}
