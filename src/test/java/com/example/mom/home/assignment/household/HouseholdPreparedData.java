package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.familymember.FamilyMember;

import java.time.LocalDate;

public class HouseholdPreparedData {
    public static FamilyMember getValidFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(HouseholdEnum.Gender.Male);
        member.setMaritalStatus(HouseholdEnum.MaritalStatus.Married);
        member.setSpouse("testspouse");
        member.setOccupationType(HouseholdEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }

    public static FamilyMember getWrongMaritalStatusWithSpouseFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(HouseholdEnum.Gender.Male);
        member.setMaritalStatus(HouseholdEnum.MaritalStatus.Single);
        member.setSpouse("testspouse");
        member.setOccupationType(HouseholdEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }

    public static FamilyMember getWrongMaritalStatusWithoutSpouseFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(HouseholdEnum.Gender.Male);
        member.setMaritalStatus(HouseholdEnum.MaritalStatus.Married);
        member.setOccupationType(HouseholdEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }
}
