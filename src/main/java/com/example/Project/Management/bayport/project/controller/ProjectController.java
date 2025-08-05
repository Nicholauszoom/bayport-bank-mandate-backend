package com.example.Project.Management.bayport.project.controller;

import com.example.Project.Management.bayport.annotation.CustomAnnotations;
import com.example.Project.Management.bayport.auth.enumerated.Permission;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.project.dto.ProjectDto;
import com.example.Project.Management.bayport.project.entity.Project;
import com.example.Project.Management.bayport.project.service.ProjectService;
import com.example.Project.Management.bayport.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping("/api/project")
@Validated
public class ProjectController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    String status = "";
    String message = "";
    Boolean error = false;

    Locale currentLocale = LocaleContextHolder.getLocale();

    @PostMapping("/create")
    @CustomAnnotations.RequirePermission(Permission.CREATE_PROJECT)
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDto projectDto,
                                          HttpServletRequest request) {

        System.out.println(" data************" + projectDto);

        Project project = new Project();
        try {
            long userId =projectDto.getUserId();
            Optional<User> user = userService.findByUserId(userId);

            if(projectDto != null){

                if (user.get()!=null){

                System.out.println(" data part2************" + projectDto);
                project.setProjectName(projectDto.getProjectName());
                project.setProjectDescription(projectDto.getProjectDescription());
                project.setStartDate(projectDto.getStartDate());
                project.setEndDate(projectDto.getEndDate());
                project.setUser(user.get());
                projectService.projectSave(project);

                message = messageSource.getMessage("message.1001", null, currentLocale);
                status = messageSource.getMessage("code.1001", null, currentLocale);
                error = false;

            } else {
                message = messageSource.getMessage("message.1003",null, currentLocale);
                status = messageSource.getMessage("code.1003", null, currentLocale);
                error = true;
            }
        } else {
            message = messageSource.getMessage("message.1005",null, currentLocale);
            status = messageSource.getMessage("code.1005", null, currentLocale);
            error = true;
        }
        } catch (Exception ex){
            ex.printStackTrace();
            message = messageSource.getMessage("message.1004",null, currentLocale);
            status = messageSource.getMessage("code.1004",null, currentLocale);
            error  = true;
        }

//        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,error,message,project,request.getRequestURI());
        Response response = new Response(
                String.valueOf(Calendar.getInstance().getTime()),
                "200 OK",
                false,
                "Project fetched successfully",
                project,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/summary/projectid/{projectid}")
    public ResponseEntity<?> getProjectSummary(@PathVariable("projectid") Long projectid, HttpServletRequest request) {
        //summary of project
        ProjectDto projectDetail = new ProjectDto();
        String message = "";
        String status = "";
        boolean error = false;
        Locale currentLocale = request.getLocale();

        try {
            if (projectid != null) {
                Optional<Project> project = projectService.findById(projectid);

                if (project.isPresent()) {
                    Project foundProject = project.get();

                    // Populate projectDetail with values
                    projectDetail.setId(foundProject.getId());
                    projectDetail.setProjectName(foundProject.getProjectName());
                    projectDetail.setProjectDescription(foundProject.getProjectDescription());
                    projectDetail.setStartDate(foundProject.getStartDate());
                    projectDetail.setEndDate(foundProject.getEndDate());
                    projectDetail.setUserId(foundProject.getUser().getId());
                    projectDetail.setUserFullName(foundProject.getUser().getUsername());

                    message = messageSource.getMessage("message.1001", null, currentLocale);
                    status = messageSource.getMessage("code.1001", null, currentLocale);
                    error = false;
                } else {
                    message = messageSource.getMessage("message.1007", null, currentLocale);
                    status = messageSource.getMessage("code.1007", null, currentLocale);
                    error = true;
                }
            } else {
                message = messageSource.getMessage("message.1005", null, currentLocale);
                status = messageSource.getMessage("code.1005", null, currentLocale);
                error = true;
            }
        } catch (Exception e) {
            message = messageSource.getMessage("message.1004", null, currentLocale);
            status = messageSource.getMessage("code.1004", null, currentLocale);
            error = true;
        }

        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), status, error, message, projectDetail, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDto projectDto ,
                                              HttpServletRequest request) {

        ProjectDto newProject = null;

        try {
            Optional<Project> project = projectService.findById(projectDto.getId());
            Optional<User> user = userService.findByUserId(projectDto.getUserId());
            if(project.isPresent()){
                project.get().setProjectName(projectDto.getProjectName());
                project.get().setProjectDescription(projectDto.getProjectDescription());
                project.get().setUser(user.get());
                project.get().setStartDate(projectDto.getStartDate());
                project.get().setEndDate(projectDto.getEndDate());

                Project updatedProject = projectService.projectSave(project.get());

                if (updatedProject != null) {

                    message = messageSource.getMessage("general.update.success", new Object[] {"Project"},currentLocale);
                    status = messageSource.getMessage("code.1001", null, currentLocale);
                    error = false;
                } else {
                    message = messageSource.getMessage("general.update.failure", new Object[] {"Project"},currentLocale);
                    status = messageSource.getMessage("code.1002", null, currentLocale);
                    error = true;
                }
            } else {
                message = messageSource.getMessage("message.1005",null, currentLocale);
                status = messageSource.getMessage("code.1005", null, currentLocale);
                error = true;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            message = messageSource.getMessage("message.1004",null, currentLocale);
            status = messageSource.getMessage("code.1004",null, currentLocale);
            error  = true;
        }
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,error,message,newProject,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProject(@RequestBody ProjectDto projectDto ,
                                           HttpServletRequest request) {

        ProjectDto newProject = null;

        try {
            Optional<Project> project = projectService.findById(projectDto.getId());
            Optional<User> user = userService.findByUserId(projectDto.getUserId());

            if(project.isPresent()){

                projectService.deleteProjectById(projectDto.getId());

                    message = messageSource.getMessage("general.update.success", new Object[] {"Project"},currentLocale);
                    status = messageSource.getMessage("code.1001", null, currentLocale);
                    error = false;

            } else {
                message = messageSource.getMessage("general.update.failure",null, currentLocale);
                status = messageSource.getMessage("code.1005", null, currentLocale);
                error = true;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            message = messageSource.getMessage("message.1004",null, currentLocale);
            status = messageSource.getMessage("code.1004",null, currentLocale);
            error  = true;
        }
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,error,message,newProject,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
