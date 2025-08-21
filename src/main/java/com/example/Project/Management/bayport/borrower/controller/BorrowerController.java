package com.example.Project.Management.bayport.borrower.controller;

import com.example.Project.Management.bayport.borrower.entity.Borrower;
import com.example.Project.Management.bayport.borrower.repository.BorrowerRepository;
import com.example.Project.Management.bayport.borrower.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.logging.Logger;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/portal/borrower")

public class BorrowerController {

    @Autowired
    private BorrowerRepository borrowerRepository;


    @Autowired
    private BorrowerService borrowerService;

    Logger logger = Logger.getLogger(BorrowerController.class.getName());

    public BorrowerController(BorrowerService borrowerService, BorrowerRepository borrowerRepository) {
        this.borrowerService = borrowerService;
        this.borrowerRepository = borrowerRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrower> findById(@PathVariable("id") Long borrowerId) {

        Optional<Borrower> borrowerOptional = borrowerService.findById(borrowerId);

        return borrowerOptional.map(borrower -> new ResponseEntity<>(borrower, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{NIN}")
    public ResponseEntity<Borrower> findByNIN(@PathVariable("NIN") String NIN) {

        Optional<Borrower> borrowerOptional = Optional.ofNullable(borrowerService.findByNIN(NIN));


        if (borrowerOptional.isPresent()) {

            return new ResponseEntity<>(borrowerOptional.get(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadBorrowers(@RequestParam("file") MultipartFile file) {
        try {

            borrowerService.saveBorrowers(file);
            logger.info("file request pass complete save process for exel data");
            return ResponseEntity.ok("Borrowers saved successfully");
        }catch (Exception e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
