package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mom.home.assignment.household.familymember.FamilyMemberMockData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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

}
