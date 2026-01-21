package br.com.imsa.easyfood.application.v1.controllers;

import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste de componentes cobrindo TODAS as rotas de UserSystem com autenticação JWT quando necessário.
 *
 * Fluxo coberto (request -> validação -> service/use case -> persistência/consulta real -> response):
 * - POST /api/user-system/v1 (aberta)
 * - POST /api/auth/v1/login (obter token)
 * - GET /api/user-system/v1 (protegida)
 * - GET /api/user-system/v1/{id} (protegida)
 * - PUT /api/user-system/v1/{id} (protegida)
 * - DELETE /api/user-system/v1/{id} (protegida)
 *
 * Observações:
 * - Usamos Testcontainers (PostgreSQL) e Liquibase real.
 * - Sem mocks de serviços internos.
 * - Comentários explicam cada cenário.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserSystemRoutesComponentTest {

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

    @Autowired
    private UserSystemRepository userSystemRepository;

    private Long userTypeId;

    @BeforeEach
    void setupDb() {
        // Limpa tabelas dependentes (ordem por FK)
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
        // Garante tipo de usuário para FK
        jdbcTemplate.update("INSERT INTO easyfood.user_type (name) VALUES (?)", "CUSTOMER");
        userTypeId = jdbcTemplate.queryForObject("SELECT id FROM easyfood.user_type ORDER BY id DESC LIMIT 1", Long.class);
        assertThat(userTypeId).isNotNull();
    }

    @AfterEach
    void cleanDb() {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
    }

    // --- Helpers

    private String buildCreatePayload(String username, String name, String email, String password) {
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
                        .content(buildCreatePayload(username, name, email, password)))
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
        assertThat(node.get("token")).isNotNull();
        return node.get("token").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    // 1) POST create user (aberta) para preparar cenário e validar persistência
    @Test
    @DisplayName("01 - POST /user-system - cria usuário e persiste (happy path)")
    void t01_postCreateUser_shouldPersist() throws Exception {
        String username = "user1";
        String email = "user1@example.com";
        String name = "User One";
        String password = "Secret1";

        String responseBody = mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildCreatePayload(username, name, email, password)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(responseBody);
        long id = node.get("id").asLong();
        assertThat(id).isPositive();

        Optional<UserSystemJpaEntity> saved = userSystemRepository.findById(id);
        assertThat(saved).isPresent();
        assertThat(saved.get().getUsername()).isEqualTo(username);
        assertThat(saved.get().getEmail()).isEqualTo(email);
    }

    // 2) Autenticar e usar token em rotas protegidas
    @Test
    @DisplayName("02 - LOGIN e GET /user-system (paginado) com token válido → 200 e conteúdo esperado")
    void t02_loginAndListWithToken_shouldReturn200() throws Exception {
        // Arrange: cria dois usuários e autentica com o primeiro
        createUserAndReturnId("john", "John", "john@example.com", "Str0ng1");
        createUserAndReturnId("jane", "Jane", "jane@example.com", "Str0ng1");
        String token = loginAndGetToken("john", "Str0ng1");

        // Act: chama GET listado com Bearer
        ResultActions action = mockMvc.perform(get("/api/user-system/v1")
                .header("Authorization", bearer(token)));

        // Assert
        String body = action.andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("content").isArray()).isTrue();
        String contentText = node.get("content").toString();
        assertThat(contentText).contains("john@example.com");
        assertThat(contentText).contains("jane@example.com");
    }

    @Test
    @DisplayName("03 - GET /user-system sem token → 401")
    void t03_listWithoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/user-system/v1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("04 - GET /user-system com token inválido → 401")
    void t04_listWithInvalidToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/user-system/v1")
                        .header("Authorization", bearer("invalid.token.here")))
                .andExpect(status().isUnauthorized());
    }

    // 3) GET by id com token
    @Test
    @DisplayName("05 - GET /user-system/{id} com token válido → 200 e dados corretos")
    void t05_getByIdWithToken_shouldReturn200() throws Exception {
        long id = createUserAndReturnId("mark", "Mark", "mark@example.com", "Secret1");
        String token = loginAndGetToken("mark", "Secret1");

        String body = mockMvc.perform(get("/api/user-system/v1/" + id)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("id").asLong()).isEqualTo(id);
        assertThat(node.get("email").asText()).isEqualTo("mark@example.com");
    }

    @Test
    @DisplayName("06 - GET /user-system/{id} inexistente → 404 (com token)")
    void t06_getByIdNotFound_shouldReturn404() throws Exception {
        long id = createUserAndReturnId("paul", "Paul", "paul@example.com", "Secret1");
        String token = loginAndGetToken("paul", "Secret1");

        mockMvc.perform(get("/api/user-system/v1/99999")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("07 - GET /user-system/{id} sem token → 401")
    void t07_getByIdWithoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/user-system/v1/1"))
                .andExpect(status().isUnauthorized());
    }

    // 4) PUT update com token
    @Test
    @DisplayName("08 - PUT /user-system/{id} com token e payload válido → 200 e atualiza no banco")
    void t08_putUpdateWithToken_shouldUpdate() throws Exception {
        long id = createUserAndReturnId("anna", "Anna", "anna@example.com", "Secret1");
        String token = loginAndGetToken("anna", "Secret1");

        String updatePayload = "{" +
                "\"username\":\"anna\"," +
                "\"name\":\"Anna Maria\"," +
                "\"email\":\"anna@example.com\"," +
                "\"userType\":" + userTypeId + "," +
                "\"address\":{\"street\":\"Rua Y\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"20\",\"zipCode\":\"11111111\"}}";

        String body = mockMvc.perform(put("/api/user-system/v1/" + id)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("name").asText()).isEqualTo("Anna Maria");

        Optional<UserSystemJpaEntity> updated = userSystemRepository.findById(id);
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Anna Maria");
    }

    @Test
    @DisplayName("09 - PUT /user-system/{id} com payload inválido → 400 (com token)")
    void t09_putInvalidPayload_should400() throws Exception {
        long id = createUserAndReturnId("bob", "Bob", "bob@example.com", "Secret1");
        String token = loginAndGetToken("bob", "Secret1");

        // nome e email inválidos; faltam address/ userType etc.
        String invalid = "{" +
                "\"username\":\"\"," +
                "\"name\":\"\"," +
                "\"email\":\"invalid\"}";

        String body = mockMvc.perform(put("/api/user-system/v1/" + id)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("status").asInt()).isEqualTo(400);
        assertThat(node.get("errors")).isNotNull();
    }

    @Test
    @DisplayName("10 - PUT /user-system/{id} sem token → 401")
    void t10_putWithoutToken_should401() throws Exception {
        mockMvc.perform(put("/api/user-system/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    // 5) DELETE com token
    @Test
    @DisplayName("11 - DELETE /user-system/{id} com token válido → 204 e remove do banco")
    void t11_deleteWithToken_should204() throws Exception {
        long id = createUserAndReturnId("cara", "Cara", "cara@example.com", "Secret1");
        String token = loginAndGetToken("cara", "Secret1");

        mockMvc.perform(delete("/api/user-system/v1/" + id)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNoContent());

        assertThat(userSystemRepository.findById(id)).isNotPresent();
    }

    @Test
    @DisplayName("12 - DELETE /user-system/{id} inexistente → 404 (com token)")
    void t12_deleteNotFound_should404() throws Exception {
        long id = createUserAndReturnId("dave", "Dave", "dave@example.com", "Secret1");
        String token = loginAndGetToken("dave", "Secret1");

        mockMvc.perform(delete("/api/user-system/v1/99999")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("13 - DELETE /user-system/{id} sem token → 401")
    void t13_deleteWithoutToken_should401() throws Exception {
        mockMvc.perform(delete("/api/user-system/v1/1"))
                .andExpect(status().isUnauthorized());
    }

    // 6) Autenticação: credenciais inválidas
    @Test
    @DisplayName("14 - LOGIN com credenciais inválidas → 400 (NegocioException)")
    void t14_loginInvalidCredentials_should400() throws Exception {
        // usuário não existe → a camada de caso de uso lança NegocioException e o handler retorna 400
        String loginPayload = "{\"username\":\"ghost\",\"password\":\"wrong\"}";
        mockMvc.perform(post("/api/auth/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isBadRequest());
    }
}
