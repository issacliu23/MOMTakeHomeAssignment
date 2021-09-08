package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMemberDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HouseholdDTO {
    private HouseholdEnum.HousingType housingType;
    private List<FamilyMemberDTO> familyMemberList = new ArrayList<>();
    @JsonIgnore
    private Integer householdIncome;

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

    public Integer getHouseholdIncome() {
        if(this.getFamilyMemberList().size() != 0) {
            int totalAnnualIncome = 0;
            for(FamilyMemberDTO m: this.getFamilyMemberList()) {
                totalAnnualIncome += m.getAnnualIncome();
            }
            return totalAnnualIncome;
        }
        else
            return 0;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj == this)
            return true;
        if (!(obj instanceof HouseholdDTO) && !(obj instanceof Household))
            return false;

        HouseholdDTO h = null;
        if(obj instanceof HouseholdDTO)
            h = (HouseholdDTO) obj;
        if(obj instanceof Household)
            h = new HouseholdDTO((Household) obj);

        if(this.getHousingType() == h.getHousingType() && this.getHouseholdIncome()==h.getHouseholdIncome() && this.getFamilyMemberList().size() == h.getFamilyMemberList().size()) {
            if(this.getFamilyMemberList().size() > 0) {
                for(int i=0; i<this.getFamilyMemberList().size(); i++) {
                    isEqual = this.getFamilyMemberList().get(i).equals(h.getFamilyMemberList().get(i));
                    if (!isEqual)
                        return false;
                }
            }
            else
                return true;
        }
        return isEqual;
    }
}
