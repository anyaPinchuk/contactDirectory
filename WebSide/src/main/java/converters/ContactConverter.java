package converters;

import dto.ContactDTO;
import entities.Contact;
import org.joda.time.DateTime;

import java.sql.Date;
import java.util.Optional;

public class ContactConverter extends Converter<ContactDTO, Contact> {
    @Override
    public Optional<Contact> toEntity(ContactDTO dto) {
        return null;
    }

    public Optional<Contact> toEntitySearch(ContactDTO dto) {
        if (dto == null) return Optional.empty();
        Date date = null;
        if (dto.getDateOfBirth() != null) {
            date = new Date(dto.getDateOfBirth().toDate().getTime());
        }
        return Optional.of(new Contact(null, dto.getName(), dto.getSurname(), dto.getThirdName(),
                date, dto.getGender(), dto.getCitizenship(), dto.getMaritalStatus(), null,
                null, null));
    }

    @Override
    public Optional<ContactDTO> toDTO(Optional<? extends Contact> entity) {
        if (entity != null && entity.isPresent())
            return Optional.of(new ContactDTO(entity.get().getId(), entity.get().getName(), entity.get().getSurname(),
                    entity.get().getThirdName(),
                    entity.get().getDateOfBirth() == null ? null : new DateTime(entity.get().getDateOfBirth()),
                    entity.get().getGender(), entity.get().getCitizenship(), entity.get().getMaritalStatus(),
                    entity.get().getWebSite(), entity.get().getEmail(), entity.get().getJob(), null));
        return Optional.empty();
    }
}
