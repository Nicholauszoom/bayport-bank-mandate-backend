package com.example.Project.Management.bayport.controller;

import com.example.Project.Management.bayport.auth.user.dto.UserDto;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.config.JwtAuthenticationFilter;
import com.example.Project.Management.bayport.project.dto.ProjectDto;
import com.example.Project.Management.bayport.project.entity.Project;
import com.example.Project.Management.bayport.project.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
//@CustomAnnotations.RequireRole(Role.ADMIN)
public class AdminController {
    private final UserService userService;

    private final ProjectService projectService;

    public AdminController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("/user/list")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
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

    @PostMapping("/project/list")
//    @CustomAnnotations.RequirePermission(Permission.VIEW_ALL_PROJECTS)
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
                log.info("success fetch projects");
                return ResponseEntity.ok(projects);
            } else {
                log.info("no project found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no project found");
            }

        } catch (Exception e) {
            log.info("fail fetch projects");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("fail fetch projects" + e.getMessage());
        }
    }
}