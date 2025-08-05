package com.example.Project.Management.bayport.task.service;

import com.example.Project.Management.bayport.task.entity.Task;
import com.example.Project.Management.bayport.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task taskSave(Task task) {
        taskRepository.save(task);
        return task;
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }

    @Override
    public Optional<Task> findById(Long taskid) {
        return taskRepository.findById(taskid);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }


}
