package br.com.imsa.easyfood.application.v1.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Component tests for UserTypeController endpoints with JWT auth.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserTypeRoutesComponentTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("easyfood_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerDataSourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.liquibase.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long userTypeId;
    private String jwt;

    @BeforeEach
    void setup() throws Exception {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_type RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
        // Seed a user_type for the user creation
        jdbcTemplate.update("INSERT INTO easyfood.user_type (name) VALUES (?)", "CUSTOMER");
        userTypeId = jdbcTemplate.queryForObject("SELECT id FROM easyfood.user_type ORDER BY id DESC LIMIT 1", Long.class);
        // Create a user and login
        createUser("utadmin", "UT Admin", "ut.admin@example.com", "Secret1!");
        jwt = loginAndGetToken("utadmin", "Secret1!");
        assertThat(jwt).isNotBlank();
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_type RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
    }

    private void createUser(String username, String name, String email, String password) throws Exception {
        String payload = "{" +
                "\"username\":\"" + username + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"userType\":" + userTypeId + "," +
                "\"password\":\"" + password + "\"," +
                "\"address\":{\"street\":\"Rua X\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"10\",\"zipCode\":\"00000000\"}}";
        mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        String payload = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"}";
        String body = mockMvc.perform(post("/api/auth/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        return node.get("token").asText();
    }

    private String buildCreatePayload(String name) {
        return "{" +
                "\"name\":\"" + name + "\"" +
                "}";
    }

    @Test
    @DisplayName("1) [Happy] create → get → list → update → delete user type with valid token")
    void t01_fullCrud_userType_authenticated() throws Exception {
        // Create
        String created = mockMvc.perform(post("/api/user-type/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildCreatePayload("MANAGER")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        long id = objectMapper.readTree(created).get("id").asLong();
        assertThat(id).isPositive();

        // Get by id
        mockMvc.perform(get("/api/user-type/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        // List
        mockMvc.perform(get("/api/user-type/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .param("name", "MAN"))
                .andExpect(status().isOk());

        // Update
        String updated = mockMvc.perform(put("/api/user-type/v1/" + id)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildCreatePayload("MANAGER-2")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(objectMapper.readTree(updated).get("name").asText()).contains("2");

        // Delete
        mockMvc.perform(delete("/api/user-type/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());

        // Get after delete
        mockMvc.perform(get("/api/user-type/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("2) [Negative] unauthorized when missing/invalid token")
    void t02_unauthorized_when_missingOrInvalidToken() throws Exception {
        mockMvc.perform(get("/api/user-type/v1")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/user-type/v1").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("3) [Negative] bad request on invalid payload")
    void t03_badRequest_onInvalidPayload() throws Exception {
        String invalid = "{" +
                "\"name\":\"\"" +
                "}";
        String body = mockMvc.perform(post("/api/user-type/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("status").asInt()).isEqualTo(400);
        assertThat(node.get("errors")).isNotNull();
    }

    @Test
    @DisplayName("4) [Negative] not found on non-existing id for update/delete")
    void t04_notFound_onNonExistingId() throws Exception {
        mockMvc.perform(put("/api/user-type/v1/999999")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildCreatePayload("X")))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/user-type/v1/999999")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }
}
