package com.store.controller;

import com.store.entity.Product;
import com.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(path = "/black-friday/products")
public class ClientController {

    private ProductService productService;

    @Autowired
    public ClientController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/all-products", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.status(OK)
                .body(productService.getAllProducts());
    }

    @GetMapping(path = "/all-products-on-sale", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProductsOnSale(){
        return ResponseEntity.status(OK)
                .body(productService.getAllProductsOnSale());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.status(CREATED)
                .body(productService.addProduct(product));
    }

    @PostMapping(path = "add-products",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> addProducts(@RequestBody List<Product> products){
        return ResponseEntity.status(CREATED)
                .body(productService.addProducts(products));
    }
}
