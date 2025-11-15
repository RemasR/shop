package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class PhonenumberValidator implements Validator<User> {

    @Override
    public Set<SimpleViolation> validate(User user) {
        Set<SimpleViolation> violations = new HashSet<>();
        if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
            violations.add(new SimpleViolation("user.phoneNumber", "Phone number cannot be null or empty"));
        }
        else if (!user.getPhoneNumber().matches("^\\+9627[7-9]\\d{7}$")) {
            violations.add(new SimpleViolation("user.phoneNumber", "Invalid phone number format"));
        }

        return violations;
    }
}