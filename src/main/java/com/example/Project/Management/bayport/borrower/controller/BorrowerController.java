package com.dflex.emkopo.app.borrower.controller;

import com.dflex.emkopo.app.auth.services.AuthService;
import com.dflex.emkopo.app.borrower.entity.Borrower;
import com.dflex.emkopo.app.borrower.repository.BorrowerRepository;
import com.dflex.emkopo.app.borrower.service.BorrowerService;
import com.dflex.emkopo.app.search.SearchBorrower;
import com.dflex.emkopo.app.users.models.User;
import com.dflex.emkopo.borrower.dto.portal.BorrowerDto;
import com.dflex.emkopo.commons.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/portal/borrower")

public class BorrowerController {
    @Autowired
    private AuthService authService;

    @Autowired
    private BorrowerRepository borrowerRepository;


    @Autowired
    private BorrowerService borrowerService;

    Logger logger = Logger.getLogger(BorrowerController.class.getName());

    public BorrowerController(BorrowerService borrowerService, AuthService authService, BorrowerRepository borrowerRepository) {
        this.borrowerService = borrowerService;
        this.authService = authService;
        this.borrowerRepository = borrowerRepository;
    }


    @GetMapping("/search")
    public ResponseEntity<List<Borrower>> filterBorrowers(SearchBorrower filter) {
        List<Borrower> borrowers = borrowerService.getSearch(filter);
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllBorrower() {
        List<BorrowerDto> borrowerDtoList = new ArrayList<>();
        try {
            User user = authService.getUser();
            String fspCode= user.getFsp().getFspCode();
            List<Borrower> borrowers = borrowerService.findByFspCode(fspCode);
            for (Borrower borrower : borrowers) {
                if (borrower != null) {
                    BorrowerDto borrowerDto = getBorrowerDto(borrower);

                    borrowerDtoList.add(borrowerDto);
                }
            }


            Response response = new Response(String.valueOf(Calendar.getInstance().getTime()), "200", false, "Success", borrowerDtoList, null);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            Response errorResponse = new Response(String.valueOf(Calendar.getInstance().getTime()), "500", true, "Internal Server Error", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private static BorrowerDto getBorrowerDto(Borrower borrower) {
        BorrowerDto borrowerDto = new BorrowerDto();
        borrowerDto.setId(borrower.getId());
        borrowerDto.setCheckNumber(borrower.getCheckNumber());
        borrowerDto.setBankAccountNumber(borrower.getBankAccountNumber());
        borrowerDto.setEmailAddress(borrower.getEmailAddress());
        borrowerDto.setNIN(borrower.getNIN());
        borrowerDto.setFirstName(borrower.getFirstName());
        borrowerDto.setMiddleName(borrower.getMiddleName());
        borrowerDto.setLastName(borrower.getLastName());
        borrowerDto.setPhysicalAddress(borrower.getPhysicalAddress());
        borrowerDto.setNearestBranchName(borrower.getNearestBranchName());
        return borrowerDto;
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
