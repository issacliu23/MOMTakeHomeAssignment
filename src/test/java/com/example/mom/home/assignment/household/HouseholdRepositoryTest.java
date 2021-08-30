package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mom.home.assignment.household.familymember.FamilyMemberMockData.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class HouseholdRepositoryTest {
    @Autowired
    private HouseholdRepository householdRepository;
    @Test
    public void whenSaveReturnHouseholdAndFamilyMembers() {
        Household request = new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getValidFamilyMember())));
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
        FamilyMember house1member1 = getValidFamilyMember("house1member1");
        FamilyMember house2member1 = getValidFamilyMember("house2member1");
        FamilyMember house3member1 = getValidFamilyMember("house3member1");

        Household household1 = new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>());
        house1member1.setHousehold(household1);
        household1.addFamilyMember(house1member1);

        Household household2 = new Household(HouseholdEnum.HousingType.Condominium, new ArrayList<FamilyMember>());
        house2member1.setHousehold(household2);
        household2.addFamilyMember(house2member1);

        Household household3 = new Household(HouseholdEnum.HousingType.Landed, new ArrayList<FamilyMember>());
        house3member1.setHousehold(household3);
        household3.addFamilyMember(house3member1);

        List<Household> mockHouseholdList = new ArrayList<Household>(List.of(household1, household2, household3));
        householdRepository.saveAllAndFlush(mockHouseholdList);
        List<Household> response = householdRepository.findAll();
        if(response.size() != 0) {
            assertEquals(mockHouseholdList.size(), response.size());
            for(int i=0; i<response.size(); i++) {
                assertEquals(mockHouseholdList.get(i).getHousingType(), response.get(i).getHousingType());
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
}
