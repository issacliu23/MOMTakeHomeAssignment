package com.example.mom.home.assignment.household;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table
public class Household {
    @Id
    @SequenceGenerator(
            name = "household_sequence",
            sequenceName = "household_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "household_sequence"
    )
    private long id;

    @NotNull
    private HouseholdEnum.HousingType housingType;

    @NotEmpty(message="There must be at least 1 family member in the household")
    @Valid
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("household")
    private List<FamilyMember> familyMemberList = new ArrayList<>();

    @JsonIgnore
    @Transient
    private Integer householdIncome;

    public Household() {}

    public Household(HouseholdEnum.HousingType housingType, List<FamilyMember> familyMemberList) {
        this.housingType = housingType;
        this.familyMemberList = familyMemberList;
    }

    public Household(long id, HouseholdEnum.HousingType housingType, List<FamilyMember> familyMemberList) {
        this.id = id;
        this.housingType = housingType;
        this.familyMemberList = familyMemberList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HouseholdEnum.HousingType getHousingType() {
        return housingType;
    }

    public void setHousingType(HouseholdEnum.HousingType housingType) {
        this.housingType = housingType;
    }

    public List<FamilyMember> getFamilyMemberList() {
        return familyMemberList;
    }

    private void setFamilyMemberList(List<FamilyMember> familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    public void addFamilyMember(FamilyMember member) {
        familyMemberList.add(member);
        member.setHousehold(this);
    }

    public Integer getHouseholdIncome() {
        if(this.getFamilyMemberList().size() != 0) {
            int totalAnnualIncome = 0;
            for(FamilyMember m: this.getFamilyMemberList()) {
                totalAnnualIncome += m.getAnnualIncome();
            }
            return totalAnnualIncome;
        }
        else
            return 0;
    }

    public boolean hasMarriedCouple() {
        Map<String, String> husbandAndWifeMap = new HashMap<>();
        for(FamilyMember m : this.getFamilyMemberList()) {
            if(husbandAndWifeMap.containsKey(m.getName())) {
                return true;
            }
            else {
                if(m.getSpouse() != null)
                    husbandAndWifeMap.put(m.getSpouse(), m.getName());
            }
        }
        return false;
    }
}
