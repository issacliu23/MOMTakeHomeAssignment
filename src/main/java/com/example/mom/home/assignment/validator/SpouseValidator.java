package com.example.mom.home.assignment.validator;

import com.example.mom.home.assignment.familymember.FamilyMember;
import com.example.mom.home.assignment.household.HouseholdEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpouseValidator implements ConstraintValidator<ValidateSpouseAnnotation, FamilyMember> {
    @Override
    public void initialize(ValidateSpouseAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(FamilyMember familyMember, ConstraintValidatorContext constraintValidatorContext) {
        if(familyMember.getMaritalStatus() == HouseholdEnum.MaritalStatus.Single || familyMember.getMaritalStatus() == HouseholdEnum.MaritalStatus.Divorced)
            return familyMember.getSpouse() == null || familyMember.getSpouse().isEmpty();
        else
            return familyMember.getSpouse() != null && !familyMember.getSpouse().isEmpty();

    }
}
