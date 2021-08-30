package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.constant.GrantSchemeConstants;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.specification.HouseholdCriteria;
import com.example.mom.home.assignment.specification.HouseholdSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mom.home.assignment.MockData.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class HouseholdRepositoryTest {
    @Autowired
    private HouseholdRepository householdRepository;
    @Test
    public void whenSaveReturnHouseholdAndFamilyMembers() {
        Household request = getMockHousehold(null);
        Household household = householdRepository.save(request);
        assertNotNull(household);
        assertEquals(household.getFamilyMemberList().size(), 1);
        Optional<Household> savedHousehold = householdRepository.findById(household.getId());
        if(savedHousehold.isPresent())
            assertEquals(household, savedHousehold.get());
        else
            fail();
    }

    @Test
    public void whenFindAllReturnAllHouseholdsWithFamilyMembers() {
        Household hdb = getEmptyHousehold(null);
        FamilyMember h1member1 = getMockFamilyMember();
        h1member1.setName("house 1 member 1");
        FamilyMember h1member2 = getMockFamilyMember();
        h1member2.setName("house 1 member 2");
        hdb.addFamilyMember(h1member1);
        hdb.addFamilyMember(h1member2);

        Household condo = getEmptyHousehold(null);
        condo.setHousingType(HouseholdEnum.HousingType.Condominium);
        FamilyMember h2member1 = getMockFamilyMember();
        h2member1.setName("house 2 member 1");
        condo.addFamilyMember(h2member1);

        List<Household> mockHouseholdList = new ArrayList<Household>(List.of(hdb, condo));
        householdRepository.saveAllAndFlush(mockHouseholdList);
        List<Household> response = householdRepository.findAll();
        if(response.size() != 0) {
            assertEquals(mockHouseholdList.size(), response.size());
            for(int i=0; i<response.size(); i++) {
                assertEquals(mockHouseholdList.get(i).getHousingType(), response.get(i).getHousingType());
                assertEquals(mockHouseholdList.get(i).getFamilyMemberList().size(), response.get(i).getFamilyMemberList().size());
                assertEquals(mockHouseholdList.get(i).getFamilyMemberList().get(0).getId(), response.get(i).getFamilyMemberList().get(0).getId());
            }
        }
        else
            fail();
    }

    @Test
    public void whenFindAllIsEmptyReturnEmptyList() {
        List<Household> response = householdRepository.findAll();
        assertEquals(response.size(), 0);
    }

    @Test
    public void whenFindByIdShouldReturnHouseholdWithFamilyMembers() {
        Household household = getMockHousehold(null);
        householdRepository.save(household);
        Optional<Household> savedHousehold = householdRepository.findById(household.getId());
        if(savedHousehold.isPresent()) {
            assertEquals(household, savedHousehold.get());
            assertEquals(household.getFamilyMemberList().size(), savedHousehold.get().getFamilyMemberList().size());
        }
        else
            fail();
    }

    @Test
    public void whenFindByIdHasNoResultShouldReturnOptionalEmpty() {
        Optional<Household> savedHousehold = householdRepository.findById(1L);
        assertTrue(savedHousehold.isEmpty());
    }

    @Test
    public void whenFindAllWithStudentEncouragementSpecsWithoutCriteriaShouldReturnResult() {
        Household studentEncouragementHousehold = getMockStudentEncouragementBonusHousehold(null);
        householdRepository.save(studentEncouragementHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.studentEncouragementBonusSpecification(null));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit);
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithFamilyTogethernessSpecsWithoutCriteriaShouldReturnResult() {
        Household familyTogethernessSchemeHousehold = getMockFamilyTogethernessSchemeHousehold(null);
        householdRepository.save(familyTogethernessSchemeHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.familyTogethernessSpecification(null));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertTrue(h.hasMarriedCouple());
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithElderBonusSpecsWithoutCriteriaShouldReturnResult() {
        Household elderBonusHousehold = getMockElderBonusHousehold(null);
        householdRepository.save(elderBonusHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.elderBonusSpecification(null));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() > GrantSchemeConstants.ElderBonusConstants.ageFloorLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithBabySunshineSpecsWithoutCriteriaShouldReturnResult() {
        Household babySunshineHousehold = getMockBabySunshineGrantHousehold(null);
        householdRepository.save(babySunshineHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.babySunshineGrantSpecification(null));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithYoloGstSpecsWithoutCriteriaShouldReturnResult() {
        Household yoloGstGrantHousehold = getMockYOLOGSTGrantHousehold(null);
        householdRepository.save(yoloGstGrantHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.yoloGstGrantSpecification(null));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithStudentEncouragementSpecsWithCriteriaShouldReturnResult() {
        Household studentEncouragementHousehold = getMockStudentEncouragementBonusHousehold(null);
        int criteriaIncome = studentEncouragementHousehold.getHouseholdIncome();
        int criteriaSize = studentEncouragementHousehold.getFamilyMemberList().size();
        HouseholdEnum.HousingType criteriaHousingType = studentEncouragementHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        householdRepository.save(studentEncouragementHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.studentEncouragementBonusSpecification(criteria));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                assertEquals(h.getHousingType(), criteriaHousingType);
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit));
                assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit);
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithFamilyTogethernessSpecsWithCriteriaShouldReturnResult() {
        Household familyTogethernessSchemeHousehold = getMockFamilyTogethernessSchemeHousehold(null);
        familyTogethernessSchemeHousehold.getFamilyMemberList().get(0).setAnnualIncome(familyTogethernessSchemeHousehold.getFamilyMemberList().get(0).getAnnualIncome()+1);
        int criteriaIncome = familyTogethernessSchemeHousehold.getHouseholdIncome();
        int criteriaSize = familyTogethernessSchemeHousehold.getFamilyMemberList().size();
        HouseholdEnum.HousingType criteriaHousingType = familyTogethernessSchemeHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);

        householdRepository.save(familyTogethernessSchemeHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.familyTogethernessSpecification(criteria));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                assertEquals(h.getHousingType(), criteriaHousingType);
                assertTrue(h.hasMarriedCouple());
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithElderBonusSpecsWithCriteriaShouldReturnResult() {
        Household elderBonusHousehold = getMockElderBonusHousehold(null);
        elderBonusHousehold.getFamilyMemberList().get(0).setAnnualIncome(elderBonusHousehold.getFamilyMemberList().get(0).getAnnualIncome()+1);
        int criteriaIncome = elderBonusHousehold.getHouseholdIncome();
        int criteriaSize = elderBonusHousehold.getFamilyMemberList().size();
        HouseholdEnum.HousingType criteriaHousingType =  elderBonusHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);

        householdRepository.save(elderBonusHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.elderBonusSpecification(criteria));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                assertEquals(h.getHousingType(), criteriaHousingType);
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() > GrantSchemeConstants.ElderBonusConstants.ageFloorLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithBabySunshineSpecsWithCriteriaShouldReturnResult() {
        Household babySunshineHousehold = getMockBabySunshineGrantHousehold(null);
        babySunshineHousehold.getFamilyMemberList().get(0).setAnnualIncome(babySunshineHousehold.getFamilyMemberList().get(0).getAnnualIncome()+1);
        int criteriaIncome = babySunshineHousehold.getHouseholdIncome();
        int criteriaSize = babySunshineHousehold.getFamilyMemberList().size();
        HouseholdEnum.HousingType criteriaHousingType = babySunshineHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        householdRepository.save(babySunshineHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.babySunshineGrantSpecification(criteria));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                assertEquals(h.getHousingType(), criteriaHousingType);
                assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit));
            }
        }
        else
            fail();
    }
    @Test
    public void whenFindAllWithYoloGstSpecsWithCriteriaShouldReturnResult() {
        Household yoloGstGrantHousehold = getMockYOLOGSTGrantHousehold(null);
        int criteriaIncome = yoloGstGrantHousehold.getHouseholdIncome();
        int criteriaSize = yoloGstGrantHousehold.getFamilyMemberList().size();
        HouseholdEnum.HousingType criteriaHousingType = yoloGstGrantHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        householdRepository.save(yoloGstGrantHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.yoloGstGrantSpecification(criteria));
        if(responseList.size() > 0) {
            for(Household h: responseList) {
                assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                assertEquals(h.getHousingType(), criteriaHousingType);
                assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
            }
        }
        else
            fail();
    }

    @Test
    public void whenFindAllWithStudentEncouragementSpecsWithCriteriaButNoResultsShouldReturnEmpty() {
        Household studentEncouragementHousehold = getMockStudentEncouragementBonusHousehold(null);
        int criteriaIncome = studentEncouragementHousehold.getHouseholdIncome();
        int criteriaSize = studentEncouragementHousehold.getFamilyMemberList().size()+1;
        HouseholdEnum.HousingType criteriaHousingType = studentEncouragementHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        householdRepository.save(studentEncouragementHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.studentEncouragementBonusSpecification(criteria));
        if(responseList.size() > 0)
            fail();
    }
    @Test
    public void whenFindAllWithFamilyTogethernessSpecsWithCriteriaButNoResultsShouldReturnEmpty() {
        Household familyTogethernessSchemeHousehold = getMockFamilyTogethernessSchemeHousehold(null);
        familyTogethernessSchemeHousehold.getFamilyMemberList().get(0).setAnnualIncome(familyTogethernessSchemeHousehold.getFamilyMemberList().get(0).getAnnualIncome()+1);
        int criteriaIncome = familyTogethernessSchemeHousehold.getHouseholdIncome();
        int criteriaSize = familyTogethernessSchemeHousehold.getFamilyMemberList().size()+1;
        HouseholdEnum.HousingType criteriaHousingType = familyTogethernessSchemeHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);

        householdRepository.save(familyTogethernessSchemeHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.familyTogethernessSpecification(criteria));
        if(responseList.size() > 0)
            fail();

    }
    @Test
    public void whenFindAllWithElderBonusSpecsWithCriteriaButNoResultsShouldReturnEmpty() {
        Household elderBonusHousehold = getMockElderBonusHousehold(null);
        elderBonusHousehold.getFamilyMemberList().get(0).setAnnualIncome(elderBonusHousehold.getFamilyMemberList().get(0).getAnnualIncome()+1);
        int criteriaIncome = elderBonusHousehold.getHouseholdIncome();
        int criteriaSize = elderBonusHousehold.getFamilyMemberList().size()+1;
        HouseholdEnum.HousingType criteriaHousingType =  elderBonusHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);

        householdRepository.save(elderBonusHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.elderBonusSpecification(criteria));
        if(responseList.size() > 0)
            fail();
    }
    @Test
    public void whenFindAllWithBabySunshineSpecsWithCriteriaButNoResultsShouldReturnEmpty() {
        Household babySunshineHousehold = getMockBabySunshineGrantHousehold(null);
        babySunshineHousehold.getFamilyMemberList().get(0).setAnnualIncome(babySunshineHousehold.getFamilyMemberList().get(0).getAnnualIncome()+1);
        int criteriaIncome = babySunshineHousehold.getHouseholdIncome();
        int criteriaSize = babySunshineHousehold.getFamilyMemberList().size()+1;
        HouseholdEnum.HousingType criteriaHousingType = babySunshineHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        householdRepository.save(babySunshineHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.babySunshineGrantSpecification(criteria));
        if(responseList.size() > 0)
            fail();
    }
    @Test
    public void whenFindAllWithYoloGstSpecsWithCriteriaButNoResultsShouldReturnEmpty() {
        Household yoloGstGrantHousehold = getMockYOLOGSTGrantHousehold(null);
        int criteriaIncome = yoloGstGrantHousehold.getHouseholdIncome();
        int criteriaSize = yoloGstGrantHousehold.getFamilyMemberList().size()+1;
        HouseholdEnum.HousingType criteriaHousingType = yoloGstGrantHousehold.getHousingType();
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        householdRepository.save(yoloGstGrantHousehold);
        List<Household> responseList = householdRepository.findAll(HouseholdSpecification.yoloGstGrantSpecification(criteria));
        if(responseList.size() > 0)
            fail();
    }
}
