package org.usm.budgetplanner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.usm.budgetplanner.dto.request.TransactionDTO;
import org.usm.budgetplanner.dto.response.BalanceUpdateResponse;
import org.usm.budgetplanner.dto.response.TransactionResponse;
import org.usm.budgetplanner.service.TransactionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/list")
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("/add")
    public BalanceUpdateResponse addTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.addTransaction(transactionDTO);
    }


}
