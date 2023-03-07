package com.haduc.beshop.controller;


import com.haduc.beshop.service.IStatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistical")
public class StatisticalController {

    @Autowired
    private IStatisticalService iStatisticalService;

    @GetMapping("/product-revenue-chart")
    public ResponseEntity<?> getFacultyUserPieChartData() {
        return ResponseEntity.status(HttpStatus.OK).body(this.iStatisticalService.getFacultyUserPieChartData());
    }
}
