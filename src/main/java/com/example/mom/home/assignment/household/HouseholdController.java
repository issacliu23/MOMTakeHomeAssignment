package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "household")
public class HouseholdController {
    private final HouseholdService householdService;

    @Autowired
    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

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

    @GetMapping
    public ResponseEntity<List<HouseholdDTO>> getAllHouseholds() {
        List<Household> households = householdService.getAllHouseholds();
        if(households != null) {
            List<HouseholdDTO> householdsDTOs = households.stream().map(HouseholdDTO::new).collect(Collectors.toList());
            return new ResponseEntity<List<HouseholdDTO>>(householdsDTOs, HttpStatus.OK);
        }
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //GET getHousehold
    @GetMapping("/{householdId}")
    public ResponseEntity<HouseholdDTO> getHousehold(@PathVariable(value = "householdId") Long householdId) {
        Household household = householdService.getHousehold(householdId);
        if(household != null)
            return new ResponseEntity<HouseholdDTO>(new HouseholdDTO(household), HttpStatus.OK);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //GET getHouseholdsWithEligibleGrant
    //DELETE removeHousehold
    //DELETE removeFamilyMember
}
