package com.example.shop.domain.usecase;

import com.example.shop.domain.dto.SimpleViolation;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    private final Set<SimpleViolation> violations;

    public ValidationException(Set<SimpleViolation> violations) {
        super("Validation failed: " + violations);
        this.violations = violations;
    }
}
