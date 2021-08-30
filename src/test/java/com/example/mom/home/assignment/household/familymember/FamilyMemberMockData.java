package com.example.mom.home.assignment.household.familymember;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberEnum;

import java.time.LocalDate;

public class FamilyMemberMockData {
    public static FamilyMember getValidFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(FamilyMemberEnum.Gender.Male);
        member.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        member.setSpouse("testspouse");
        member.setOccupationType(FamilyMemberEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }

    public static FamilyMember getValidFamilyMember(String name) {
        FamilyMember member = new FamilyMember();
        member.setName(name);
        member.setGender(FamilyMemberEnum.Gender.Male);
        member.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        member.setSpouse("testspouse");
        member.setOccupationType(FamilyMemberEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }

    public static FamilyMember getWrongMaritalStatusWithSpouseFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(FamilyMemberEnum.Gender.Male);
        member.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Single);
        member.setSpouse("testspouse");
        member.setOccupationType(FamilyMemberEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }

    public static FamilyMember getWrongMaritalStatusWithoutSpouseFamilyMember() {
        FamilyMember member = new FamilyMember();
        member.setName("testmember");
        member.setGender(FamilyMemberEnum.Gender.Male);
        member.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        member.setOccupationType(FamilyMemberEnum.OccupationType.Employed);
        member.setAnnualIncome(30000);
        member.setDob(LocalDate.of(1991,3,2));
        return member;
    }
}
