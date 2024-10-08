package com.fullsnacke.eimsfuhcmbe.controller;

import com.fullsnacke.eimsfuhcmbe.dto.mapper.UserMapper;
import com.fullsnacke.eimsfuhcmbe.dto.request.UserRequestDTO;
import com.fullsnacke.eimsfuhcmbe.dto.response.ApiResponse;
import com.fullsnacke.eimsfuhcmbe.dto.response.UserResponseDTO;
import com.fullsnacke.eimsfuhcmbe.entity.User;
import com.fullsnacke.eimsfuhcmbe.exception.AuthenticationProcessException;
import com.fullsnacke.eimsfuhcmbe.exception.ErrorCode;
import com.fullsnacke.eimsfuhcmbe.exception.repository.user.UserNotFoundException;
import com.fullsnacke.eimsfuhcmbe.repository.UserRepository;
import com.fullsnacke.eimsfuhcmbe.service.UserServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "API for User Controller")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<User> userList = userServiceImpl.getAllUsers();

        if(userList.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            List<UserResponseDTO> userResponseDTOList;
            userResponseDTOList = userList.stream().map(user -> userMapper.toDto(user)).toList();
            return ResponseEntity.ok(userResponseDTOList);
        }
    }

    @PostMapping
    @Operation(summary = "Add a user", description = "Add a new user")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
        User user = userMapper.toEntity(userRequestDTO);
        User addedUser = userServiceImpl.add(user);
        URI uri = URI.create("/users" + user.getFuId());
        UserResponseDTO userResponseDTO = userMapper.toDto(addedUser);
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @PutMapping("/{fuId}")
    @Operation(summary = "Update a user", description = "Update an existing user")
    public ResponseEntity<UserResponseDTO> updateUserByFuId(@RequestBody @Valid UserRequestDTO userRequestDTO){
        User user = userMapper.toEntity(userRequestDTO);
        try{
            User updatedUser = userServiceImpl.updateUser(user);
            UserResponseDTO userResponseDTO = userMapper.toDto(updatedUser);
            return ResponseEntity.ok(userResponseDTO);
        }catch (UserNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{fuId}")
    @Operation(summary = "Get a user by fuId", description = "Retrieve a user by fuId")
    public ResponseEntity<UserResponseDTO> getUserByFuId(@PathVariable("fuId") String fuId){
        User user = userServiceImpl.getUserByFuId(fuId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }else{
            UserResponseDTO userResponseDTO = userMapper.toDto(user);
            return ResponseEntity.ok(userResponseDTO);
        }
    }

    @DeleteMapping("/{fuId}")
    @Operation(summary = "Delete a user by fuId", description = "Delete a user by fuId")
    public ResponseEntity<?> deleteUserByFuId(@PathVariable("fuId") String fuId){
        try{
            userServiceImpl.deleteUser(fuId);
            return ResponseEntity.noContent().build();
        }catch (UserNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserResponseDTO> getUserInfo(@AuthenticationPrincipal
    OAuth2User oAuth2User){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userServiceImpl.getMyInfo(oAuth2User));
    }
}
