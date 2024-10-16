package com.taidev198.repository.customization;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taidev198.model.ShoppingCart;
import com.taidev198.repository.base.BaseRepository;

public interface ShoppingCartsCustomRepository
        extends CustomRepository<ShoppingCart, Integer, BaseRepository<ShoppingCart>> {
    Page<ShoppingCart> findAllByAccountId(Integer accountId, Pageable pageable);

    List<ShoppingCart> findAllByAccountId(Integer accountId);
}
