package com.example.mom.home.assignment.household;
import com.example.mom.home.assignment.familymember.FamilyMember;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

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
    private HouseholdEnum.HousingType housingType;

    @NotEmpty(message="There must be at least 1 family member in the household")
    @Valid
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("household")
    private List<FamilyMember> familyMemberList = new ArrayList<>();

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

}
