package com.taidev198.repository.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface BaseRepository<T> {
    List<T> fetchAllDataWithoutPagination(List<WhereElements> whereElements, Sort sort, String... relationships);

    Page<T> fetchAllDataWithPagination(List<WhereElements> whereElements, Pageable pageable, String... relationships);

    List<T> fetchAllDataWithFirstQuery(
            List<WhereElements> whereElements, String baseQuery, Sort sort, Pageable pageable);
}
