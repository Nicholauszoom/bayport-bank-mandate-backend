package com.example.Project.Management.bayport.project.service;

import com.example.Project.Management.bayport.project.entity.Project;
import com.example.Project.Management.bayport.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Project projectSave(Project project) {
        projectRepository.save(project);

        return project;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> findById(Long projectid) {
        return projectRepository.findById(projectid);
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }
}
