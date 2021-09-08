package com.example.mom.home.assignment.household.familymember;
import com.example.mom.home.assignment.household.Household;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mom.home.assignment.MockData.getMockFamilyMember;
import static com.example.mom.home.assignment.MockData.getMockHousehold;
import static com.example.mom.home.assignment.shared.SharedMethods.asJsonString;
import static com.example.mom.home.assignment.shared.SharedMethods.getMapper;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FamilyMemberIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    public void whenAddFamilyMemberShouldReturnMemberWithId() throws Exception {
        Household mockHousehold = getMockHousehold(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockHousehold))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Long id = (long) mockHousehold.getFamilyMemberList().size()+1;

        Household household = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<Household>(){});
        FamilyMember request = getMockFamilyMember();
        mockMvc.perform(MockMvcRequestBuilders.post("/household/{householdId}/family-member",household.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
}
