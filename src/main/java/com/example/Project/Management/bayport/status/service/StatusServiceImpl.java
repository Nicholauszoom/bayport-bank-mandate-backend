package com.example.Project.Management.bayport.status.service;

import com.example.Project.Management.bayport.status.entity.Status;
import com.example.Project.Management.bayport.status.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StatusServiceImpl implements StatusService{


    StatusRepository statusRepository;
    @Override
    public Status saveStatus(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    @Override
    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }

}
