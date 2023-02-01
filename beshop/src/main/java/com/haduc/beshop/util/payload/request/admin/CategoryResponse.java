package com.haduc.beshop.util.payload.request.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Integer categoryId;

    private String categoryName;

    private boolean isDelete;

}
