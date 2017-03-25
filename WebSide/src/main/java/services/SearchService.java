package services;

import converters.AddressConverter;
import converters.ContactConverter;
import dto.ContactDTO;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchService {
    public List<ContactDTO> searchContacts(ContactDTO contactDTO, String dateCriteria) {
        ContactService contactService = new ContactService();
        ContactConverter contactConverter = new ContactConverter();
        AddressConverter addressConverter = new AddressConverter();
        Contact contact = contactConverter.toEntity(contactDTO).get();
        Address address = addressConverter.toEntity(contactDTO.getAddress()).get();
        List<Contact> contacts = contactService.findByCriteria(contact, address, dateCriteria);
        if (contacts.size() == 0) return new ArrayList<>();
        else
            return contacts.stream().map(obj -> contactConverter.toDTO(Optional.of(obj)).get()).collect(Collectors.toList());
    }

}
