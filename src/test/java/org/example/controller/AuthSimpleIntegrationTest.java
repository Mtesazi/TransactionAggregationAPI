package org.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthSimpleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginAndInspect_shouldReturnTokenAndRoles() throws Exception {
        String loginJson = "{\"username\":\"user\",\"password\":\"password\"}";

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        assertThat(node.has("token")).isTrue();
        String token = node.get("token").asText();
        assertThat(token).isNotBlank();

        MvcResult inspect = mockMvc.perform(get("/auth/inspect").param("token", token))
                .andExpect(status().isOk())
                .andReturn();

        String inspectBody = inspect.getResponse().getContentAsString();
        JsonNode inspectNode = objectMapper.readTree(inspectBody);
        assertThat(inspectNode.has("username")).isTrue();
        assertThat(inspectNode.get("username").asText()).isEqualTo("user");
        assertThat(inspectNode.has("roles")).isTrue();
        assertThat(inspectNode.get("roles").isArray()).isTrue();
    }
}

