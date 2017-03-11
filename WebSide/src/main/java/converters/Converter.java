package converters;

import dto.DTO;

import java.util.Optional;

public abstract class Converter<S extends DTO, T > {

    public abstract Optional<T> toEntity(S dto);

    public abstract Optional<S> toDTO(Optional<? extends T> entity);
}
