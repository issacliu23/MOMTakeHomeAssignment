package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HouseholdDTO {
    private HouseholdEnum.HousingType housingType;
    private List<FamilyMemberDTO> familyMemberList = new ArrayList<>();

    public HouseholdDTO() {
    }

    public HouseholdDTO(Household household) {
        this(household.getHousingType(), household.getFamilyMemberList().stream().map(FamilyMemberDTO::new).collect(Collectors.toList()));
    }


    public HouseholdDTO(HouseholdEnum.HousingType housingType, List<FamilyMemberDTO> familyMemberList) {
        this.housingType = housingType;
        this.familyMemberList = familyMemberList;
    }


    public HouseholdEnum.HousingType getHousingType() {
        return housingType;
    }

    public List<FamilyMemberDTO> getFamilyMemberList() {
        return familyMemberList;
    }





}
