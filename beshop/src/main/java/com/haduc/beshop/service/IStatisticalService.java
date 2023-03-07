package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.response.admin.ColumnChartDataResponse;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IStatisticalService {

    List<ColumnChartDataResponse> getRevenueStatisticsData();

}

