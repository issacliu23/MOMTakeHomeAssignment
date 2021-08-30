package com.example.mom.home.assignment.familymember;

import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.household.HouseholdEnum;
import com.example.mom.home.assignment.validator.ValidateSpouseAnnotation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table
@ValidateSpouseAnnotation
public class FamilyMember {
    @Id
    @SequenceGenerator(
            name = "family_member_sequence",
            sequenceName = "family_member_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "family_member_sequence"
    )
    private long id;

    @NotNull
    private String name;
    @NotNull
    private HouseholdEnum.Gender gender;
    @NotNull
    private HouseholdEnum.MaritalStatus maritalStatus;
    private String spouse;
    @NotNull
    private HouseholdEnum.OccupationType occupationType;
    @NotNull
    private Integer annualIncome;
    @NotNull
    private LocalDate dob;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id")
    @JsonIgnoreProperties("familyMemberList")
    private Household household;

    public FamilyMember() {}

    public FamilyMember(String name, HouseholdEnum.Gender gender, HouseholdEnum.MaritalStatus maritalStatus, String spouse, HouseholdEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouse = spouse;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dob = dob;
    }

    public FamilyMember(long id, String name, HouseholdEnum.Gender gender, HouseholdEnum.MaritalStatus maritalStatus, String spouse, HouseholdEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob, Household household) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouse = spouse;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dob = dob;
        this.household = household;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HouseholdEnum.Gender getGender() {
        return gender;
    }

    public void setGender(HouseholdEnum.Gender gender) {
        this.gender = gender;
    }

    public HouseholdEnum.MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(HouseholdEnum.MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public HouseholdEnum.OccupationType getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(HouseholdEnum.OccupationType occupationType) {
        this.occupationType = occupationType;
    }

    public Integer getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Integer annualIncome) {
        this.annualIncome = annualIncome;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

}
