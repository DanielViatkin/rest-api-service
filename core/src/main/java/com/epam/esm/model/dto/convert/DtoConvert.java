package com.epam.esm.model.dto.convert;

public interface DtoConvert<E, D> {
    E convertToEntity(D dto);
    D convertToDto(E entity);
}
