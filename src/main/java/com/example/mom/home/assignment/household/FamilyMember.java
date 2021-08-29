package com.example.mom.home.assignment.household;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
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
    @Column(name = "family_id")
    private long id;
    private String name;
    private HouseholdEnum.Gender gender;
    private HouseholdEnum.MaritalStatus maritalStatus;
    private String spouse;
    private HouseholdEnum.OccupationType occupationType;
    private Integer annualIncome;
    private LocalDate dob;

    @ManyToOne
    @JoinColumn(name="household_id", nullable = false)
    private Household household;

    public FamilyMember() {
    }

    public FamilyMember(String name, HouseholdEnum.Gender gender, HouseholdEnum.MaritalStatus maritalStatus, String spouse, HouseholdEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob, Household household) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouse = spouse;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dob = dob;
        this.household = household;
    }

    public FamilyMember(long id, String name, HouseholdEnum.Gender gender, HouseholdEnum.MaritalStatus maritalStatus, String spouse, HouseholdEnum.OccupationType occupationType, Integer annualIncome, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouse = spouse;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dob = dob;
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
