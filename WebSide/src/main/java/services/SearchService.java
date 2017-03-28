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
        AddressService addressService = new AddressService();
        List<Contact> contacts = new ArrayList<>();
        ContactConverter contactConverter = new ContactConverter();
        AddressConverter addressConverter = new AddressConverter();
        Contact contact = contactConverter.toEntitySearch(contactDTO).get();
        Address address = addressConverter.toEntitySearch(contactDTO.getAddress()).get();
        if (address.getCountry().equals("") && address.getCity().equals("") && address.getStreetAddress().equals("")
                && address.getIndex().equals("")) {
            contacts = contactService.findByCriteria(contact, null, dateCriteria);
        } else contacts = contactService.findByCriteria(contact, address, dateCriteria);
        List<ContactDTO> contactDTOList;
        contactDTOList = contacts.stream().map(obj -> {
            ContactDTO dto = contactConverter.toDTO(Optional.of(obj)).get();
            Address addressObj = addressService.findById(obj.getId());
            dto.setAddress(addressConverter.toDTO(Optional.of(addressObj)).get());
            return dto;
        }).collect(Collectors.toList());
        if (contacts.size() == 0) return new ArrayList<>();
        else
            return contactDTOList;
    }

}
