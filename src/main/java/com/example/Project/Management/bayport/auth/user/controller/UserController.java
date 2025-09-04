package com.example.Project.Management.bayport.auth.user.controller;

import com.example.Project.Management.bayport.auth.enumerated.Role;
import com.example.Project.Management.bayport.auth.user.dto.UserDto;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    String message = null;
    String status = null;
    Boolean isError = false;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        Optional<User> userOpt = userService.findByUserId(id);
        log.info("userOpt = {}", userOpt.get());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOpt.get();
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setPassword(user.getPassword());

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/assign_role")
    public ResponseEntity<?> assignUserRole(@RequestBody UserDto userDto, HttpServletRequest request){

        User assignUser = null;
        try {

            if(userDto!=null){
                log.info("userDto is null");
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userDto is null");
            }
            Optional<User> user = userService.findByUserId(userDto.getId());

            if(user != null) {

                user.get().setRole(Role.valueOf(userDto.getRole()));
                User updatedUser = userService.saveUser(user.get());

                assignUser = updatedUser;

                if(assignUser != null) {
                    log.info("success assigned role, user ID: {}", assignUser.getId());
                    ResponseEntity.status(HttpStatus.OK).body(assignUser);

                }
            } else {
                log.info("no user to assigned role, user ID: {}", userDto.getId());
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("no user to assigned role");
            }


        } catch(Exception e) {
            log.info("fail to update user", e.getMessage());
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("fail to update user");
        }

        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,isError,message,assignUser,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/detail")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();

        log.info("username : {}", username);

        Optional<User> userOpt = userService.findByEmail(username);

        if(userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();

        UserDto userDtoDetails = UserDto.builder()
                .id(user.getId())
                .role(user.getRole().name())
                .email(user.getEmail())
                .username(user.getFullName())
                .build();

        log.info("userDtoDetails : {}", userDtoDetails);

        return ResponseEntity.ok(userDtoDetails);
    }

    @PostMapping("/{id}/delete")
    public void deleteCharge(
            @PathVariable("id") long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateUserDetails(
            @PathVariable("id") long id,
            Authentication authentication,
            @RequestBody UserDto userDto) {

        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            String username = authentication.getName();
            log.info("username : {}", username);

            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOpt.get();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setRole(Role.valueOf(userDto.getRole()));

            userService.saveUser(user);
            log.info("updated user details: {}", user);

            return ResponseEntity.ok("USER INFORMATION UPDATED SUCCESSFULLY");
        } catch (Exception e) {
            log.error("Update failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("USER INFORMATION FAILED TO UPDATE");
        }
    }


}