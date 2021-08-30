package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.specification.HouseholdCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long>, JpaSpecificationExecutor<Household> {
//    @Query("SELECT DISTINCT h " +
//            "FROM Household h LEFT JOIN h.familyMemberList m " +
//            "WHERE (:#{#criteria.getHousingType()} IS NULL OR h.housingType = :#{#criteria.getHousingType()}) " +
//            "AND EXISTS(SELECT 1 FROM FamilyMember m WHERE m.household = h.id AND EXTRACT(YEAR from CURRENT_DATE) - EXTRACT(YEAR from m.dob) < 16) " +
//            "GROUP BY h.id " +
//            "HAVING SUM(m.annualIncome) < 150000 " +
//            "AND (:#{#criteria.getHouseholdSize()} IS NULL OR COUNT(m) = :#{#criteria.getHouseholdSize()}) " +
//            "AND (:#{#criteria.getHouseholdIncome()} IS NULL OR SUM(m.annualIncome) = :#{#criteria.getHouseholdIncome()})")
//    List<Household> findStudentEncouragementBonusEligibleHouseholds(@Param("criteria") HouseholdCriteria criteria);
//
////    List<Household> findFamilyTogethernessSchemeEligibleHouseholds(@Param("criteria") HouseholdCriteria criteria);
////
//    @Query("SELECT DISTINCT h " +
//            "FROM Household h LEFT JOIN h.familyMemberList m " +
//            "WHERE (:#{#criteria.getHousingType()} IS NULL OR h.housingType = :#{#criteria.getHousingType()}) " +
//            "AND EXISTS(SELECT 1 FROM FamilyMember m WHERE m.household = h.id AND EXTRACT(YEAR from CURRENT_DATE) - EXTRACT(YEAR from m.dob) > 50) " +
//            "GROUP BY h.id " +
//            "HAVING (:#{#criteria.getHouseholdSize()} IS NULL OR COUNT(m) = :#{#criteria.getHouseholdSize()}) " +
//            "AND (:#{#criteria.getHouseholdIncome()} IS NULL OR SUM(m.annualIncome) = :#{#criteria.getHouseholdIncome()})")
//    List<Household> findElderBonusEligibleHouseholds(@Param("criteria") HouseholdCriteria criteria);
//
//    @Query("SELECT DISTINCT h " +
//            "FROM Household h LEFT JOIN h.familyMemberList m " +
//            "WHERE (:#{#criteria.getHousingType()} IS NULL OR h.housingType = :#{#criteria.getHousingType()}) " +
//            "AND EXISTS(SELECT 1 FROM FamilyMember m WHERE m.household = h.id AND EXTRACT(YEAR from CURRENT_DATE) - EXTRACT(YEAR from m.dob) < 5) " +
//            "GROUP BY h.id " +
//            "HAVING (:#{#criteria.getHouseholdSize()} IS NULL OR COUNT(m) = :#{#criteria.getHouseholdSize()}) " +
//            "AND (:#{#criteria.getHouseholdIncome()} IS NULL OR SUM(m.annualIncome) = :#{#criteria.getHouseholdIncome()})")
//
//    List<Household> findBabySunshineGrantEligibleHouseholds(@Param("criteria") HouseholdCriteria criteria);
//
//    @Query("SELECT DISTINCT h, SUM(m.annualIncome) " +
//            "FROM Household h LEFT JOIN h.familyMemberList m " +
//            "WHERE (:#{#criteria.getHousingType()} IS NULL OR h.housingType = :#{#criteria.getHousingType()}) " +
//            "GROUP BY h.id " +
//            "HAVING SUM(m.annualIncome) < 100000 " +
//            "AND (:#{#criteria.getHouseholdSize()} IS NULL OR COUNT(m) = :#{#criteria.getHouseholdSize()}) " +
//            "AND (:#{#criteria.getHouseholdIncome()} IS NULL OR SUM(m.annualIncome) = :#{#criteria.getHouseholdIncome()})")
//    List<Household> findYOLOGSTGrantEligibleHouseholds(@Param("criteria") HouseholdCriteria criteria);

}
