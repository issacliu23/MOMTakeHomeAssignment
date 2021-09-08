package com.example.mom.home.assignment.household.familymember;

import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.household.HouseholdDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class FamilyMemberDTO {
    private String name;
    private FamilyMemberEnum.Gender gender;
    private FamilyMemberEnum.MaritalStatus maritalStatus;
    private String spouse;
    private FamilyMemberEnum.OccupationType occupationType;
    private Integer annualIncome;
    private LocalDate dob;
    @JsonIgnore
    private Integer age;
    public FamilyMemberDTO() {
    }

    public FamilyMemberDTO(FamilyMember member) {
        this(member.getName(), member.getGender(),member.getMaritalStatus(),member.getSpouse(),member.getOccupationType(),member.getAnnualIncome(),member.getDob());
    }

    public FamilyMemberDTO(String name, FamilyMemberEnum.Gender gender, FamilyMemberEnum.MaritalStatus maritalStatus, String spouse, FamilyMemberEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouse = spouse;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public FamilyMemberEnum.Gender getGender() {
        return gender;
    }

    public FamilyMemberEnum.MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public String getSpouse() {
        return spouse;
    }

    public FamilyMemberEnum.OccupationType getOccupationType() {
        return occupationType;
    }

    public Integer getAnnualIncome() {
        return annualIncome;
    }

    public LocalDate getDob() {
        return dob;
    }
    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof FamilyMemberDTO) && !(obj instanceof FamilyMember))
            return false;

        FamilyMemberDTO f = null;
        if(obj instanceof FamilyMemberDTO)
            f = (FamilyMemberDTO) obj;
        if(obj instanceof FamilyMember)
            f = new FamilyMemberDTO((FamilyMember) obj);
        
        return Objects.equals(this.getOccupationType(),(f.getOccupationType())) &&
                Objects.equals(this.getName(), f.getName()) &&
                Objects.equals(this.getAge(), f.getAge()) &&
                Objects.equals(this.getAnnualIncome(), f.getAnnualIncome()) &&
                Objects.equals(this.getDob(), f.getDob()) &&
                Objects.equals(this.getGender(), f.getGender()) &&
                Objects.equals(this.getSpouse(), f.getSpouse()) &&
                Objects.equals(this.getMaritalStatus(), f.getMaritalStatus());
    }

}
