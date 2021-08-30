package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.constant.GrantSchemeConstants;
import com.example.mom.home.assignment.customexception.ResourceNotFoundException;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.example.mom.home.assignment.household.familymember.FamilyMemberEnum;
import com.example.mom.home.assignment.specification.HouseholdCriteria;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.mom.home.assignment.MockData.*;
import static com.example.mom.home.assignment.shared.SharedMethods.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseholdController.class)
public class HouseholdControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HouseholdService householdService;

    //region createHousehold API
    @Test
    public void whenCreateHouseholdShouldReturnHouseholdWithId() throws Exception {
        Household request = getMockHousehold(null);
        Long id = 1L;
        Household response = getMockHousehold(id);
        when(householdService.createHousehold(any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void whenCreateHouseholdAndServiceReturnNullShouldReturnInternalServerError() throws Exception {
        Household request = getMockHousehold(null);
        when(householdService.createHousehold(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void whenCreateHouseholdWithNullHouseholdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdWithNoFamilyMembersShouldReturnBadRequest() throws Exception {
        Household request = getEmptyHousehold(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdWithNullFamilyMemberListShouldReturnBadRequest() throws Exception {
        Household request = new Household(HouseholdEnum.HousingType.HDB, null);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdWithSpouseAndWrongMaritalStatusShouldReturnBadRequest() throws Exception {
        FamilyMember mockMember = getMockFamilyMember();
        mockMember.setSpouse("spouse");
        mockMember.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Single);
        Household request = getMockHousehold(null);
        request.addFamilyMember(mockMember);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdWithoutSpouseAndWrongMaritalStatusShouldReturnBadRequest() throws Exception {
        FamilyMember mockMember = getMockFamilyMember();
        mockMember.setSpouse(null);
        mockMember.setMaritalStatus(FamilyMemberEnum.MaritalStatus.Married);
        Household request = getMockHousehold(null);
        request.addFamilyMember(mockMember);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdAndServiceThrowConstraintViolationExceptionShouldReturnBadRequest() throws Exception {
        Household request = getMockHousehold(null);
        when(householdService.createHousehold(any())).thenThrow(ConstraintViolationException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //endregion
    //region getAllHouseholds API
    @Test
    public void whenGetAllHouseholdsShouldReturnHouseholdDTOList() throws Exception {
        HouseholdEnum.HousingType house1type = HouseholdEnum.HousingType.HDB;
        HouseholdEnum.HousingType house2type = HouseholdEnum.HousingType.Condominium;

        List<Household> mockHouseholdList = new ArrayList<Household>();
        Household hdb = getEmptyHousehold(1L);
        hdb.setHousingType(house1type);
        FamilyMember h1member1 = getMockFamilyMember();
        h1member1.setName("House 1 member 1");
        hdb.addFamilyMember(h1member1);

        Household condo = getEmptyHousehold(1L);
        condo.setHousingType(HouseholdEnum.HousingType.Condominium);
        FamilyMember h2member1 = getMockFamilyMember();
        h2member1.setName("House 2 member 1");
        FamilyMember h2member2 = getMockFamilyMember();
        h2member2.setName("House 2 member 2");
        condo.addFamilyMember(h2member1);
        condo.addFamilyMember(h2member2);

        mockHouseholdList.add(hdb);
        mockHouseholdList.add(condo);
        when(householdService.getAllHouseholds()).thenReturn(mockHouseholdList);
        MvcResult result = mockMvc.perform(get("/household"))
                .andExpect(status().isOk())
                .andReturn();

        List<HouseholdDTO> returnedHouseholds = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<HouseholdDTO>>(){});
        assertEquals(returnedHouseholds.size(), 2);
        assertEquals(returnedHouseholds.get(0).getHousingType(), house1type);
        assertEquals(returnedHouseholds.get(0).getFamilyMemberList().size(), 1);
        assertEquals(returnedHouseholds.get(1).getHousingType(), house2type);
        assertEquals(returnedHouseholds.get(1).getFamilyMemberList().size(), 2);
    }
    @Test
    public void whenGetAllHouseholdsHaveNoDataShouldReturnEmptyList() throws Exception {
        List<Household> mockHouseholds = new ArrayList<Household>();
        when(householdService.getAllHouseholds()).thenReturn(mockHouseholds);
        mockMvc.perform(get("/household"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    @Test
    public void whenGetAllHouseholdsAndServiceReturnNullShouldReturnInternalServerError() throws Exception {
        when(householdService.getAllHouseholds()).thenReturn(null);
        mockMvc.perform(get("/household"))
                .andExpect(status().isInternalServerError());
    }
    //endregion
    //region getHousehold API
    @Test
    public void whenGetHouseholdShouldReturnHouseholdWithFamilyMembers() throws Exception {
        Long householdId = 1L;
        Household mockHousehold = getMockHousehold(householdId);
        when(householdService.getHousehold(householdId)).thenReturn(mockHousehold);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/household/{householdId}",householdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        HouseholdDTO response = getMapper().readValue(result.getResponse().getContentAsString(), HouseholdDTO.class);
        assertEquals(mockHousehold.getHousingType(), response.getHousingType());
        assertEquals(mockHousehold.getFamilyMemberList().size(), response.getFamilyMemberList().size());
        assertEquals(mockHousehold.getFamilyMemberList().get(0).getName(), response.getFamilyMemberList().get(0).getName());
    }
    @Test
    public void whenGetHouseholdNotFoundShouldReturnNotFound() throws Exception {
        Long householdId = 1L;
        when(householdService.getHousehold(householdId)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/household/{householdId}",householdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetHouseholdAndServiceReturnNullShouldReturnInternalServerError() throws Exception {
        Long householdId = 1L;
        when(householdService.getHousehold(householdId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/household/{householdId}",householdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
    //endregion

    //region getGrantEligibleHouseholds API
    @Test
    public void whenGetGrantEligibleHouseholdsWithoutParamsShouldReturnResult() throws Exception {
        GrantEligibleHouseholdDTO mockResponse = getMockGrantEligibleHouseholds();
        when(householdService.getGrantEligibleHouseholds(any())).thenReturn(mockResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/household/getGrantEligibleHouseholds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GrantEligibleHouseholdDTO actualResponse = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<GrantEligibleHouseholdDTO>(){});
        actualResponse.getGrantEligibleHouseholdMap().forEach((key, value) -> {
            if(key.equals(HouseholdEnum.Grant.StudentEncouragementBonus)) {
                for(Household h: value) {
                    assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit);
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.FamilyTogethernessScheme)) {
                for(Household h: value) {
                    assertTrue(h.hasMarriedCouple());
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.ElderBonus)) {
                for(Household h: value) {
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() > GrantSchemeConstants.ElderBonusConstants.ageFloorLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.BabySunshineGrant)) {
                for(Household h: value) {
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.YOLOGSTGrant)) {
                for(Household h: value) {
                    assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
                }
            }
            else {
                fail();
            }
        });
    }
    @Test
    public void whenGetGrantEligibleHouseholdsWithParamsShouldReturnFilteredResult() throws Exception {
        int criteriaIncome = GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit-1;
        int criteriaSize = 1;
        HouseholdEnum.HousingType criteriaHousingType = HouseholdEnum.HousingType.HDB;
        HouseholdCriteria criteria = new HouseholdCriteria(criteriaSize,criteriaIncome,criteriaHousingType);
        GrantEligibleHouseholdDTO mockResponse = getMockGrantEligibleHouseholds();
        Predicate<Household> incomePredicate = h -> h.getHouseholdIncome() == criteriaIncome;
        Predicate<Household> sizePredicate = h -> h.getFamilyMemberList().size() == criteriaSize;
        Predicate<Household> housingTypePredicate = h -> h.getHousingType() == criteriaHousingType;
        mockResponse.getGrantEligibleHouseholdMap().forEach((key,value)-> {
            mockResponse.getGrantEligibleHouseholdMap().put(key,value.stream().filter(incomePredicate.and(sizePredicate.and(housingTypePredicate))).collect(Collectors.toList()));
        });
        when(householdService.getGrantEligibleHouseholds(any())).thenReturn(mockResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/household/getGrantEligibleHouseholds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        GrantEligibleHouseholdDTO actualResponse = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<GrantEligibleHouseholdDTO>(){});
        actualResponse.getGrantEligibleHouseholdMap().forEach((key, value) -> {
            if(key.equals(HouseholdEnum.Grant.StudentEncouragementBonus)) {
                for(Household h: value) {
                    assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                    assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                    assertEquals(h.getHousingType(), criteriaHousingType);
                    assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit);
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.FamilyTogethernessScheme)) {
                for(Household h: value) {
                    assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                    assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                    assertEquals(h.getHousingType(), criteriaHousingType);

                    assertTrue(h.hasMarriedCouple());
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.ElderBonus)) {
                for(Household h: value) {
                    assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                    assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                    assertEquals(h.getHousingType(), criteriaHousingType);

                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() > GrantSchemeConstants.ElderBonusConstants.ageFloorLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.BabySunshineGrant)) {
                for(Household h: value) {
                    assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                    assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                    assertEquals(h.getHousingType(), criteriaHousingType);
                    assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit));
                }
            }
            else if(key.equals(HouseholdEnum.Grant.YOLOGSTGrant)) {
                for(Household h: value) {
                    assertEquals((int) h.getHouseholdIncome(), criteriaIncome);
                    assertEquals(h.getFamilyMemberList().size(), criteriaSize);
                    assertEquals(h.getHousingType(), criteriaHousingType);
                    assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
                }
            }
            else {
                fail();
            }
        });
    }



}
