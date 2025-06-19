package org.usm.budgetplanner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usm.budgetplanner.dto.request.TransactionDTO;
import org.usm.budgetplanner.dto.response.TransactionResponse;
import org.usm.budgetplanner.dto.response.TransactionUpdateResponse;
import org.usm.budgetplanner.service.TransactionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/list")
    public Map<LocalDate, List<TransactionResponse>> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("/add")
    public TransactionUpdateResponse addTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.addTransaction(transactionDTO);
    }

    @PutMapping("/{transactionId}")
    public TransactionUpdateResponse updateTransaction(@PathVariable Long transactionId, @RequestBody TransactionDTO transactionDTO) {
        return transactionService.updateTransaction(transactionId, transactionDTO);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransactionById(transactionId);
        return ResponseEntity.noContent().build();
    }


}
