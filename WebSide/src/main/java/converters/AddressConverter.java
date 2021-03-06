package converters;


import dto.AddressDTO;
import entities.Address;

import java.util.Optional;

public class AddressConverter extends Converter<AddressDTO, Address>{
    @Override
    public Optional<Address> toEntity(AddressDTO dto) {
        if(dto == null) return Optional.empty();
        return Optional.of(new Address(dto.getId(), dto.getCountry(), dto.getCity(), dto.getStreetAddress(),
                dto.getIndex(), null));
    }

    @Override
    public Optional<AddressDTO> toDTO(Optional<? extends Address> entity) {
        if (entity != null && entity.isPresent())
            return Optional.of(new AddressDTO(entity.get().getId(), entity.get().getCountry(), entity.get().getCity(),
                    entity.get().getStreetAddress(), entity.get().getIndex()));
        return Optional.empty();
    }

    public Optional<Address> toEntitySearch(AddressDTO dto) {
        if(dto == null) return Optional.empty();
        return Optional.of(new Address(null, dto.getCountry(), dto.getCity(), dto.getStreetAddress(),
                dto.getIndex(), null));
    }
}
