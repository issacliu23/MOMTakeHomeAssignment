package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.constant.GrantSchemeConstants;
import com.example.mom.home.assignment.customexception.ResourceNotFoundException;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberEnum;
import com.example.mom.home.assignment.household.familymember.FamilyMemberRepository;
import com.example.mom.home.assignment.specification.HouseholdCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.mom.home.assignment.MockData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        FamilyMember member = getMockFamilyMember();
        Household request = getEmptyHousehold(null);
        request.addFamilyMember(member);
        Household mockResponse = getEmptyHousehold(1L);
        mockResponse.addFamilyMember(member);
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
        FamilyMember mockMember = getMockFamilyMember();
        mockMember.setSpouse("spouse");
        mockMember.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Single);
        Household mockHousehold = getMockHousehold(null);
        mockHousehold.addFamilyMember(mockMember);
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.createHousehold(mockHousehold);
        });
    }

    @Test
    public void whenCreateHouseholdWithoutSpouseButWrongMaritalStatusThenThrowConstraintViolationException() {
        FamilyMember mockMember = getMockFamilyMember();
        mockMember.setSpouse(null);
        mockMember.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        Household mockHousehold = getMockHousehold(null);
        mockHousehold.addFamilyMember(mockMember);
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.createHousehold(mockHousehold);
        });
    }
    //endregion

    //region Method: addFamilyMember
    @Test
    public void whenAddFamilyMemberShouldReturnObjectWithHousehold() {
        Long mockHouseholdId = 1L;
        FamilyMember newMember = getMockFamilyMember();
        Household mockHousehold = getMockHousehold(mockHouseholdId);
        mockHousehold.addFamilyMember(newMember);
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
        FamilyMember newMember = getMockFamilyMember();
        newMember.setName("new member");
        Optional<Household> mockResponse = Optional.empty();
        when(householdRepository.findById(any())).thenReturn(mockResponse);
        when(familyMemberRepository.save(newMember)).thenReturn(newMember);
        assertThrows(ResourceNotFoundException.class, () -> {
            householdService.addFamilyMember(mockHouseholdId, newMember);
        });
    }

    @Test
    public void whenAddFamilyMemberWithoutSpouseButWrongMaritalStatusShouldThrowConstraintViolationException() {
        FamilyMember mockMember = getMockFamilyMember();
        mockMember.setSpouse(null);
        mockMember.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.addFamilyMember(1L, mockMember);
        });
    }
    @Test
    public void whenAddFamilyMemberWithSpouseButWrongMaritalStatusShouldThrowConstraintViolationException() {
        FamilyMember mockMember = getMockFamilyMember();
        mockMember.setSpouse("spouse");
        mockMember.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Single);
        assertThrows(ConstraintViolationException.class, () -> {
            householdService.addFamilyMember(1L, mockMember);
        });
    }

    @Test
    public void whenAddFamilyMemberIsNullShouldReturnNull() {
        FamilyMember response = householdService.addFamilyMember(1L, null);
        assertNull(response);
    }

    @Test
    public void whenAddMarriedFamilyMemberAndSpouseExistInHouseholdShouldThrowResourceNotFoundException() {
        Long mockHouseholdId = 1L;
        FamilyMember husband = getMockFamilyMember();
        husband.setName("husband");
        husband.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        husband.setSpouse("wife");
        Household mockHousehold = getMockHousehold(mockHouseholdId);
        mockHousehold.addFamilyMember(husband);
        Optional<Household> mockResponse = Optional.of(mockHousehold);
        when(householdRepository.findById(mockHouseholdId)).thenReturn(mockResponse);
        FamilyMember wifeWithWrongHusband = getMockFamilyMember();
        wifeWithWrongHusband.setName("wife");
        wifeWithWrongHusband.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        wifeWithWrongHusband.setSpouse("wrongHusband");
        assertThrows(ResourceNotFoundException.class, () -> {
            householdService.addFamilyMember(1L, wifeWithWrongHusband);
        });
        FamilyMember wrongWifeWithHusband = getMockFamilyMember();
        wrongWifeWithHusband.setName("wrongWife");
        wrongWifeWithHusband.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        wrongWifeWithHusband.setSpouse("husband");
        assertThrows(ResourceNotFoundException.class, () -> {
            householdService.addFamilyMember(1L, wrongWifeWithHusband);
        });
    }

    //endregion

    //region Method: getAllHouseholds
    @Test
    public void whenGetAllHouseholdsShouldReturnEveryHouseholdWithFamilyMembers() {
        Household mockHousehold1 = getEmptyHousehold(1L);
        FamilyMember h1member1 = getMockFamilyMember();
        h1member1.setName("house 1 member 1");
        mockHousehold1.addFamilyMember(h1member1);

        Household mockHousehold2 = getEmptyHousehold(2L);
        FamilyMember h2member1 = getMockFamilyMember();
        h2member1.setName("house 1 member 2");
        FamilyMember h2member2 = getMockFamilyMember();
        h2member2.setName("house 2 member 2");
        mockHousehold2.addFamilyMember(h2member1);
        mockHousehold2.addFamilyMember(h2member2);


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
        FamilyMember mockMember = getMockFamilyMember();
        Household mockHousehold = getEmptyHousehold(mockHouseholdId);
        mockHousehold.addFamilyMember(mockMember);

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

    //region Method: getGrantEligibleHouseholds
    @Test
    public void whenGetGrantEligibleHouseholdsWithoutCriteriaShouldReturnCorrectList() throws Exception {
        when(householdRepository.findAll(any(Specification.class))).thenReturn(
                List.of(getMockStudentEncouragementBonusHousehold(1L)),
                List.of(getMockFamilyTogethernessSchemeHousehold(2L)),
                List.of(getMockElderBonusHousehold(3L)),
                List.of(getMockBabySunshineGrantHousehold(4L)),
                List.of(getMockYOLOGSTGrantHousehold(5L)));
        GrantEligibleHouseholdDTO response = householdService.getGrantEligibleHouseholds(null);
        AtomicInteger noOfEligibleHouseholds = new AtomicInteger(0);
        verify(householdRepository, times(5)).findAll(any(Specification.class));
        if(response != null) {
            response.getGrantEligibleHouseholdMap().forEach((key, value) -> {
                if (key.equals(HouseholdEnum.Grant.StudentEncouragementBonus)) {
                    for (Household h : value) {
                        noOfEligibleHouseholds.getAndIncrement();
                        assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit);
                        assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit));
                    }
                } else if (key.equals(HouseholdEnum.Grant.FamilyTogethernessScheme)) {
                    for (Household h : value) {
                        noOfEligibleHouseholds.getAndIncrement();
                        assertTrue(h.hasMarriedCouple());
                        assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
                    }
                } else if (key.equals(HouseholdEnum.Grant.ElderBonus)) {
                    for (Household h : value) {
                        noOfEligibleHouseholds.getAndIncrement();
                        assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() > GrantSchemeConstants.ElderBonusConstants.ageFloorLimit));
                    }
                } else if (key.equals(HouseholdEnum.Grant.BabySunshineGrant)) {
                    for (Household h : value) {
                        noOfEligibleHouseholds.getAndIncrement();
                        assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit));
                    }
                } else if (key.equals(HouseholdEnum.Grant.YOLOGSTGrant)) {
                    for (Household h : value) {
                        noOfEligibleHouseholds.getAndIncrement();
                        assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
                    }
                } else
                    fail();
            });
        }
        else
            fail();
        assertNotEquals(noOfEligibleHouseholds.get(), 0);
    }

    @Test
    public void whenGetGrantEligibleHouseholdsWithCriteriaShouldReturnCorrectList() throws Exception {
        int criteriaIncome = GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit-1;
        int criteriaSize = 1;
        HouseholdEnum.HousingType criteriaHousingType = HouseholdEnum.HousingType.HDB;
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);

        when(householdRepository.findAll(any(Specification.class))).thenReturn(List.of(getMockYOLOGSTGrantHousehold(1L)));
        GrantEligibleHouseholdDTO response = householdService.getGrantEligibleHouseholds(criteria);
        AtomicInteger noOfEligibleHouseholds = new AtomicInteger(0);
        verify(householdRepository, times(5)).findAll(any(Specification.class));
        if(response != null) {
            response.getGrantEligibleHouseholdMap().forEach((key, value) -> {
                if (key.equals(HouseholdEnum.Grant.YOLOGSTGrant)) {
                    for (Household h : value) {
                        noOfEligibleHouseholds.getAndIncrement();
                        assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                        assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                        assertEquals(h.getHousingType(), criteriaHousingType);
                        assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
                    }
                }
            });
        }
        else
            fail();
        assertNotEquals(noOfEligibleHouseholds.get(), 0);

    }






}
