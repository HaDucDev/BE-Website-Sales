package com.haduc.beshop.util.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsPaginationResponse {

    List<GetProductResponse> content;// noi dung
    int number;// chi so cua trang hien tai, bat dau tu 0
    int size;// trung binh 1 trang co bao nhieu element
    int totalElements;// tong so element chinh la length cua content
    int totalPages;// tong so trang tinh tu 1
}
