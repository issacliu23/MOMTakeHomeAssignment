package com.example.mom.home.assignment.household.familymember;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class FamilyMemberDTO {
    private String name;
    private FamilyMemberEnum.Gender gender;
    private FamilyMemberEnum.MaritalStatus maritalStatus;
    private String spouse;
    private FamilyMemberEnum.OccupationType occupationType;
    private Integer annualIncome;
    private LocalDate dob;

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
}
