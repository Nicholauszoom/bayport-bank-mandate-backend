package com.dflex.emkopo.app.borrower.repository;

import com.dflex.emkopo.app.borrower.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower,Long> {
    Borrower findByNIN(String nin);

    Borrower findByBankAccountNumber(String bankAccountNumber);

    Borrower findByCheckNumber(String checkNumber);

    List<Borrower> findByFspCode(String fspCode);

    @Query("SELECT b FROM Borrower b WHERE " +
            "(:firstName IS NULL OR b.firstName LIKE %:firstName%) AND " +
            "(:middleName IS NULL OR b.middleName LIKE %:middleName%) AND " +
            "(:lastName IS NULL OR b.lastName LIKE %:lastName%) AND " +
            "(:NIN IS NULL OR b.NIN = :NIN) AND " +
            "(:bankAccountNumber IS NULL OR b.bankAccountNumber = :bankAccountNumber)")
    List<Borrower> findBorrowersByFilter(@Param("firstName") String firstName,
                                         @Param("middleName") String middleName,
                                         @Param("lastName") String lastName,
                                         @Param("NIN") String NIN,
                                         @Param("bankAccountNumber") String bankAccountNumber);

}
