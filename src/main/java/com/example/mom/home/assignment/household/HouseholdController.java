package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "household")
public class HouseholdController {
    private final HouseholdService householdService;

    @Autowired
    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }
    //POST createHousehold
    @PostMapping
    public ResponseEntity<Household> createHouseHold(@Valid @RequestBody Household household) {
        try {
            Household createdHousehold = householdService.createHousehold(household);
            if (createdHousehold != null)
                return new ResponseEntity<>(createdHousehold, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //GET getAllHouseholds
    //GET getHousehold
    //GET getHouseholdsWithEligibleGrant
    //DELETE removeHousehold
    //DELETE removeFamilyMember
}
