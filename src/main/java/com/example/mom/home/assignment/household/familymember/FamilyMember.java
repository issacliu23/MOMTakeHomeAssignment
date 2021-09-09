package com.example.mom.home.assignment.household.familymember;

import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.validator.annotation.ValidateSpouseAnnotation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

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
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private FamilyMemberEnum.Gender gender;
    @NotNull
    private FamilyMemberEnum.MaritalStatus maritalStatus;
    private String spouse;
    @NotNull
    private FamilyMemberEnum.OccupationType occupationType;
    @NotNull
    private Integer annualIncome;

    @NotNull
    private LocalDate dob;
    @JsonIgnore
    @Transient
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id")
    @JsonIgnoreProperties("familyMemberList")
    private Household household;

    public FamilyMember() {}

    public FamilyMember(String name, FamilyMemberEnum.Gender gender, FamilyMemberEnum.MaritalStatus maritalStatus, String spouse, FamilyMemberEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouse = spouse;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dob = dob;
    }

    public FamilyMember(long id, String name, FamilyMemberEnum.Gender gender, FamilyMemberEnum.MaritalStatus maritalStatus, String spouse, FamilyMemberEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob, Household household) {
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

    public FamilyMember(FamilyMember another) {
        this.id = another.id;
        this.name = another.name;
        this.gender = another.gender;
        this.maritalStatus = another.maritalStatus;
        this.spouse = another.spouse;
        this.occupationType = another.occupationType;
        this.annualIncome = another.annualIncome;
        this.dob = another.dob;
        this.household = another.household;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FamilyMemberEnum.Gender getGender() {
        return gender;
    }

    public void setGender(FamilyMemberEnum.Gender gender) {
        this.gender = gender;
    }

    public FamilyMemberEnum.MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(FamilyMemberEnum.MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public FamilyMemberEnum.OccupationType getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(FamilyMemberEnum.OccupationType occupationType) {
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

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof FamilyMember))
            return false;

        FamilyMember f = (FamilyMember) obj;

        return Objects.equals(this.getId(), f.getId()) &&
                Objects.equals(this.getOccupationType(),(f.getOccupationType())) &&
                Objects.equals(this.getName(), f.getName()) &&
                Objects.equals(this.getAge(), f.getAge()) &&
                Objects.equals(this.getAnnualIncome(), f.getAnnualIncome()) &&
                Objects.equals(this.getDob(), f.getDob()) &&
                Objects.equals(this.getGender(), f.getGender()) &&
                Objects.equals(this.getSpouse(), f.getSpouse()) &&
                Objects.equals(this.getMaritalStatus(), f.getMaritalStatus());
    }
}
