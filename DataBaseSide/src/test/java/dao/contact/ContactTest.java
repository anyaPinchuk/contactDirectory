package dao.contact;

import dao.ContactDAO;
import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.MessageError;
import org.junit.Test;

import java.util.List;


public class ContactTest {

    @Test
    public void addContact(){
        ContactDAO contactDAO = new ContactDAO();
        Contact contact = new Contact("anya", "pinchuk");
        try {
            contactDAO.insert(contact);
        }
        catch (GenericDAOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void update(){
        ContactDAO contactDAO = new ContactDAO();
        Contact contact;
        try {
           if (contactDAO.findByField("anya@gmail.com").isPresent()){
               contact = contactDAO.findByField("anya@gmail.com").get();
               contact.setCitizenship("belarus");
               contactDAO.updateById(contact.getId(), contact);
           } else{
               throw new GenericDAOException("Contact wasn't found");
           }
        }
        catch (GenericDAOException e){
            new MessageError(e.getMessage(), e);
        }
    }
    @Test
    public void delete(){
        ContactDAO contactDAO = new ContactDAO();
        Contact contact;
        try {
            if (contactDAO.findByField("anya@gmail.com").isPresent()){
                contact = contactDAO.findByField("anya@gmail.com").get();
                contactDAO.deleteById(contact.getId());
            } else{
                throw new GenericDAOException("Contact wasn't found");
            }
        }
        catch (GenericDAOException e){
            new MessageError(e.getMessage(), e);
        }
    }

    @Test
    public void findAll(){
        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contacts;
        try {
            contacts = contactDAO.findAll();
            System.out.println(contacts);
        }
        catch (GenericDAOException e){
            new MessageError(e.getMessage(), e);
        }
    }

}
