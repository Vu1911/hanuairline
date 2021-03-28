package com.se2.hanuairline.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtils {
    public static Pageable pagingSort (int page, int size, String[] sort){
        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(Sort.Direction.valueOf(sort[1].toUpperCase()), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(Sort.Direction.valueOf(sort[1].toUpperCase()), sort[0]));
        }

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        return pagingSort;
    }
}
