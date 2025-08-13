package com.dflex.emkopo.app.status.service;

import com.dflex.emkopo.app.status.entity.Status;
import java.util.List;
import java.util.Optional;

public interface StatusService {


     Status saveStatus(Status status);

     List<Status> findAll();

     Optional<Status> findById(Long id);

     void deleteStatus(Long id);

     Status updateStatus(Long id, Status updateStatus);


}
