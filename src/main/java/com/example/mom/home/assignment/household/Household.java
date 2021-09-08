package com.example.mom.home.assignment.household;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

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
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj == this)
            return true;
        if (!(obj instanceof Household))
            return false;

        Household h = (Household) obj;

        if(Objects.equals(this.getId(),h.getId()) &&
                Objects.equals(this.getHousingType() ,h.getHousingType()) &&
                Objects.equals(this.getHouseholdIncome(),h.getHouseholdIncome()) &&
                this.getFamilyMemberList().size()==h.getFamilyMemberList().size()) {
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
