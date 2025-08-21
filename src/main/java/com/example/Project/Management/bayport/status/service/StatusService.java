package com.example.Project.Management.bayport.status.service;

import com.example.Project.Management.bayport.status.entity.Status;

import java.util.List;
import java.util.Optional;

public interface StatusService {


     Status saveStatus(Status status);

     List<Status> findAll();

     Optional<Status> findById(Long id);

     void deleteStatus(Long id);


}
