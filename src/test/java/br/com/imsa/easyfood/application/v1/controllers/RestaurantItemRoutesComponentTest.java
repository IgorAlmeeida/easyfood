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
 * Component tests for RestaurantItemController endpoints, covering request → auth → service → DB persistence.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.DisplayName.class)
class RestaurantItemRoutesComponentTest {

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
    private long restaurantId;

    @BeforeEach
    void setupDb() throws Exception {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant_item RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
        // Seed user_type
        jdbcTemplate.update("INSERT INTO easyfood.user_type (name) VALUES (?)", "CUSTOMER");
        userTypeId = jdbcTemplate.queryForObject("SELECT id FROM easyfood.user_type ORDER BY id DESC LIMIT 1", Long.class);
        assertThat(userTypeId).isNotNull();
        // Create user and login
        createUser("owneritem", "Owner Item", "owner.item@example.com", "Secret1!");
        jwt = loginAndGetToken("owneritem", "Secret1!");
        assertThat(jwt).isNotBlank();
        // Create a restaurant to link items
        restaurantId = createRestaurantAndReturnId("Rest X");
        assertThat(restaurantId).isPositive();
    }

    @AfterEach
    void cleanDb() {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant_item RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.restaurant RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
    }

    // Helpers
    private void createUser(String username, String name, String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"" + username + "\"," +
                                "\"name\":\"" + name + "\"," +
                                "\"email\":\"" + email + "\"," +
                                "\"userType\":" + userTypeId + "," +
                                "\"password\":\"" + password + "\"," +
                                "\"address\":{\"street\":\"Rua X\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"10\",\"zipCode\":\"00000000\"}}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("id").asLong()).isPositive();
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"" + username + "\"," +
                                "\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readTree(body).get("token").asText();
    }

    private long createRestaurantAndReturnId(String name) throws Exception {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);
        ResultActions createAct = mockMvc.perform(post("/api/restaurant/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"" + name + "\"," +
                                "\"address\":{\"street\":\"Av. A\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"100\",\"zipCode\":\"00000000\"}," +
                                "\"kitchenType\":\"BRAZILIAN\"," +
                                "\"startOperationTime\":\"" + start + "\"," +
                                "\"endOperationTime\":\"" + end + "\"," +
                                "\"proprietaryId\":1" + // will be ignored by use case validation if not needed
                                "}"))
                .andExpect(status().isCreated());
        String created = createAct.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readTree(created).get("id").asLong();
    }

    private String buildItemCreatePayload(String description) {
        return "{" +
                "\"description\":\"" + description + "\"," +
                "\"price\":25.5," +
                "\"image\":\"/img.png\"," +
                "\"availability\":\"LOCAL\"," +
                "\"restaurantId\":" + restaurantId +
                "}";
    }

    private String buildItemUpdatePayload(String description) {
        return "{" +
                "\"description\":\"" + description + "\"," +
                "\"price\":30.0," +
                "\"image\":\"/img2.png\"," +
                "\"availability\":\"DELIVERY\"," +
                "\"restaurantId\":" + restaurantId +
                "}";
    }

    @Test
    @DisplayName("1) [Happy] create → get → list → update → delete item with valid token")
    void t01_fullCrudFlow_item_authenticated() throws Exception {
        // Create
        ResultActions createAct = mockMvc.perform(post("/api/restaurant-item/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildItemCreatePayload("Item A")))
                .andExpect(status().isCreated());
        String created = createAct.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        long id = objectMapper.readTree(created).get("id").asLong();
        assertThat(id).isPositive();

        // Get by id
        mockMvc.perform(get("/api/restaurant-item/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        // List (with filters)
        mockMvc.perform(get("/api/restaurant-item/v1")
                        .header("Authorization", "Bearer " + jwt)
                        .param("description", "Item")
                        .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(status().isOk());

        // Update
        String updBody = mockMvc.perform(put("/api/restaurant-item/v1/" + id)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildItemUpdatePayload("Item A Updated")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(objectMapper.readTree(updBody).get("description").asText()).contains("Updated");

        // Delete
        mockMvc.perform(delete("/api/restaurant-item/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());

        // Get after delete
        mockMvc.perform(get("/api/restaurant-item/v1/" + id)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("2) [Negative] unauthorized when missing/invalid token")
    void t02_unauthorized_when_missingOrInvalidToken() throws Exception {
        mockMvc.perform(get("/api/restaurant-item/v1")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/restaurant-item/v1").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("3) [Negative] bad request on invalid payload")
    void t03_badRequest_onInvalidPayload() throws Exception {
        String invalid = "{" +
                "\"description\":\"\"," +
                "\"price\":null," +
                "\"restaurantId\":null" +
                "}";
        String body = mockMvc.perform(post("/api/restaurant-item/v1")
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
        mockMvc.perform(put("/api/restaurant-item/v1/999999")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildItemUpdatePayload("X")))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/restaurant-item/v1/999999")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }
}
