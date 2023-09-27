package com.example.rotiscnz.mappers;

import com.example.rotiscnz.dtos.cartItemDTOs.CartItemCreateDTO;
import com.example.rotiscnz.dtos.cartItemDTOs.CartItemResponseDTO;
import com.example.rotiscnz.entities.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "orderID", ignore = true)
    @Mapping(target = "id", ignore = true)
    CartItemEntity toCartItemEntityFromCartItemCreateDTO(CartItemCreateDTO cartItemCreateDTO);
}
