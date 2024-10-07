package com.taidev198.repository;

import com.taidev198.model.Constant;
import com.taidev198.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstantsRepository extends JpaRepository<Constant, Integer> {
    @Query("SELECT c.value FROM Constant c WHERE c.type = 'Category'")
    List<String> findAllCategories();

    @Query("SELECT c.value FROM Constant c WHERE c.type = 'Style'")
    List<String> findAllStyles();

    @Query("SELECT c.value FROM Constant c WHERE c.type = 'Material'")
    List<String> findAllMaterials();
    Constant findByValue(String value);
}
