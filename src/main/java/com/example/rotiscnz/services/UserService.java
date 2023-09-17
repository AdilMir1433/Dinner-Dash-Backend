package com.example.rotiscnz.services;

import com.example.rotiscnz.dtos.ResponseDTO;
import com.example.rotiscnz.dtos.userDTOs.UserResponseDTO;
import com.example.rotiscnz.dtos.userDTOs.UserSignUpDTO;
import com.example.rotiscnz.entities.UserEntity;
import com.example.rotiscnz.enums.UserRole;
import com.example.rotiscnz.mappers.UserMapper;
import com.example.rotiscnz.repositories.UserRepository;
import com.example.rotiscnz.security.JWTUtility;
import com.example.rotiscnz.utility.SessionData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtility jwtUtility;
    private final UserMapper userMapper;
    private final SessionData sessionData;

    /**
     * Method to load user details by name
     *
     * @param username: username of the user to be loaded from the database (email) (String)
     * @return User: user details (User)
     */
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Method to login user into app
     * @param email: email of the user entered by user (String)
     * @param password: password entered by the user (String)
     * @return token: generated token is returned (String)
     */
    public ResponseDTO<UserResponseDTO> login(String email, String password) {
        UserEntity user = loadUserByUsername(email);
        UserResponseDTO userResponseDTO = userMapper.toUSerResponseDTOFromUserEntity(user);
        sessionData.setUser(userResponseDTO);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return generateFailureResponse("User Name or Password Error");
        }
        String token = jwtUtility.generateToken(user);
        sessionData.setToken(token);
        return generateSuccessResponseForLogin(token,userResponseDTO);
    }

    /**
     * Method to load user details by email and password
     * @param email: email of the user entered by user (String)
     * @param password: password entered by the user (String)
     * @return User: User details are returned (UserResponseDTO)
     */
    public ResponseDTO<UserResponseDTO> getProfile(String email, String password) {
        UserEntity user = loadUserByUsername(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setResponseCode(-1);
            responseDTO.setErrorMessage("User Name or Password Error");
            return responseDTO;
        }
        ResponseDTO<UserResponseDTO> userResponseDTOResponseDTO= new ResponseDTO<>();
        UserResponseDTO userResponseDTO = userMapper.toUSerResponseDTOFromUserEntity(user);
        userResponseDTOResponseDTO.setData(userResponseDTO);
        userResponseDTOResponseDTO.setResponseCode(0);
        return userResponseDTOResponseDTO;
    }
    /**
     * Method to save user to database
     * @param user: User Data (UserResponseDTO)
     * @return User: User details are returned (UserResponseDTO)
     */
    public ResponseDTO<UserResponseDTO> signUp(UserSignUpDTO user){
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        UserEntity userEntity = userMapper.toUserEntityFromUserSignUpDTO(user);
        userEntity.setRole(UserRole.CUSTOMER);
        UserEntity createdUser = userRepository.save(userEntity);
        ResponseDTO<UserResponseDTO> userResponseDTOResponseDTO= new ResponseDTO<>();
        UserResponseDTO userResponseDTO = userMapper.toUSerResponseDTOFromUserEntity(createdUser);
        userResponseDTOResponseDTO.setData(userResponseDTO);
        userResponseDTOResponseDTO.setResponseCode(0);
        return generateSuccessResponseForUser(userResponseDTO);

    }
}
