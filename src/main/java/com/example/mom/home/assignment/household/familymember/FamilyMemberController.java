package com.example.mom.home.assignment.household.familymember;

import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.household.HouseholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class FamilyMemberController {
    private final HouseholdService householdService;

    @Autowired
    public FamilyMemberController(HouseholdService householdService){
        this.householdService = householdService;
    }

    @PostMapping("/household/{householdId}/family-member")
    public ResponseEntity<FamilyMember> addFamilyMember(@PathVariable(value = "householdId") Long householdId, @Valid @RequestBody FamilyMember member) {
        FamilyMember createdMember = householdService.addFamilyMember(householdId, member);
        if(createdMember != null)
            return new ResponseEntity<>(createdMember, HttpStatus.OK);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
