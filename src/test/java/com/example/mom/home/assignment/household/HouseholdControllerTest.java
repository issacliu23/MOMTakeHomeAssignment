package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.household.familymember.FamilyMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mom.home.assignment.household.familymember.FamilyMemberMockData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
