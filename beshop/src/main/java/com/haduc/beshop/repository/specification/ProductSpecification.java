package com.haduc.beshop.repository.specification;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.request.user.PriceRangeFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;



public class ProductSpecification implements Specification<Product> {

    private Integer categoryId;

    private Integer supplierId;

    private String text;

    private  List<PriceRangeFilterRequest> priceRanges;

    public ProductSpecification(Integer categoryId, Integer supplierId, String text, List<PriceRangeFilterRequest> priceRanges) {
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.text = text;
        this.priceRanges = priceRanges;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // add other predicates
        predicates.add(criteriaBuilder.equal(root.get("isDelete"), false));
        if(categoryId != null && categoryId!=0){
            predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"), categoryId));
        }
        if(supplierId != null && supplierId!=0){
            predicates.add(criteriaBuilder.equal(root.get("supplier").get("supplierId"), supplierId));
        }
        if (text != null && !text.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("productName"), String.format("%%%s%%", text)));
        }
        //if(text !=null || text.trim().isEmpty()==false){//trim() xoa khoang trang dau va cuoi

        //}
        // tinh gia ban
        Expression<Integer> x=  criteriaBuilder.diff(
                root.get("unitPrice"), criteriaBuilder.quot(
                        criteriaBuilder.prod(root.get("unitPrice"), root.get("discount")).as(Integer.class),
                        criteriaBuilder.literal(Integer.valueOf(100))
                                                            ).as(Integer.class)
                ).as(Integer.class);

        // add price range predicate
        if (priceRanges.size() != 0 && !priceRanges.isEmpty()) {
            Predicate[] pricePredicates = new Predicate[priceRanges.size()];
            for (int i = 0; i < priceRanges.size(); i++) {
                PriceRangeFilterRequest priceRange = priceRanges.get(i);
                Predicate pricePredicate = null;


                if (priceRange.getStartPrice() != null && priceRange.getEndPrice() != null && priceRange.getEndPrice() != 0) {

                    pricePredicate = criteriaBuilder.between(x, priceRange.getStartPrice(), priceRange.getEndPrice());

                } else if (priceRange.getStartPrice() == null && priceRange.getEndPrice() != null && priceRange.getEndPrice() != 0) {

                    pricePredicate = criteriaBuilder.lessThanOrEqualTo(x,priceRange.getEndPrice());

                } else if (priceRange.getStartPrice() != null && ((priceRange.getEndPrice() == null || priceRange.getEndPrice() == 0))) {

                    pricePredicate = criteriaBuilder.greaterThanOrEqualTo(x, priceRange.getStartPrice());
                }
                pricePredicates[i] = pricePredicate;// gan lai phan tu mang, ban dau mang la 0. duyet mang nay dat phan tu mang kia
            }
            predicates.add(criteriaBuilder.or(pricePredicates));// add vo mang dk chung
        }// mot dong or
        // AND tung cac dieu kien
        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));// 0 o day la mang rong và y ns kich thuc mang ms doi tuy vao size cua list
    }
}
