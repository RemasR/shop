package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.product.ProductExistenceValidator;
import com.example.shop.domain.validators.product.ProductNameValidator;
import com.example.shop.domain.validators.product.ProductPriceValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

    public class UpdateProductUsecase {
        private final ProductRepository productRepository;
        private final List<Validator<Product>> productValidators;
        private final List<Validator<Integer>> productExistenceValidators;

        public UpdateProductUsecase(ProductRepository productRepository){
            this.productRepository = productRepository;
            this.productValidators = List.of(
                    new ProductNameValidator(),
                    new ProductPriceValidator()
            );
            this.productExistenceValidators = List.of(
                    new ProductExistenceValidator(productRepository)
            );
        }

        public Product execute(int id, ProductDTO dto){
            if(dto == null){
                throw new IllegalArgumentException("Update data cannot be null");
            }

            validateAll(productExistenceValidators, id);

            Product existingProduct = productRepository.findById(id);

            if(dto.getName() != null){
                existingProduct.setName(dto.getName());
            }

            if(dto.getDescription() != null){
                existingProduct.setDescription(dto.getDescription());
            }

            if(dto.getPrice() != null){
                existingProduct.setPrice(dto.getPrice());
            }

            validateAll(productValidators, existingProduct);

            return productRepository.save(existingProduct);
        }

        private <T> void validateAll(List<Validator<T>> validators, T value) {
            Set<SimpleViolation> violations = new HashSet<>();

            for (Validator<T> validator : validators) {
                violations.addAll(validator.validate(value));
            }

            if (!violations.isEmpty()) {
                throw new IllegalArgumentException("Validation failed: " + violations);
            }
        }
    }

