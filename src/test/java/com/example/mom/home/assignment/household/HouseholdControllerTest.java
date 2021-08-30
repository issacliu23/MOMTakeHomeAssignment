package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.customexception.ResourceNotFoundException;
import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static com.example.mom.home.assignment.household.familymember.FamilyMemberMockData.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    //region createHousehold API Test
    @Test
    public void whenCreateHouseholdShouldReturnHouseholdWithId() throws Exception {
        HouseholdEnum.HousingType housingType = HouseholdEnum.HousingType.HDB;
        List<FamilyMember> familyMemberList = new ArrayList<FamilyMember>(List.of(getValidFamilyMember()));
        Household request = new Household(housingType, familyMemberList);
        Long id = 1L;
        Household response = new Household(id, housingType, familyMemberList);
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
        HouseholdEnum.HousingType housingType = HouseholdEnum.HousingType.HDB;
        List<FamilyMember> familyMemberList = new ArrayList<FamilyMember>(List.of(getValidFamilyMember()));
        Household request = new Household(housingType, familyMemberList);
        Long id = 1L;
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
        Household request = new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>());
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
        Household request = new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getWrongMaritalStatusWithSpouseFamilyMember())));
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdWithoutSpouseAndWrongMaritalStatusShouldReturnBadRequest() throws Exception {
        Household request = new Household(HouseholdEnum.HousingType.HDB, new ArrayList<FamilyMember>(List.of(getWrongMaritalStatusWithoutSpouseFamilyMember())));
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateHouseholdAndServiceThrowConstraintViolationExceptionShouldReturnBadRequest() throws Exception {
        HouseholdEnum.HousingType housingType = HouseholdEnum.HousingType.HDB;
        List<FamilyMember> familyMemberList = new ArrayList<FamilyMember>(List.of(getValidFamilyMember()));
        Household request = new Household(housingType, familyMemberList);
        Long id = 1L;
        when(householdService.createHousehold(any())).thenThrow(ConstraintViolationException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //endregion

    //region getAllHouseholds API Test
    @Test
    public void whenGetAllHouseholdsShouldReturnHouseholdDTOList() throws Exception {
        HouseholdEnum.HousingType house1type = HouseholdEnum.HousingType.HDB;
        HouseholdEnum.HousingType house2type = HouseholdEnum.HousingType.Condominium;

        List<Household> mockHouseholds = new ArrayList<Household>();
        FamilyMember member1 = getValidFamilyMember("House 1 member 1");
        FamilyMember member2 = getValidFamilyMember("House 2 member 1");
        FamilyMember member3 = getValidFamilyMember("House 2 member 2");

        mockHouseholds.add(new Household(house1type, new ArrayList<FamilyMember>(List.of(member1))));
        mockHouseholds.add(new Household(house2type, new ArrayList<FamilyMember>(List.of(member2, member3))));
        when(householdService.getAllHouseholds()).thenReturn(mockHouseholds);
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
    //region getHousehold API Test
    @Test
    public void whenGetHouseholdShouldReturnHouseholdWithFamilyMembers() throws Exception {
        Long householdId = 1L;
        Household mockHousehold = new Household(householdId, HouseholdEnum.HousingType.HDB,new ArrayList<FamilyMember>(List.of(getValidFamilyMember())));
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

    private static String asJsonString(final Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


}
