package com.example.rotiscnz.mappers;

import com.example.rotiscnz.dtos.ItemDTOs.ItemCreateDTO;
import com.example.rotiscnz.dtos.ItemDTOs.ItemResponseDTO;
import com.example.rotiscnz.entities.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartItemsById", ignore = true)
    ItemEntity toItemEntityFromItemCreateDTO(ItemCreateDTO itemCreateDTO);

    ItemResponseDTO toItemResponseDTOFromItemEntity(ItemEntity item);
}
