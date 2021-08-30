package com.example.mom.home.assignment.household.familymember;

import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.household.HouseholdEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mom.home.assignment.MockData.getMockFamilyMember;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class FamilyMemberRepositoryTest {
    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Test
    public void whenSaveReturnFamilyMemberWithHousehold() {
        Household mockHousehold = new Household(1L, HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getMockFamilyMember())));
        FamilyMember newMember = getMockFamilyMember();
        newMember.setName("new member");
        newMember.setHousehold(mockHousehold);
        FamilyMember response = familyMemberRepository.save(newMember);
        assertNotNull(response);
        assertNotNull(newMember.getHousehold());
        Optional<FamilyMember> savedMember = familyMemberRepository.findById(newMember.getId());
        if(savedMember.isPresent()) {
            assertEquals(newMember, savedMember.get());
        }
        else
            fail();
    }

    @Test
    public void whenSaveWithoutHouseholdShouldThrowException() {
        FamilyMember newMember = getMockFamilyMember();
        assertThrows(Exception.class, () -> {
            familyMemberRepository.saveAndFlush(newMember); // use saveAndFlush as validation is not committed when using save
        });

    }
}
