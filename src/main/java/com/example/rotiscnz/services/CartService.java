package com.example.rotiscnz.services;

import com.example.rotiscnz.dtos.ResponseDTO;
import com.example.rotiscnz.dtos.cartDTOs.CartCreateDTO;
import com.example.rotiscnz.dtos.cartDTOs.CartMapperDTO;
import com.example.rotiscnz.dtos.cartDTOs.CartResponseDTO;
import com.example.rotiscnz.entities.CartEntity;
import com.example.rotiscnz.mappers.CartMapper;
import com.example.rotiscnz.repositories.CartRepository;
import com.example.rotiscnz.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository repository;
    private final CartMapper mapper;
    private final UserRepository userRepository;
    public ResponseDTO<CartResponseDTO> createCart(Long id){
        CartEntity cart = new CartEntity();
        List<CartEntity> cartEntities = repository.findAll();
        if(!cartEntities.isEmpty()) {
            for (CartEntity e : cartEntities) {
                if (Objects.equals(e.getUserByUserId(), id)) {
                    CartResponseDTO cartResponseDTO = new CartResponseDTO(e.getId(), e.getUserByUserId());
                    ResponseDTO<CartResponseDTO> responseDTO = new ResponseDTO<>();
                    responseDTO.setResponseCode(0);
                    responseDTO.setData(cartResponseDTO);
                    return responseDTO;
                }
            }
        }
        cart.setUserByUserId(id);
        CartResponseDTO cartResponseDTO = CartMapperDTO.toCartResponseDTOFromCartEntity(repository.save(cart));
        ResponseDTO<CartResponseDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setResponseCode(0);
        responseDTO.setData(cartResponseDTO);
        return responseDTO;
    }
}