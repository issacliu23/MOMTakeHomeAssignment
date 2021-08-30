package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.customexception.ResourceNotFoundException;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
public class HouseholdService {
    private final HouseholdRepository householdRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public HouseholdService(HouseholdRepository householdRepository, FamilyMemberRepository familyMemberRepository) {
        this.householdRepository = householdRepository;
        this.familyMemberRepository = familyMemberRepository;
    }

    public Household createHousehold(Household household) throws ConstraintViolationException{
        if(household != null) {
            validateConstraints(household);
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

    public FamilyMember addFamilyMember(Long householdId, FamilyMember member) {
        if(member != null) {
            validateConstraints(member);
            Optional<Household> householdOptional = householdRepository.findById(householdId);
            if (householdOptional.isPresent()) {
                member.setHousehold(householdOptional.get());
                return familyMemberRepository.save(member);
            } else
                throw new ResourceNotFoundException("Household not found");
        }
        else
            return null;
    }

    private void validateConstraints(Object obj) throws ConstraintViolationException {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
    }
}
