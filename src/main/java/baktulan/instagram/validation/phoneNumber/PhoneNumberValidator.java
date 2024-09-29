package baktulan.instagram.validation.phoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation,String> {
    @Override
    public boolean isValid(String number, ConstraintValidatorContext constraintValidatorContext) {
        return number.startsWith("+996")&& number.length()==13;
    }
}
