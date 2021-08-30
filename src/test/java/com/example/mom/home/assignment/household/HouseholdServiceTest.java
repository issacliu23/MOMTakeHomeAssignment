package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.customexception.ResourceNotFoundException;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mom.home.assignment.household.familymember.FamilyMemberMockData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HouseholdServiceTest {
    @Autowired
    private HouseholdService householdService;
    @MockBean
    private HouseholdRepository householdRepository;
    @MockBean
    private FamilyMemberRepository familyMemberRepository;

    //region Method: createHousehold
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

    //region Method: addFamilyMember
    @Test
    public void whenAddFamilyMemberShouldReturnObjectWithHousehold() {
        Long mockHouseholdId = 1L;
        FamilyMember newMember = getValidFamilyMember("new member");
        Household mockHousehold = new Household(mockHouseholdId, HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getValidFamilyMember())));
        newMember.setHousehold(mockHousehold);
        Optional<Household> mockResponse = Optional.of(mockHousehold);
        when(householdRepository.findById(any())).thenReturn(mockResponse);
        when(familyMemberRepository.save(newMember)).thenReturn(newMember);
        FamilyMember response = householdService.addFamilyMember(mockHouseholdId, newMember);
        assertEquals(newMember, response);
        assertEquals(mockHousehold, response.getHousehold());
        assertEquals(mockHouseholdId, response.getHousehold().getId());
    }
    @Test
    public void whenAddFamilyMemberWithNoHouseholdFoundShouldThrowResourceNotFoundException() {
        Long mockHouseholdId = 1L;
        FamilyMember newMember = getValidFamilyMember("new member");
        Optional<Household> mockResponse = Optional.empty();
        when(householdRepository.findById(any())).thenReturn(mockResponse);
        when(familyMemberRepository.save(newMember)).thenReturn(newMember);
        assertThrows(ResourceNotFoundException.class, () -> {
            householdService.addFamilyMember(mockHouseholdId, newMember);
        });
    }

    @Test
    public void whenAddFamilyMemberWithoutSpouseButWrongMaritalStatusShouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.addFamilyMember(1L, getWrongMaritalStatusWithoutSpouseFamilyMember());
        });
    }
    @Test
    public void whenAddFamilyMemberWithSpouseButWrongMaritalStatusShouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.addFamilyMember(1L, getWrongMaritalStatusWithSpouseFamilyMember());
        });
    }

    @Test
    public void whenAddFamilyMemberIsNullShouldReturnNull() {
        FamilyMember response = householdService.addFamilyMember(1L, null);
        assertNull(response);
    }

    //endregion

    //region Method: getAllHouseholds
    @Test
    public void whenGetAllHouseholdsShouldReturnEveryHouseholdWithFamilyMembers() {
        Household mockHousehold1 = new Household(1, HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getValidFamilyMember("member1"))));
        Household mockHousehold2 = new Household(2, HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getValidFamilyMember("member2"),getValidFamilyMember("member3"))));
        List<Household> mockHouseholds = new ArrayList<>(List.of(mockHousehold1,mockHousehold2));
        when(householdRepository.findAll()).thenReturn(mockHouseholds);
        List<Household> response = householdService.getAllHouseholds();
        assertEquals(mockHouseholds.size(), response.size());
        assertEquals(mockHouseholds.get(0).getFamilyMemberList().size(), response.get(0).getFamilyMemberList().size());
        assertEquals(mockHouseholds.get(1).getFamilyMemberList().size(), response.get(1).getFamilyMemberList().size());
        assertEquals(mockHouseholds.get(0).getFamilyMemberList().get(0).getName(), response.get(0).getFamilyMemberList().get(0).getName());
    }

    @Test
    public void whenGetAllHouseholdsAndNoHouseholdFoundShouldReturnEmptyList() {
        when(householdRepository.findAll()).thenReturn(new ArrayList<>());
        List<Household> actualHouseholds = householdService.getAllHouseholds();
        assertEquals(0, actualHouseholds.size());
    }

    //region Method: getHousehold
    @Test
    public void whenGetHouseholdShouldReturnHouseholdWithFamilyMembers() {
        Long mockHouseholdId = 1L;
        FamilyMember mockMember = getValidFamilyMember("mock member");
        Household mockHousehold = new Household(mockHouseholdId, HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(mockMember)));
        Optional<Household> mockResponse = Optional.of(mockHousehold);
        when(householdRepository.findById(mockHouseholdId)).thenReturn(mockResponse);
        Household response = householdService.getHousehold(mockHouseholdId);
        assertEquals(mockHousehold, response);
        assertEquals(mockHousehold.getFamilyMemberList().get(0).getName(), mockMember.getName());
    }

    @Test
    public void whenGetHouseholdHaveNoResultShouldThrowResourceNotFoundException() {
        Long mockHouseholdId = 1L;
        Optional<Household> emptyResponse = Optional.empty();
        when(householdRepository.findById(mockHouseholdId)).thenReturn(emptyResponse);
        assertThrows(ResourceNotFoundException.class, () -> {
            householdService.getHousehold(mockHouseholdId);
        });
    }
    //endregion






}
