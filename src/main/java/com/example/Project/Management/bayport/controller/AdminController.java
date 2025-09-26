package com.example.Project.Management.bayport.controller;

import com.example.Project.Management.bayport.auth.user.dto.UserDto;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
//@CustomAnnotations.RequireRole(Role.ADMIN)
public class AdminController {
    private final UserService userService;


    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/list")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<?> getUserList() {

        List<UserDto> userDtoArrayList = new ArrayList<>();
        try {
            List<User> users = userService.findAll();

            if (users != null) {
                for (User user : users) {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getFullName());
                    userDto.setEmail(user.getEmail());
                    userDto.setRole(user.getRole().name());
                    userDtoArrayList.add(userDto);
                }
                log.info("success fetch user list");
                return ResponseEntity.ok(userDtoArrayList);
            } else {
                log.info("no user found");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no user found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found: " + e.getMessage());
        }
    }

}