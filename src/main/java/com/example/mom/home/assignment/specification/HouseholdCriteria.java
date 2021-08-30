package com.example.mom.home.assignment.specification;

import com.example.mom.home.assignment.household.HouseholdEnum;

public class HouseholdCriteria {
    private Integer householdSize;
    private Integer householdIncome;
    private HouseholdEnum.HousingType housingType;

    public HouseholdCriteria() {
    }

    public HouseholdCriteria(Integer householdSize, Integer householdIncome, HouseholdEnum.HousingType housingType) {
        this.householdSize = householdSize;
        this.householdIncome = householdIncome;
        this.housingType = housingType;
    }

    public void setHouseholdSize(Integer householdSize) {
        this.householdSize = householdSize;
    }

    public void setHouseholdIncome(Integer householdIncome) {
        this.householdIncome = householdIncome;
    }

    public void setHousingType(HouseholdEnum.HousingType housingType) {
        this.housingType = housingType;
    }

    public Integer getHouseholdSize() {
        return householdSize;
    }

    public Integer getHouseholdIncome() {
        return householdIncome;
    }

    public HouseholdEnum.HousingType getHousingType() {
        return housingType;
    }
}
