package com.haduc.beshop.util.dto.response.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnChartDataResponse {
    private String key;
    private Long value;
}
