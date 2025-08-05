package com.example.Project.Management.bayport.project.service;

import com.example.Project.Management.bayport.project.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Project projectSave(Project project);

    List<Project> findAll();

    Optional<Project> findById(Long projectid);

    void deleteProjectById(Long id);
}
