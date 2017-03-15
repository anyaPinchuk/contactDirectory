package converters;

import dto.PhoneDTO;
import entities.PhoneNumber;

import java.util.Optional;

public class PhoneConverter extends Converter<PhoneDTO, PhoneNumber>{
    @Override
    public Optional<PhoneNumber> toEntity(PhoneDTO dto) {
        if(dto == null) return Optional.empty();
        String[] strings = dto.getNumber().split(" ");
        String countryCode = strings[0].substring(1);
        String operatorCode = strings[1];
        String number = strings[2];
        return Optional.of(new PhoneNumber(dto.getId(), countryCode, operatorCode, number, dto.getNumberType(), dto.getComment(),
                dto.getContactDTO().getId()));
    }

    @Override
    public Optional<PhoneDTO> toDTO(Optional<? extends PhoneNumber> entity) {
        if (entity != null && entity.isPresent())
            return Optional.of(new PhoneDTO(entity.get().getId(), entity.get().getCountryCode() +
                    entity.get().getOperatorCode() + entity.get().getNumber(), entity.get().getNumberType(),
                    entity.get().getComment(), null));
        return Optional.empty();
    }
}
