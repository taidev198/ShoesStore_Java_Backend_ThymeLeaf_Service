package com.taidev198.controller.seller;

import com.taidev198.annotation.PreAuthorizeAllWithoutCustomer;
import com.taidev198.bean.*;
import com.taidev198.model.ProductDetail;
import com.taidev198.service.ConstantService;
import com.taidev198.service.FilterService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taidev198.model.ProductImage;
import com.taidev198.service.ProductService;
import com.taidev198.util.exception.BadRequestException;
import com.taidev198.util.exception.NotFoundObjectException;
import com.taidev198.util.util.PaginationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller("sellerProductsController")
@PreAuthorizeAllWithoutCustomer
@RequestMapping("/seller/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private FilterService filterService;

    @GetMapping("/{id}")
    public String showProductDetailSeller(@PathVariable("id") Integer id, Model model) {
        try {
            ProductDetailInfoSeller productInfo = productService.getProductDetailByIdForManager(id);
            List<ProductDetailColors> productDetailColors = productService.getProductDetailColors(id);
            model.addAttribute("currentPage", "product-management");

            model.addAttribute("productInfo", productInfo);
            model.addAttribute("productDetailColors", productDetailColors);
            return "screens/seller/products/show";
        } catch (NotFoundObjectException ex) {
            model.addAttribute("toastMessages", new ToastMessage("error", ex.getMessage()));
            return "screens/seller/products/index";
        }
    }

    @GetMapping("/{id}/edit")
    public String editProductDetail(@PathVariable("id") Integer id, Model model) {
        try {
            ProductDetailEdit productInfo = productService.getProductDetailEditById(id);
            List<ProductDetailColors> productDetailColors = productService.getProductDetailColors(id);
            List<ProductImage> productImages = productService.getProductImagesByProductDetailId(id);

            model.addAttribute("currentPage", "product-management");
            model.addAttribute("productInfo", productInfo);
            model.addAttribute("productDetailColors", productDetailColors);
            model.addAttribute("productImages", productImages);

            model.addAttribute("availableCategories", productService.findAllCategories());
            model.addAttribute("availableStyles", productService.findAllStyles());
            model.addAttribute("availableMaterials", productService.findAllMaterials());

            return "screens/seller/products/edit";
        } catch (NotFoundObjectException ex) {
            model.addAttribute("toastMessages", new ToastMessage("error", ex.getMessage()));
            return "screens/products/index";
        }
    }

    @GetMapping
    public String getProductManagement(
            @RequestParam(name = "filterStyle", required = false, defaultValue = "-1") String filterStyle,
            @RequestParam(name = "filterMaterial", required = false, defaultValue = "-1") String filterMaterial,
            @RequestParam(name = "queryProduct", required = false, defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size,
            Model model) {

        List<Integer> filterStyles = new ArrayList<>();
        List<Integer> filterMaterials = new ArrayList<>();

        if (!filterStyle.isEmpty() && Integer.parseInt(filterStyle) != -1) {
            filterStyles.add(Integer.parseInt(filterStyle));
        }
        if (!filterMaterial.isEmpty() && Integer.parseInt(filterMaterial) != -1) {
            filterMaterials.add(Integer.parseInt(filterMaterial));
        }

        Page<ProductFilterInfo> productFilterPage = filterService.getProductFilterInfos(
                filterStyles,
                new ArrayList<>(),
                filterMaterials,
                new ArrayList<>(),
                1,
                query,
                PageRequest.of(page - 1, size));

        PaginationUtil paginationUtil = new PaginationUtil(
                (int) productFilterPage.getTotalElements(),
                size,
                page,
                5, // Number of pages to show around the current page
                buildQueryString(filterStyle, filterMaterial, query));

        model.addAttribute("totalProducts", (int) productFilterPage.getTotalElements());
        model.addAttribute("productDetails", productFilterPage.getContent());
        model.addAttribute("pagination", paginationUtil);
        model.addAttribute("currentPagination", page);

        model.addAttribute("filterStyle", filterStyle);
        model.addAttribute("filterMaterial", filterMaterial);
        model.addAttribute("queryProduct", query);

        model.addAttribute("styles", constantService.getListConstantsByType("Style"));
        model.addAttribute("materials", constantService.getListConstantsByType("Material"));

        model.addAttribute("currentPage", "product-management");
        return "screens/seller/products/index";
    }

    private String buildQueryString(String filterStyle, String filterMaterial, String query) {
        return UriComponentsBuilder.fromUriString("/seller/products")
                .queryParam("filterStyle", filterStyle.equals("-1") ? null : filterStyle)
                .queryParam("filterMaterial", filterMaterial.equals("-1") ? null : filterMaterial)
                .queryParam("queryProduct", query)
                .build()
                .encode()
                .toUriString();
    }

    @PostMapping("/{id}/update-images")
    public String updateProductImages(
            @PathVariable("id") Integer id,
            @RequestParam("imagesToAdd") List<MultipartFile> imagesToAdd,
            @RequestParam("imagesToRemove") String imagesToRemove,
            Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> imagesToRemoveList;
        try {
            imagesToRemoveList = objectMapper.readValue(imagesToRemove, new TypeReference<List<Integer>>() {
            });
            productService.updateProductImages(id, imagesToAdd, imagesToRemoveList);
            model.addAttribute("toastMessages", new ToastMessage("success", "Cập nhật hình ảnh thành công!"));
        } catch (Exception e) {
            model.addAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }
        return "redirect:/seller/products/" + id + "/edit";
    }

    @PostMapping("/{id}/update-info")
    public String saveProductEdit(
            @PathVariable("id") Integer id,
            @ModelAttribute("productInfo") ProductDetailEdit productDetailEdit,
            Model model) {
        try {
            ProductDetail productDetail = productService.updateProductInfo(productDetailEdit);
        } catch (Exception e) {
            model.addAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }
        return "redirect:/seller/products/" + id + "/edit";
    }

    @PostMapping
    public String importProducts(@RequestParam("file") MultipartFile file, Model model,
            RedirectAttributes redirectAttrs) {
        if (file.isEmpty()) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Bạn chưa chọn file."));
            return "screens/seller/products/index";
        }
        try {
            productService.importProducts(file);
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Nhập sản phẩm thành công!"));
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }

        return "redirect:/seller/products";
    }

    @DeleteMapping("/{id}")
    public String deleteProductAdmin(
            @PathVariable("id") Integer id) {
        try {
            productService.deleteProductDetailById(id);
        } catch (NotFoundObjectException ex) {
            System.out.println(ex);
        }
        return "redirect:/seller/products";
    }
}
