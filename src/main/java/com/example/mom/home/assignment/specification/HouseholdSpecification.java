package com.example.mom.home.assignment.specification;

import com.example.mom.home.assignment.household.Household;
import com.example.mom.home.assignment.household.Household_;
import com.example.mom.home.assignment.constant.GrantSchemeConstants;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMember_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HouseholdSpecification {
    public static Specification<Household> studentEncouragementBonusSpecification(HouseholdCriteria criteria) {
        return Specification.where(
                (root, query, builder) -> {
                    final List<Predicate> wherePredicates = new ArrayList<>();
                    final List<Predicate> havingPredicates = new ArrayList<>();
                    final Join<Household, FamilyMember> householdFamJoin = root.join(Household_.familyMemberList, JoinType.LEFT);
                    if (criteria != null) {
                        if (criteria.getHousingType() != null) {
                            wherePredicates.add(builder.equal(root.get(Household_.housingType), criteria.getHousingType()));
                        }
                        if (criteria.getHouseholdIncome() != null) {
                            havingPredicates.add(builder.equal(
                                    builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)),
                                    criteria.getHouseholdIncome()));
                        }
                        if (criteria.getHouseholdSize() != null) {
                            havingPredicates.add(builder.equal(
                                    builder.count(householdFamJoin),
                                    criteria.getHouseholdSize()));
                        }
                    }
                    Subquery<FamilyMember> subQuery = query.subquery(FamilyMember.class);
                    Root<FamilyMember> subQueryRoot = subQuery.from(FamilyMember.class);
                    subQuery.select(subQueryRoot);
                    Expression<Integer> age = builder.diff(LocalDate.now().getYear(), builder.function("YEAR", Integer.class, subQueryRoot.get(FamilyMember_.dob)));
                    Predicate agePredicate = builder.lt(age,GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit);
                    Predicate idPredicate = builder.equal(subQueryRoot.get(FamilyMember_.household), root);
                    subQuery.where(agePredicate, idPredicate);
                    wherePredicates.add(builder.exists(subQuery));
                    query.groupBy(root.get(Household_.id));
                    havingPredicates.add(builder.lt(builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)), GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit));
                    query.having(havingPredicates.toArray(new Predicate[0]));
                    query.multiselect().distinct(true);
                    return builder.and(wherePredicates.toArray(new Predicate[0]));
                }
        );
    }
    public static Specification<Household> familyTogethernessSpecification(HouseholdCriteria criteria) {
        return Specification.where(
                (root, query, builder) -> {
                    final List<Predicate> wherePredicates = new ArrayList<>();
                    final List<Predicate> havingPredicates = new ArrayList<>();
                    if (criteria != null) {
                        if (criteria.getHousingType() != null) {
                            wherePredicates.add(builder.equal(root.get(Household_.housingType), criteria.getHousingType()));
                        }
                        if(criteria.getHouseholdIncome() != null || criteria.getHouseholdSize() != null) {
                            final Join<Household, FamilyMember> householdFamJoin = root.join(Household_.familyMemberList, JoinType.LEFT);
                            if (criteria.getHouseholdIncome() != null) {
                                havingPredicates.add(builder.equal(
                                        builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)),
                                        criteria.getHouseholdIncome()));

                            }
                            if (criteria.getHouseholdSize() != null) {
                                havingPredicates.add(builder.equal(
                                        builder.count(householdFamJoin),
                                        criteria.getHouseholdSize()));
                            }
                            query.groupBy(root.get(Household_.id));
                            query.having(havingPredicates.toArray(new Predicate[0]));
                        }
                    }
                    Subquery<FamilyMember> subQuery = query.subquery(FamilyMember.class);
                    Root<FamilyMember> subQueryRoot = subQuery.from(FamilyMember.class);
                    subQuery.select(subQueryRoot);
                    Expression<Integer> age = builder.diff(LocalDate.now().getYear(), builder.function("YEAR", Integer.class, subQueryRoot.get(FamilyMember_.dob)));
                    Predicate agePredicate = builder.and(builder.lt(age, GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
                    subQuery.where(agePredicate, builder.equal(subQueryRoot.get(FamilyMember_.household),root));
                    wherePredicates.add(builder.exists(subQuery));

                    Subquery<FamilyMember> subQuery2 = query.subquery(FamilyMember.class);
                    Root<FamilyMember> subQueryRoot2 = subQuery2.from(FamilyMember.class);
                    subQuery2.select(subQueryRoot2);

                    Subquery<String> getSpouseSubQuery = query.subquery(String.class);
                    Root<FamilyMember> spouseSubRoot = getSpouseSubQuery.from(FamilyMember.class);
                    getSpouseSubQuery.select(spouseSubRoot.get(FamilyMember_.spouse));
                    getSpouseSubQuery.where(builder.equal(spouseSubRoot.get(FamilyMember_.household),root));
                    Predicate spousePredicate = builder.in(subQueryRoot2.get(FamilyMember_.name)).value(getSpouseSubQuery);

                    subQuery2.where(spousePredicate, builder.equal(subQueryRoot2.get(FamilyMember_.household),root));
                    wherePredicates.add(builder.exists(subQuery2));

                    query.multiselect().distinct(true);
                    return builder.and(wherePredicates.toArray(new Predicate[0]));
                }
        );
    }
    public static Specification<Household> elderBonusSpecification(HouseholdCriteria criteria) {
        return Specification.where(
                (root, query, builder) -> {
                    final List<Predicate> wherePredicates = new ArrayList<>();
                    final List<Predicate> havingPredicates = new ArrayList<>();
                    if (criteria != null) {
                        if (criteria.getHousingType() != null) {
                            wherePredicates.add(builder.equal(root.get(Household_.housingType), criteria.getHousingType()));
                        }
                        if(criteria.getHouseholdIncome() != null || criteria.getHouseholdSize() != null)
                        {
                            final Join<Household, FamilyMember> householdFamJoin = root.join(Household_.familyMemberList, JoinType.LEFT);
                            if (criteria.getHouseholdIncome() != null) {
                                havingPredicates.add(builder.equal(
                                        builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)),
                                        criteria.getHouseholdIncome()));

                            }
                            if (criteria.getHouseholdSize() != null) {
                                havingPredicates.add(builder.equal(
                                        builder.count(householdFamJoin),
                                        criteria.getHouseholdSize()));
                            }
                            query.groupBy(root.get(Household_.id));
                            query.having(havingPredicates.toArray(new Predicate[0]));
                        }
                    }
                    Subquery<FamilyMember> subQuery = query.subquery(FamilyMember.class);
                    Root<FamilyMember> subQueryRoot = subQuery.from(FamilyMember.class);
                    Expression<Integer> age = builder.diff(LocalDate.now().getYear(), builder.function("YEAR", Integer.class, subQueryRoot.get(FamilyMember_.dob)));
                    Predicate agePredicate = builder.gt(age, GrantSchemeConstants.ElderBonusConstants.ageFloorLimit);
                    Predicate idPredicate = builder.equal(subQueryRoot.get(FamilyMember_.household),root);
                    subQuery.where(agePredicate, idPredicate);
                    subQuery.select(subQueryRoot);
                    wherePredicates.add(builder.exists(subQuery));

                    query.multiselect().distinct(true);
                    return builder.and(wherePredicates.toArray(new Predicate[0]));
                }
        );
    }

    public static Specification<Household> babySunshineGrantSpecification(HouseholdCriteria criteria) {
        return Specification.where(
                (root, query, builder) -> {
                    final List<Predicate> wherePredicates = new ArrayList<>();
                    final List<Predicate> havingPredicates = new ArrayList<>();
                    if (criteria != null) {
                        if (criteria.getHousingType() != null) {
                            wherePredicates.add(builder.equal(root.get(Household_.housingType), criteria.getHousingType()));
                        }
                        if(criteria.getHouseholdIncome() != null || criteria.getHouseholdSize() != null) {
                            final Join<Household, FamilyMember> householdFamJoin = root.join(Household_.familyMemberList, JoinType.LEFT);
                            if (criteria.getHouseholdIncome() != null) {
                            havingPredicates.add(builder.equal(
                                    builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)),
                                    criteria.getHouseholdIncome()));

                            }
                            if (criteria.getHouseholdSize() != null) {
                                havingPredicates.add(builder.equal(
                                        builder.count(householdFamJoin),
                                        criteria.getHouseholdSize()));
                            }
                            query.groupBy(root.get(Household_.id));
                            query.having(havingPredicates.toArray(new Predicate[0]));
                        }
                    }
                    Subquery<FamilyMember> subQuery = query.subquery(FamilyMember.class);
                    Root<FamilyMember> subQueryRoot = subQuery.from(FamilyMember.class);
                    subQuery.select(subQueryRoot);
                    Expression<Integer> age = builder.diff(LocalDate.now().getYear(), builder.function("YEAR", Integer.class, subQueryRoot.get(FamilyMember_.dob)));
                    Predicate agePredicate = builder.lt(age, GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit);
                    Predicate idPredicate = builder.equal(subQueryRoot.get(FamilyMember_.household),root);
                    subQuery.where(agePredicate, idPredicate);
                    wherePredicates.add(builder.exists(subQuery));

                    query.multiselect().distinct(true);
                    return builder.and(wherePredicates.toArray(new Predicate[0]));
                }
        );
    }

    public static Specification<Household> yoloGstGrantSpecification(HouseholdCriteria criteria) {
        return Specification.where(
            (root, query, builder) -> {
                final List<Predicate> wherePredicates = new ArrayList<>();
                final List<Predicate> havingPredicates = new ArrayList<>();
                final Join<Household, FamilyMember> householdFamJoin = root.join(Household_.familyMemberList, JoinType.LEFT);
                if (criteria != null) {
                    if (criteria.getHousingType() != null) {
                        wherePredicates.add(builder.equal(root.get(Household_.housingType), criteria.getHousingType()));
                    }
                    if (criteria.getHouseholdIncome() != null) {
                        havingPredicates.add(builder.equal(
                                builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)),
                                criteria.getHouseholdIncome()));

                    }
                    if (criteria.getHouseholdSize() != null) {
                        havingPredicates.add(builder.equal(
                                builder.count(householdFamJoin),
                                criteria.getHouseholdSize()));
                    }
                }
                query.groupBy(root.get(Household_.id));
                havingPredicates.add(builder.lt(builder.sum(householdFamJoin.get(FamilyMember_.annualIncome)), GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit));
                query.having(havingPredicates.toArray(new Predicate[0]));
                query.multiselect().distinct(true);
                return builder.and(wherePredicates.toArray(new Predicate[0]));
            }
        );
    }
}
