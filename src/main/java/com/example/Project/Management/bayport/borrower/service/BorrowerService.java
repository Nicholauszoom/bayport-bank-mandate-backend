package com.example.Project.Management.bayport.borrower.service;

import com.example.Project.Management.bayport.borrower.entity.Borrower;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BorrowerService {


    Borrower findByNIN(String NIN);

    List<Borrower> getAllBorrower();
    
    Optional<Borrower> findById(Long id);


    ResponseEntity<String> accountValidation(String message)  throws Exception;

    void saveBorrowers(MultipartFile file) throws Exception;

    List<Borrower> findByFspCode(String fspCode);
}
