package com.example.mom.home.assignment.household;
import javax.persistence.*;
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
    @Column(name = "household_id")
    private long id;
    private HouseholdEnum.HousingType housingType;

    @OneToMany(mappedBy = "household")
    private List<FamilyMember> familyMemberList;

    public Household() {
    }

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

    public void setFamilyMemberList(List<FamilyMember> familyMemberList) {
        this.familyMemberList = familyMemberList;
    }
}
