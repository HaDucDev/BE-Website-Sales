package com.haduc.beshop.util.dto.response.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponse {

    private Integer categoryId;

    private String categoryName;

    private boolean isDelete;

}
