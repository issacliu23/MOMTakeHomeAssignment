package com.example.mom.home.assignment;

import com.example.mom.home.assignment.constant.GrantSchemeConstants;
import com.example.mom.home.assignment.household.GrantEligibleHouseholdDTO;
import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.household.HouseholdDTO;
import com.example.mom.home.assignment.household.HouseholdEnum;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberEnum;
import com.example.mom.home.assignment.specification.HouseholdCriteria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static FamilyMember getMockFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(FamilyMemberEnum.Gender.Male);
        member.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Single);
        member.setOccupationType(FamilyMemberEnum.OccupationType.Employed);
        member.setAnnualIncome(0);
        member.setDob(LocalDate.of(1990,3,2));
        return member;
    }

    public static Household getMockHousehold(Long id) {
        Household h = getEmptyHousehold(id);
        h.addFamilyMember(getMockFamilyMember());
        return h;
    }

    public static Household getEmptyHousehold(Long id) {
        if(id != null)
            return new Household(id, HouseholdEnum.HousingType.HDB,new ArrayList<FamilyMember>());
        else
            return new Household(HouseholdEnum.HousingType.HDB,new ArrayList<FamilyMember>());
    }


    public static Household getMockStudentEncouragementBonusHousehold(Long id) {
        Household h = getEmptyHousehold(id);
        FamilyMember incomeLessThanCeilingMan = getMockFamilyMember();
        incomeLessThanCeilingMan.setAnnualIncome(GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit-1);
        FamilyMember ageLessThanCeilingChild = getMockFamilyMember();
        int child1Age = GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit-1;
        ageLessThanCeilingChild.setDob(LocalDate.of(LocalDate.now().getYear()-child1Age, 1, 1));
        h.addFamilyMember(incomeLessThanCeilingMan);
        h.addFamilyMember(ageLessThanCeilingChild);
        return h;
    }

    public static Household getMockFamilyTogethernessSchemeHousehold(Long id) {
        Household h = getEmptyHousehold(id);
        FamilyMember husband = getMockFamilyMember();
        husband.setName("James");
        husband.setSpouse("Mary");
        husband.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);

        FamilyMember wife = getMockFamilyMember();
        wife.setName("Mary");
        wife.setSpouse("James");
        wife.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        wife.setGender(FamilyMemberEnum.Gender.Female);

        FamilyMember ageLessThanCeilingChild = getMockFamilyMember();
        int childAge = GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit-1;
        ageLessThanCeilingChild.setDob(LocalDate.of(LocalDate.now().getYear()-childAge, 1, 1));
        h.addFamilyMember(husband);
        h.addFamilyMember(wife);
        h.addFamilyMember(ageLessThanCeilingChild);
        return h;
    }

    public static Household getMockElderBonusHousehold(Long id) { // 1-man family
        Household h = getEmptyHousehold(id);
        FamilyMember ageMoreThanFloorElderly = getMockFamilyMember();
        int elderlyAge = GrantSchemeConstants.ElderBonusConstants.ageFloorLimit+1;
        ageMoreThanFloorElderly.setDob(LocalDate.of(LocalDate.now().getYear()-elderlyAge, 1, 1));
        h.addFamilyMember(ageMoreThanFloorElderly);
        return h;
    }
    public static Household getMockBabySunshineGrantHousehold(Long id) { // 2-man family
        Household h = getEmptyHousehold(id);
        FamilyMember familyMan = getMockFamilyMember();
        FamilyMember ageLessThanCeilingChild = getMockFamilyMember();
        int childAge = GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit-1;
        ageLessThanCeilingChild.setDob(LocalDate.of(LocalDate.now().getYear()-childAge, 1, 1));
        h.addFamilyMember(familyMan);
        h.addFamilyMember(ageLessThanCeilingChild);
        return h;
    }
    public static Household getMockYOLOGSTGrantHousehold(Long id) {
        Household h = getEmptyHousehold(id);
        FamilyMember yoloMember = getMockFamilyMember();
        yoloMember.setAnnualIncome(GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit-1);
        h.addFamilyMember(yoloMember);
        return h;
    }

    public static GrantEligibleHouseholdDTO getMockGrantEligibleHouseholds() {
        GrantEligibleHouseholdDTO eligibleHouseholds = new GrantEligibleHouseholdDTO();
        eligibleHouseholds.getGrantEligibleHouseholdMap().put(HouseholdEnum.Grant.StudentEncouragementBonus, List.of(getMockStudentEncouragementBonusHousehold(1L)));
        eligibleHouseholds.getGrantEligibleHouseholdMap().put(HouseholdEnum.Grant.FamilyTogethernessScheme, List.of(getMockFamilyTogethernessSchemeHousehold(2L)));
        eligibleHouseholds.getGrantEligibleHouseholdMap().put(HouseholdEnum.Grant.ElderBonus, List.of(getMockElderBonusHousehold(3L)));
        eligibleHouseholds.getGrantEligibleHouseholdMap().put(HouseholdEnum.Grant.BabySunshineGrant, List.of(getMockBabySunshineGrantHousehold(4L)));
        eligibleHouseholds.getGrantEligibleHouseholdMap().put(HouseholdEnum.Grant.YOLOGSTGrant, List.of(getMockYOLOGSTGrantHousehold(5L)));
        return eligibleHouseholds;
    }
}
