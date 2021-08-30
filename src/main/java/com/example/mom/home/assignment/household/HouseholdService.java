package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.familymember.FamilyMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class HouseholdService {
    private final HouseholdRepository householdRepository;
    @Autowired
    private Validator validator;

    @Autowired
    public HouseholdService(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
    }

    public Household createHousehold(Household household) {
        if(household != null) {
            Set<ConstraintViolation<Household>> violations = validator.validate(household);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Household> constraintViolation : violations) {
                    sb.append(constraintViolation.getMessage());
                }
                throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
            }
            Household newHousehold = new Household();
            newHousehold.setHousingType(household.getHousingType());
            for (FamilyMember member : household.getFamilyMemberList()) {
                newHousehold.addFamilyMember(member);
            }
            return householdRepository.save(newHousehold);
        }
        else
            return null;
    }
}
