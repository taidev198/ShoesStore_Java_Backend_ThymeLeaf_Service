package com.taidev198.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.taidev198.model.ProductDetail;
import com.taidev198.service.ProductService;
import com.taidev198.util.PaginationUtil;

@Controller
public class SearchController {

    private final ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search-results")
    public ModelAndView searchPage(
            @RequestParam(name = "key", required = false, defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        ModelAndView modelAndView = new ModelAndView("screens/products/search");

        int size = 18;

        Page<ProductDetail> productPage = productService.getProductsByName(query, page, size);
        List<ProductDetail> productsDetailList = productPage.getContent();

        int totalItems = (int) productPage.getTotalElements();
        PaginationUtil paginationHelper = new PaginationUtil(totalItems, size, page, 5, buildQueryString(query));

        modelAndView.addObject("paginationHelper", paginationHelper);
        modelAndView.addObject("result_count", productPage.getTotalElements());
        modelAndView.addObject("query", query);
        modelAndView.addObject("page", page);
        modelAndView.addObject("totalPages", productPage.getTotalPages());
        modelAndView.addObject("products", productsDetailList);

        return modelAndView;
    }

    private String buildQueryString(String query) {
        return UriComponentsBuilder.fromUriString("/search-results")
                .queryParam("key", query)
                .toUriString();
    }
}
