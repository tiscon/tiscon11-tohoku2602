package com.tiscon11.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

/**
 * 入力値が指定された形式の数値であるかをバリデーションするクラス。
 *
 * @author TIS Taro
 */
@Documented
@Constraint(validatedBy = { NumericValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Numeric {

    String message() default "{tiscon.Numeric.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
            ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        Numeric[] value();
    }
}
