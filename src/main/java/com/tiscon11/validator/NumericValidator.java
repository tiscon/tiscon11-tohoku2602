package com.tiscon11.validator;

import org.springframework.util.ObjectUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@link Numeric}の検証を行う実装クラス。
 *
 * @author TIS Taro
 */
public class NumericValidator implements ConstraintValidator<Numeric, String> {

    @Override
    public void initialize(Numeric constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (ObjectUtils.isEmpty(s)) {
            return true;
        }
        return s.matches("[0-9]*");
    }
}
