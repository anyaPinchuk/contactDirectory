package converters;

import dto.ContactDTO;
import entities.Contact;

import java.util.Optional;

/**
 * Created by Анна on 11.03.2017.
 */
public class ContactConverter extends Converter<ContactDTO, Contact>{
    @Override
    public Optional<Contact> toEntity(ContactDTO dto) {
        if(dto == null) return Optional.empty();
        return Optional.of(new Contact(dto.getId(),dto.getName(), dto.getSurname(), dto.getThirdName(),
                dto.getDateOfBirth(), dto.getSex(), dto.getCitizenship(), dto.getMaritalStatus(), dto.getWebSite(),
                dto.getEmail(), dto.getJob(), dto.getAddress().getId()));
    }

    @Override
    public Optional<ContactDTO> toDTO(Optional<? extends Contact> entity) {
        if (entity != null && entity.isPresent())
            return Optional.of(new ContactDTO(entity.get().getId(), entity.get().getName(), entity.get().getSurname(),
                    entity.get().getThirdName(), entity.get().getDateOfBirth(), entity.get().getSex(),
                    entity.get().getCitizenship(), entity.get().getMaritalStatus(), entity.get().getWebSite(),
                    entity.get().getEmail(), entity.get().getJob(), null));
        return Optional.empty();
    }
}
