package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.familymember.FamilyMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.mom.home.assignment.household.HouseholdPreparedData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HouseholdServiceTest {
    @Autowired
    private HouseholdService householdService;
    @MockBean
    private HouseholdRepository householdRepository;

    //region Method: createHousehold Test
    @Test
    public void whenCreateHouseholdSuccessThenReturnHouseholdObject() {
        FamilyMember member = getValidFamilyMember();
        Household request = new Household(HouseholdEnum.HousingType.HDB, new ArrayList<>(List.of(member)));
        Household mockResponse = new Household(1, HouseholdEnum.HousingType.HDB, new ArrayList<>(List.of(member)));
        when(householdRepository.save(any())).thenReturn(mockResponse);
        Household response = householdService.createHousehold(request);
        assertEquals(mockResponse, response);
    }

    @Test
    public void whenCreateHouseholdIsNullThenReturnNull() {
        Household response = householdService.createHousehold(null);
        assertNull(response);
    }

    @Test
    public void whenCreateHouseholdWithNoFamilyMembersThenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.createHousehold(new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>()));
        });
    }

    @Test
    public void whenCreateHouseholdWithSpouseButWrongMaritalStatusThenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.createHousehold(new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getWrongMaritalStatusWithSpouseFamilyMember()))));
        });
    }

    @Test
    public void whenCreateHouseholdWithoutSpouseButWrongMaritalStatusThenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.createHousehold(new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getWrongMaritalStatusWithoutSpouseFamilyMember()))));
        });
    }
    //endregion





}
