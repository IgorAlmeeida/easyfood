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
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Component tests for RestaurantController endpoints, covering request → auth → service → DB persistence.
 * Scenarios:
 *  - Create proprietor user, login to get JWT, then call protected routes with Authorization header.
 *  - Happy paths for create, list, get by id, update, delete.
 *  - Negative: unauthorized (no/invalid token), invalid payload (400), not found (404).
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.DisplayName.class)
class RestaurantRoutesComponentTest {

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
    private long proprietorId;

    @BeforeEach
    void setupDb() throws Exception {
        // Clean related tables (order by FKs)
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant_item RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
        // Seed user_type
        jdbcTemplate.update("INSERT INTO easyfood.user_type (name) VALUES (?)", "CUSTOMER");
        userTypeId = jdbcTemplate.queryForObject("SELECT id FROM easyfood.user_type ORDER BY id DESC LIMIT 1", Long.class);
        assertThat(userTypeId).isNotNull();
        // Create a user and login to obtain JWT for protected routes
        proprietorId = createUserAndReturnId("owner1", "Owner Name", "owner1@example.com", "Secret1!");
        jwt = loginAndGetToken("owner1", "Secret1!");
        assertThat(jwt).isNotBlank();
    }

    @AfterEach
    void cleanDb() {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant_item RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
    }

    // Helpers
    private String buildUserPayload(String username, String name, String email, String password) {
        return "{" +
                "\"username\":\"" + username + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"userType\":" + userTypeId + "," +
                "\"password\":\"" + password + "\"," +
                "\"address\":{\"street\":\"Rua X\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"10\",\"zipCode\":\"00000000\"}}";
    }

    private long createUserAndReturnId(String username, String name, String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildUserPayload(username, name, email, password)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        return node.get("id").asLong();
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        String loginPayload = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"}";
        String body = mockMvc.perform(post("/api/auth/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        return node.get("token").asText();
    }

    private String buildRestaurantCreatePayload(String name) {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);
        return "{" +
                "\"name\":\"" + name + "\"," +
                "\"address\":{\"street\":\"Av. A\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"100\",\"zipCode\":\"00000000\"}," +
                "\"kitchenType\":\"BRAZILIAN\"," +
                "\"startOperationTime\":\"" + start.toString() + "\"," +
                "\"endOperationTime\":\"" + end.toString() + "\"," +
                "\"proprietaryId\":" + proprietorId +
                "}";
    }

    private String buildRestaurantUpdatePayload(String newName) {
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = LocalDateTime.now().plusHours(4);
        return "{" +
                "\"name\":\"" + newName + "\"," +
                "\"address\":{\"street\":\"Av. B\",\"neighborhood\":\"Bairro B\",\"city\":\"RJ\",\"number\":\"200\",\"zipCode\":\"11111111\"}," +
                "\"kitchenType\":\"BRAZILIAN\"," +
                "\"startOperationTime\":\"" + start.toString() + "\"," +
                "\"endOperationTime\":\"" + end.toString() + "\"," +
                "\"proprietaryId\":" + proprietorId +
                "}";
    }

    @Test
    @DisplayName("1) [Happy] create → get → list → update → delete restaurant with valid token")
    void t01_fullCrudFlow_restaurant_authenticated() throws Exception {
        // Create
        ResultActions createAct = mockMvc.perform(post("/api/restaurant/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRestaurantCreatePayload("Rest A")))
                .andExpect(status().isCreated());
        String created = createAct.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        long id = objectMapper.readTree(created).get("id").asLong();
        assertThat(id).isPositive();

        // Get by id
        String getBody = mockMvc.perform(get("/api/restaurant/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(objectMapper.readTree(getBody).get("id").asLong()).isEqualTo(id);

        // List
        mockMvc.perform(get("/api/restaurant/v1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        // Update
        String updBody = mockMvc.perform(put("/api/restaurant/v1/" + id)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRestaurantUpdatePayload("Rest A Updated")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(objectMapper.readTree(updBody).get("name").asText()).contains("Updated");

        // Delete
        mockMvc.perform(delete("/api/restaurant/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());

        // Get after delete
        mockMvc.perform(get("/api/restaurant/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("2) [Negative] unauthorized when missing/invalid token")
    void t02_unauthorized_when_missingOrInvalidToken() throws Exception {
        mockMvc.perform(get("/api/restaurant/v1")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/restaurant/v1").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("3) [Negative] bad request on invalid payload")
    void t03_badRequest_onInvalidPayload() throws Exception {
        String invalidPayload = "{" +
                "\"name\":\"\"," +
                "\"address\":null," + // missing address
                "\"kitchenType\":null," +
                "\"startOperationTime\":null," +
                "\"endOperationTime\":null" +
                "}";
        String body = mockMvc.perform(post("/api/restaurant/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("status").asInt()).isEqualTo(400);
        assertThat(node.get("errors")).isNotNull();
    }

    @Test
    @DisplayName("4) [Negative] not found on non-existing id for update/delete")
    void t04_notFound_onNonExistingId() throws Exception {
        mockMvc.perform(put("/api/restaurant/v1/999999")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildRestaurantUpdatePayload("X")))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/restaurant/v1/999999")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }
}
