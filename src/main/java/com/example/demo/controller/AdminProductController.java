package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("admin/products")
@Controller
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listproduct", productService.GetAll());
        return "admin/products/index.html";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", categoryService.GetAll());
        return "admin/products/create";
    }

    @PostMapping("/create")
    public String create(@Validated Product newProduct,
                         @RequestParam MultipartFile imageProduct,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("listCategory", categoryService.GetAll());
            return "admin/products/create";
        }
        if (imageProduct != null && imageProduct.getSize() > 0) {
            try {
                String newImageFile = UUID.randomUUID() + ".png";
                Path root = Paths.get("src/main/resources/static/images" + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(),
                        root, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.add(newProduct);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.get(id));
        model.addAttribute("listCategory", categoryService.GetAll());
        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Validated Product editProduct,
                       @RequestParam MultipartFile imageProduct,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            model.addAttribute("listCategory", categoryService.GetAll());
            return "admin/products/edit";
        }
        if (imageProduct != null && imageProduct.getSize() > 0) {
            try {
                editProduct.setImage(imageProduct.getOriginalFilename());
                Path root = Paths.get("src/main/resources/static/images");
                String filename = imageProduct.getOriginalFilename();
                Path filePath = root.resolve(filename);

                Files.copy(imageProduct.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.edit(editProduct);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}