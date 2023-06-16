package com.example.demo.services;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> GetAll() {
        return productRepository.findAll();
    }

    public void edit(Product editProduct) {
        productRepository.save(editProduct);
    }

    public void add(Product newProduct) {
        productRepository.save(newProduct);
    }

    public Product get(int id) {
        return productRepository.findById(id).get();
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }
}