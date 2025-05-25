package org.usm.budgetplanner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.usm.budgetplanner.dto.response.BalanceResponse;
import org.usm.budgetplanner.dto.response.CategoryResponse;
import org.usm.budgetplanner.service.InfoService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        return infoService.getBalance();
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return infoService.getCategories();
    }


}
