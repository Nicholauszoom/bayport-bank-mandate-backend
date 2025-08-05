package com.example.Project.Management.bayport.task.controller;

import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.task.dto.TaskDto;
import com.example.Project.Management.bayport.task.entity.Task;
import com.example.Project.Management.bayport.common.entity.Status;
import com.example.Project.Management.bayport.task.service.TaskService;
import com.example.Project.Management.bayport.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    String status = "";
    String message = "";
    Boolean error = false;

    Locale currentLocale = LocaleContextHolder.getLocale();


    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto,
                                           HttpServletRequest request) {

        System.out.println(" data************" + taskDto);

        Task task = new Task();
        try {
            long userId =taskDto.getUserId();
            Optional<User> user = userService.findByUserId(userId);

            if(taskDto != null){

                if (user.get()!=null){

                    System.out.println(" data part2************" + taskDto);
                    task.setTaskName(taskDto.getTaskName());
                    task.setTaskDescription(taskDto.getTaskDescription());
                    task.setStartDate(taskDto.getStartDate());
                    task.setEndDate(taskDto.getEndDate());
                    task.setUser(user.get());
                    task.setTaskStatus(Status.valueOf(taskDto.getStatus()));
                    taskService.taskSave(task);

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

        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,error,message,task,request.getRequestURI());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/list/projectid/{projectid}")
    public ResponseEntity<?> getTaskListByProjectId(@PathVariable("projectid") Long projectId,HttpServletRequest request) {

        List<TaskDto> taskDtoArrayList = new ArrayList<>();
        try {
            List<Task> tasks = taskService.findAllByProjectId(projectId);
            if (tasks != null) {

                for (Task task : tasks) {

                    TaskDto taskDto = new TaskDto();
                    taskDto.setId(task.getId());
                    taskDto.setTaskName(task.getTaskName());
                    taskDto.setTaskDescription(task.getTaskDescription());
                    taskDto.setStartDate(task.getStartDate());
                    taskDto.setEndDate(task.getEndDate());
                    taskDto.setStatus(taskDto.getStatus());
                    taskDtoArrayList.add(taskDto);

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
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), status, error, message, taskDtoArrayList, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/summary/taskid/{taskid}")
    public ResponseEntity<?> getTaskSummary(@PathVariable("taskid") Long taskid, HttpServletRequest request) {
        //summary
        TaskDto taskDetail = new TaskDto();
        String message = "";
        String status = "";
        boolean error = false;
        Locale currentLocale = request.getLocale();

        try {
            if (taskid != null) {
                Optional<Task> task = taskService.findById(taskid);

                if (task.isPresent()) {
                    Task foundTask = task.get();

                    // Populate  with values
                    taskDetail.setId(foundTask.getId());
                    taskDetail.setTaskName(foundTask.getTaskName());
                    taskDetail.setTaskDescription(foundTask.getTaskDescription());
                    taskDetail.setStartDate(foundTask.getStartDate());
                    taskDetail.setEndDate(foundTask.getEndDate());
                    taskDetail.setUserId(foundTask.getUser().getId());
                    taskDetail.setUsername(foundTask.getUser().getUsername());

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

        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), status, error, message, taskDetail, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto ,
                                           HttpServletRequest request) {

        TaskDto newTask = null;

        try {
            Optional<Task> task = taskService.findById(taskDto.getId());
            Optional<User> user = userService.findByUserId(taskDto.getUserId());
            if(task.isPresent()){
                task.get().setTaskName(taskDto.getTaskName());
                task.get().setTaskDescription(taskDto.getTaskDescription());
                task.get().setUser(user.get());
                task.get().setStartDate(taskDto.getStartDate());
                task.get().setEndDate(taskDto.getEndDate());
                task.get().setTaskStatus(Status.valueOf(taskDto.getStatus()));
                Task updatedTask = taskService.taskSave(task.get());

                if (updatedTask != null) {

                    message = messageSource.getMessage("general.update.success", new Object[] {"Task"},currentLocale);
                    status = messageSource.getMessage("code.1001", null, currentLocale);
                    error = false;
                } else {
                    message = messageSource.getMessage("general.update.failure", new Object[] {"Task"},currentLocale);
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
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,error,message,newTask,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestBody TaskDto taskDto ,
                                           HttpServletRequest request) {
        TaskDto newTask = null;
        try {
            Optional<Task> task = taskService.findById(taskDto.getId());
            Optional<User> user = userService.findByUserId(taskDto.getUserId());

            if(task.isPresent()){

                taskService.deleteTaskById(taskDto.getId());

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
        Response response = new Response(String.valueOf(Calendar.getInstance().getTime()),status,error,message,newTask,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
