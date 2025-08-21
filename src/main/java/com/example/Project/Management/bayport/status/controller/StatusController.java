package com.example.Project.Management.bayport.status.controller;

import com.example.Project.Management.bayport.status.entity.Status;
import com.example.Project.Management.bayport.status.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/Status")
public class StatusController {


    private StatusService statusService;

    @PostMapping("/saveStatus")
    public ResponseEntity<String> saveStatus(@RequestBody Status status) {
        statusService.saveStatus(status);
        return ResponseEntity.ok("Status saved successfully");
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<Status>> getAllStatuses() {
        List<Status> statuses = statusService.findAll();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable Long id) {
        Optional<Status> status = statusService.findById(id);
        if (status.isPresent()) {
            return ResponseEntity.ok(status.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.ok("Status deleted successfully");
    }

}
