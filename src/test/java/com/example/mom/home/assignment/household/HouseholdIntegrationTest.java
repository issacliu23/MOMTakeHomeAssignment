package com.example.mom.home.assignment.household;

import com.example.mom.home.assignment.constant.GrantSchemeConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.mom.home.assignment.MockData.*;
import static com.example.mom.home.assignment.shared.SharedMethods.asJsonString;
import static com.example.mom.home.assignment.shared.SharedMethods.getMapper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HouseholdIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    public void whenCreateHouseholdShouldReturnHouseholdWithId() throws Exception {
        Household request = getMockHousehold(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Household returnedHousehold = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertNotNull(returnedHousehold.getId());
    }

    @Transactional
    @Test
    public void whenGetAllHouseholdShouldReturnAllHouseholds() throws Exception {
        Household request1 = getMockHousehold(null);
        Household request2 = getMockHousehold(null);
        request2.setHousingType(HouseholdEnum.HousingType.Condominium);

        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request2))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/household"))
                .andExpect(status().isOk())
                .andReturn();

        List<HouseholdDTO> returnedHouseholds = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(returnedHouseholds.size(), 2);
        assertTrue(returnedHouseholds.get(0).equals(request1));
        assertTrue(returnedHouseholds.get(1).equals(request2));

    }

    @Transactional
    @Test
    public void whenGetHouseholdShouldReturnCorrectHousehold() throws Exception {
        Household request = getMockHousehold(null);

        MvcResult savedResult = mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Household savedHousehold = getMapper().readValue(savedResult.getResponse().getContentAsString(), new TypeReference<>(){});

        MvcResult result = mockMvc.perform(get("/household/{householdId}", savedHousehold.getId()))
                .andExpect(status().isOk())
                .andReturn();

        HouseholdDTO returnedHousehold = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertTrue(returnedHousehold.equals(request));
    }

    @Transactional
    @Test
    public void whenGetEligibleHouseholdsReturnCorrectResult() throws Exception {
        Household request1 = getMockStudentEncouragementBonusHousehold(null);
        Household request2 = getMockFamilyTogethernessSchemeHousehold(null);
        Household request3 = getMockElderBonusHousehold(null);
        Household request4 = getMockBabySunshineGrantHousehold(null);
        Household request5 = getMockYOLOGSTGrantHousehold(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request2))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request3))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request4))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request5))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/household/getGrantEligibleHouseholds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        GrantEligibleHouseholdDTO actualResponse = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<GrantEligibleHouseholdDTO>(){});
        if(actualResponse != null) {
            assertEquals(actualResponse.getGrantEligibleHouseholdMap().size(), 5);
            actualResponse.getGrantEligibleHouseholdMap().forEach((key,value) -> {
                if(value.size() > 0) {
                    if (key.equals(HouseholdEnum.Grant.StudentEncouragementBonus)) {
                        for (Household h : value) {
                            assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.StudentEncouragementBonusConstants.incomeCeilingLimit);
                            assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.StudentEncouragementBonusConstants.ageCeilingLimit));
                        }
                    } else if (key.equals(HouseholdEnum.Grant.FamilyTogethernessScheme)) {
                        for (Household h : value) {
                            assertTrue(h.hasMarriedCouple());
                            assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.FamilyTogethernessSchemeConstants.ageCeilingLimit));
                        }
                    } else if (key.equals(HouseholdEnum.Grant.ElderBonus)) {
                        for (Household h : value) {
                            assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() > GrantSchemeConstants.ElderBonusConstants.ageFloorLimit));
                        }
                    } else if (key.equals(HouseholdEnum.Grant.BabySunshineGrant)) {
                        for (Household h : value) {
                            assertTrue(h.getFamilyMemberList().stream().anyMatch(m -> m.getAge() < GrantSchemeConstants.BabySunshineGrantConstants.ageCeilingLimit));
                        }
                    } else if (key.equals(HouseholdEnum.Grant.YOLOGSTGrant)) {
                        for (Household h : value) {
                            assertTrue(h.getHouseholdIncome() < GrantSchemeConstants.YoloGstGrantConstants.incomeCeilingLimit);
                        }
                    } else
                        fail();
                }
                else
                    fail();
            });
        }
        else
            fail();
    }

}
