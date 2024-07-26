package com.assu.study.clean_me.common;

import jakarta.validation.*;
import java.util.Set;

public abstract class SelfValidating<T> {
  private Validator validator;

  public SelfValidating() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  // 필드에 지정된 Bean Validation 애너테이션(@NotNull 같은) 을 검증 후 유효성 검증 규칙을 위반할 경우 예외 던짐
  protected void validateSelf() {
    Set<ConstraintViolation<T>> violations = validator.validate((T) this);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
