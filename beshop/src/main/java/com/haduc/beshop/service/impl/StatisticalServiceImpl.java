package com.haduc.beshop.service.impl;

import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.service.IStatisticalService;
import com.haduc.beshop.util.dto.response.admin.ColumnChartDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticalServiceImpl implements IStatisticalService {

    @Autowired
    private IProductRepository iProductRepository;
    @Override
    public List<ColumnChartDataResponse> getRevenueStatisticsData() {
        return this.iProductRepository.getRevenueStatistics();
    }
}
