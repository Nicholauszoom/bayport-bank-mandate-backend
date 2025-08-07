package com.example.Project.Management.bayport.auth.user.controller;

import com.example.Project.Management.bayport.auth.enumerated.Role;
import com.example.Project.Management.bayport.auth.user.dto.UserDto;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.config.JwtAuthenticationFilter;
import com.example.Project.Management.bayport.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    String message = null;
    String status = null;
    Boolean isError = false;
    Locale currentLocale = LocaleContextHolder.getLocale();

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
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

}
