package com.haduc.beshop.util.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceRangeFilterRequest {

    private Integer startPrice;
    private Integer endPrice;


    @Override
    public String toString() {
        return "Nume{" +
                "startPrice=" + startPrice +
                ", endPrice=" + endPrice +
                '}';
    }
}
