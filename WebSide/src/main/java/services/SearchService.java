package services;

import converters.AddressConverter;
import converters.ContactConverter;
import dto.ContactDTO;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchService {
    public List<ContactDTO> searchContacts(ContactDTO contactDTO, String dateCriteria) {
        ContactService contactService = new ContactService();
        AddressService addressService = new AddressService();
        List<Contact> contacts;
        ContactConverter contactConverter = new ContactConverter();
        AddressConverter addressConverter = new AddressConverter();
        Contact contact = contactConverter.toEntitySearch(contactDTO).get();
        Address address = addressConverter.toEntitySearch(contactDTO.getAddress()).get();
        if (StringUtils.isNotEmpty(address.getCountry()) && StringUtils.isNotEmpty(address.getCity()) &&
                StringUtils.isNotEmpty(address.getStreetAddress()) && StringUtils.isNotEmpty(address.getIndex())) {
            contacts = contactService.findByCriteria(contact, null, dateCriteria);
        } else contacts = contactService.findByCriteria(contact, address, dateCriteria);
        List<ContactDTO> contactDTOList = contacts.stream().map(obj -> {
            ContactDTO dto = contactConverter.toDTO(Optional.of(obj)).get();
            Address addressObj = addressService.findById(obj.getId());
            dto.setAddress(addressConverter.toDTO(Optional.of(addressObj)).get());
            return dto;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(contactDTOList)) return new ArrayList<>();
        else return contactDTOList;
    }

}
