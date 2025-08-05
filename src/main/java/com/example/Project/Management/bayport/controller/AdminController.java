package com.example.Project.Management.bayport.controller;

import com.example.Project.Management.bayport.annotation.CustomAnnotations;
import com.example.Project.Management.bayport.auth.enumerated.Permission;
import com.example.Project.Management.bayport.auth.enumerated.Role;
import com.example.Project.Management.bayport.auth.user.dto.UserDto;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.config.JwtAuthenticationFilter;
import com.example.Project.Management.bayport.project.dto.ProjectDto;
import com.example.Project.Management.bayport.project.entity.Project;
import com.example.Project.Management.bayport.project.service.ProjectService;
import com.example.Project.Management.bayport.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/admin")
//@CustomAnnotations.RequireRole(Role.ADMIN)
public class AdminController {
    private final UserService userService;

    private final ProjectService projectService;

    private final MessageSource messageSource;

    String status = "";
    String message = "";
    Boolean error = false;

    Locale currentLocale = LocaleContextHolder.getLocale();

    public AdminController(UserService userService, ProjectService projectService, MessageSource messageSource) {
        this.userService = userService;
        this.projectService = projectService;
        this.messageSource = messageSource;
    }

    @GetMapping("/user/list")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<?> getUserList(HttpServletRequest request, JwtAuthenticationFilter auth) {

        List<UserDto> userDtoArrayList = new ArrayList<>();
        try {
            List<User> users = userService.findAll();
            System.out.println("Users" + users);
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
                error = false;
            } else {
                message = messageSource.getMessage("message.1007", null, currentLocale);
                status = messageSource.getMessage("code.1007", null, currentLocale);
                error = true;
            }

        } catch (Exception e) {
            message = messageSource.getMessage("message.1004", null, currentLocale);
            status = messageSource.getMessage("code.1004", null, currentLocale);
            error = true;
        }
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), status, error, message, userDtoArrayList, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/project/list")
    @CustomAnnotations.RequirePermission(Permission.VIEW_ALL_PROJECTS)
    public ResponseEntity<?> getProjectList(HttpServletRequest request) {

        List<ProjectDto> projectDtoArrayList = new ArrayList<>();
        try {
            List<Project> projects = projectService.findAll();
            if (projects != null) {

                for (Project project : projects) {

                    ProjectDto projectDto = new ProjectDto();
                    projectDto.setId(project.getId());
                    projectDto.setProjectName(project.getProjectName());
                    projectDto.setProjectDescription(projectDto.getProjectDescription());
                    projectDto.setStartDate(project.getStartDate());
                    projectDto.setEndDate(project.getEndDate());
                    projectDtoArrayList.add(projectDto);

                }
                message = messageSource.getMessage("message.1001", null, currentLocale);
                status = messageSource.getMessage("code.1001", null, currentLocale);
                error = false;
            } else {
                message = messageSource.getMessage("message.1007", null, currentLocale);
                status = messageSource.getMessage("code.1007", null, currentLocale);
                error = true;
            }

        } catch (Exception e) {
            message = messageSource.getMessage("message.1004", null, currentLocale);
            status = messageSource.getMessage("code.1004", null, currentLocale);
            error = true;
        }
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), status, error, message, projectDtoArrayList, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
