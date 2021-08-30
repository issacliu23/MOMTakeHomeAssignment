package com.example.mom.home.assignment.household.familymember;

import com.example.mom.home.assignment.customexception.ResourceNotFoundException;
import com.example.mom.home.assignment.household.HouseholdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.example.mom.home.assignment.household.familymember.FamilyMemberMockData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FamilyMemberController.class)
public class FamilyMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HouseholdService householdService;
    //region addFamilyMember API Test
    @Test
    public void whenAddFamilyMemberShouldReturnFamilyMemberWithId() throws Exception {
        FamilyMember request = getValidFamilyMember();
        FamilyMember response = new FamilyMember(request);
        Long id = 1L;
        response.setId(id);
        when(householdService.addFamilyMember(any(),any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void whenAddFamilyMemberWithNullFamilyMemberShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAddFamilyMemberWithSpouseButWrongMaritalStatusShouldReturnBadRequest() throws Exception {
        FamilyMember invalidMember = getWrongMaritalStatusWithSpouseFamilyMember();
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidMember))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenAddFamilyMemberWithoutSpouseButWrongMaritalStatusShouldReturnBadRequest() throws Exception {
        FamilyMember invalidMember = getWrongMaritalStatusWithoutSpouseFamilyMember();
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidMember))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAddFamilyMemberAndServiceReturnHouseholdNotFoundShouldReturnNotFound() throws Exception {
        FamilyMember request = getValidFamilyMember();
        Optional<FamilyMember> response = Optional.empty();
        when(householdService.addFamilyMember(any(),any())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenAddFamilyMemberAndServiceReturnNullShouldReturnInternalServerError() throws Exception {
        FamilyMember request = getValidFamilyMember();
        Optional<FamilyMember> response = Optional.empty();
        when(householdService.addFamilyMember(any(),any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
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
