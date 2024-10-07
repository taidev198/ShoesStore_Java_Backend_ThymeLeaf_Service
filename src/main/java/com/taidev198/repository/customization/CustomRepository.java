package com.taidev198.repository.customization;

import com.taidev198.repository.base.BaseRepository;
import com.taidev198.repository.base.WhereClauseType;
import com.taidev198.repository.base.WhereElements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CustomRepository<T, ID, K extends BaseRepository<T>> {
    K getRepository();

    default Optional<T> findByIdWithRelationship(ID id, String... relationships) {
        return getRepository().fetchAllDataWithoutPagination(
                List.of(
                    new WhereElements("id", id, WhereClauseType.EQUAL)
                ),
                null,
                relationships)
            .stream().findFirst();
    }

    default List<T> findAllWithRelationship(String... relationships) {
        return getRepository().fetchAllDataWithoutPagination(null, null, relationships);
    }

    default Page<T> findAllWithRelationship(Pageable pageable, String... relationships) {
        return getRepository().fetchAllDataWithPagination(null, pageable, relationships);
    }

    default List<T> findAllWithRelationship(Sort sort, String... relationships) {
        return getRepository().fetchAllDataWithoutPagination(null, sort, relationships);
    }
}
