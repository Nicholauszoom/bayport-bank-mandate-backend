package com.dflex.emkopo.app.status.service;

import com.dflex.emkopo.app.status.entity.Status;
import com.dflex.emkopo.app.status.repository.StatusRepository;
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

    @Override
    public Status updateStatus(Long id, Status updateStatus) {
        Status existStatus = statusRepository.findById(id).orElse(null);
        if (existStatus !=null){
            existStatus.setName(updateStatus.getName());
            existStatus.setDescription(updateStatus.getDescription());
            return statusRepository.save(existStatus);
        }

        return null;
    }

}
