package com.example.mom.home.assignment.household;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GrantEligibleHouseholdDTO {

    private final Map<HouseholdEnum.Grant, List<Household>> grantEligibleHouseholdMap = new LinkedHashMap<>();

    public GrantEligibleHouseholdDTO() {
        init();
    }

    private void init() {
        grantEligibleHouseholdMap.put(HouseholdEnum.Grant.StudentEncouragementBonus, new ArrayList<>());
        grantEligibleHouseholdMap.put(HouseholdEnum.Grant.FamilyTogethernessScheme, new ArrayList<>());
        grantEligibleHouseholdMap.put(HouseholdEnum.Grant.ElderBonus, new ArrayList<>());
        grantEligibleHouseholdMap.put(HouseholdEnum.Grant.BabySunshineGrant, new ArrayList<>());
        grantEligibleHouseholdMap.put(HouseholdEnum.Grant.YOLOGSTGrant, new ArrayList<>());
    }

    public Map<HouseholdEnum.Grant, List<Household>> getGrantEligibleHouseholdMap() {
        return grantEligibleHouseholdMap;
    }
}
