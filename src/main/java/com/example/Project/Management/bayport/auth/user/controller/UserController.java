package com.example.Project.Management.bayport.auth.user.controller;

import com.example.Project.Management.bayport.auth.enumerated.Role;
import com.example.Project.Management.bayport.auth.user.dto.UserDto;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.config.JwtAuthenticationFilter;
import com.example.Project.Management.bayport.util.Response;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping(value="/api/user")
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

//    @GetMapping("/")
//    public ResponseEntity<List<User>> allUsers() {
//        List <User> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }

    @PostMapping("/list")
    public ResponseEntity<?> getUserList(HttpServletRequest request, JwtAuthenticationFilter auth) {

        List<UserDto> userDtoArrayList = new ArrayList<>();
        try {
            List<User> users = userService.findAll();
            if (users != null) {

                for (User user : users) {

                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUserName(user.getUsername());
                    userDto.setEmail(user.getEmail());
                    userDto.setRole(user.getRole().name());
                    userDtoArrayList.add(userDto);

                }
                message = messageSource.getMessage("message.1001", null, currentLocale);
                status = messageSource.getMessage("code.1001", null, currentLocale);
                isError = false;
            } else {
                message = messageSource.getMessage("message.1007", null, currentLocale);
                status = messageSource.getMessage("code.1007", null, currentLocale);
                isError = true;
            }

        } catch (Exception e) {
            message = messageSource.getMessage("message.1004", null, currentLocale);
            status = messageSource.getMessage("code.1004", null, currentLocale);
            isError = true;
        }
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), status, isError, message, userDtoArrayList, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/assign_role")
    public ResponseEntity<?> assignUserRole(@RequestBody UserDto userDto, HttpServletRequest request){

        User assignUser = null;
        try {

            if(userDto!=null){
                Optional<User> user = userService.findByUserId(userDto.getId());

                if(user == null) {

                    user.get().setRole(Role.valueOf(userDto.getRole()));
                    User updatedUser = userService.saveUser(user.get());

                    assignUser = updatedUser;

                    if(assignUser != null) {
                        message = messageSource.getMessage("general.updated.success", new Object[]{"User"}, currentLocale);
                        status = messageSource.getMessage("code.1001",null, currentLocale);
                        isError  = false;

                    } else {
                        message = messageSource.getMessage("general.create.failure", new Object[]{"User "}, currentLocale);
                        status = messageSource.getMessage("code.1002",null, currentLocale);
                        isError  = true;
                    }
                } else {
                    message = messageSource.getMessage("general.create.failure", new Object[]{"User "}, currentLocale);
                    status = messageSource.getMessage("code.1002",null, currentLocale);
                    isError  = true;
                }
                } else {
                    message = messageSource.getMessage("general.current.exists",new Object[]{"User "}, currentLocale);
                    status = messageSource.getMessage("code.1008",null, currentLocale);
                    isError  = true;
                }

        } catch(Exception ex) {
            ex.printStackTrace();
            message = messageSource.getMessage("general.create.failure", new Object[]{"User "}, currentLocale);
            status = messageSource.getMessage("code.1004",null, currentLocale);
            isError = true;
        }

        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,isError,message,assignUser,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
