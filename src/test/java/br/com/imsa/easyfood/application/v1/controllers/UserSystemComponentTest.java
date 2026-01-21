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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste de componente para o fluxo completo da entidade UserSystem:
 * - Simula a request HTTP (MockMvc)
 * - Valida payload (Bean Validation + GlobalExceptionHandler)
 * - Passa pelo service/use case e camada de persistência
 * - Verifica gravação real em banco (Testcontainers + PostgreSQL)
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserSystemComponentTest {

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
        // Deixa o Liquibase habilitado no perfil de teste
        registry.add("spring.liquibase.enabled", () -> "true");
        // Usa o changelog padrão definido em application.yml
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
    void setUp() {
        // Garante limpeza entre testes (ordem importa por FK)
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
        // Garante pelo menos um tipo de usuário para atender a FK
        jdbcTemplate.update("INSERT INTO easyfood.user_type (name) VALUES (?)", "CUSTOMER");
        userTypeId = jdbcTemplate.queryForObject("SELECT id FROM easyfood.user_type ORDER BY id DESC LIMIT 1", Long.class);
        assertThat(userTypeId).isNotNull();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.user_system RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE easyfood.address RESTART IDENTITY CASCADE");
        // Não limpamos user_type para reutilizar o registro entre os testes, evitando recriar schema etc.
    }

    @Test
    @DisplayName("[Happy path] Deve criar usuário do sistema com payload válido e persistir no banco")
    void t01_shouldCreateUserSystem_whenValidRequest_thenPersistAndReturn201() throws Exception {
        String payload = "{" +
                "\"username\":\"jdoe\"," +
                "\"name\":\"John Doe\"," +
                "\"email\":\"john.doe@example.com\"," +
                "\"userType\":" + userTypeId + "," +
                "\"password\":\"Str0ngP@ss\"," +
                "\"address\":{\"street\":\"Av. Paulista\",\"neighborhood\":\"Bela Vista\",\"city\":\"São Paulo\",\"number\":\"1000\",\"zipCode\":\"01310100\"}}";

        String responseBody = mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(responseBody);
        Long id = node.get("id").asLong();
        assertThat(id).isNotNull();
        assertThat(node.get("username").asText()).isEqualTo("jdoe");
        assertThat(node.get("email").asText()).isEqualTo("john.doe@example.com");
        assertThat(node.get("name").asText()).isEqualTo("John Doe");
        assertThat(node.get("address")).isNotNull();
        assertThat(node.get("userType")).isNotNull();

        Optional<UserSystemJpaEntity> savedOpt = userSystemRepository.findById(id);
        assertThat(savedOpt).isPresent();
        UserSystemJpaEntity saved = savedOpt.get();
        assertThat(saved.getUsername()).isEqualTo("jdoe");
        assertThat(saved.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getAddressJpaEntity()).isNotNull();
        assertThat(saved.getUserType()).isNotNull();
        assertThat(saved.getUserType().getId()).isEqualTo(userTypeId);
        // Senha é criptografada, então apenas valida se não está em branco
        assertThat(saved.getPassword()).isNotBlank();
    }

    @Test
    @DisplayName("[Negativo] Deve retornar 400 quando payload for inválido (campos obrigatórios ausentes)")
    void t02_shouldReturn400_whenInvalidPayload_missingRequiredFields() throws Exception {
        // Payload faltando vários campos obrigatórios
        String payload = "{" +
                "\"username\":\"\"," + // em branco
                "\"name\":\"\"," + // em branco
                "\"email\":\"invalid-email\"" + // formato inválido
                "}";

        String responseBody = mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(responseBody);
        assertThat(node.get("status").asInt()).isEqualTo(400);
        assertThat(node.get("errors")).isNotNull();
        assertThat(node.get("errors").isArray()).isTrue();
        // Deve conter mensagens de validação para username, name, password, userType e address, além do email inválido
        String errorsAsText = node.get("errors").toString();
        assertThat(errorsAsText).contains("username");
        assertThat(errorsAsText).contains("name");
        assertThat(errorsAsText).contains("password");
        assertThat(errorsAsText).contains("userType");
        assertThat(errorsAsText).contains("address");
        assertThat(errorsAsText).contains("email");
    }

    @Test
    @DisplayName("[Negativo] Deve retornar 400 quando e-mail e username já existirem (violação de unicidade)")
    void t03_shouldReturn400_whenDuplicateEmailOrUsername() throws Exception {
        // Primeiro cadastro (válido)
        String base = "{" +
                "\"username\":\"uniqueUser\"," +
                "\"name\":\"Alice\"," +
                "\"email\":\"alice@example.com\"," +
                "\"userType\":" + userTypeId + "," +
                "\"password\":\"Secret1\"," +
                "\"address\":{\"street\":\"Rua A\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"10\",\"zipCode\":\"00000000\"}}";

        mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(base))
                .andExpect(status().isCreated());

        long countBefore = userSystemRepository.count();

        // Segundo cadastro com mesmo email e username
        String duplicate = "{" +
                "\"username\":\"uniqueUser\"," + // repetido
                "\"name\":\"Alice 2\"," +
                "\"email\":\"alice@example.com\"," + // repetido
                "\"userType\":" + userTypeId + "," +
                "\"password\":\"Secret1\"," +
                "\"address\":{\"street\":\"Rua B\",\"neighborhood\":\"Centro\",\"city\":\"SP\",\"number\":\"11\",\"zipCode\":\"00000001\"}}";

        String responseBody = mockMvc.perform(post("/api/user-system/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicate))
                .andExpect(status().isConflict()) // GlobalExceptionHandler mapeia DataIntegrityViolation para 409 (CONFLICT)
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(responseBody);
        assertThat(node.get("status").asInt()).isEqualTo(409);
        // A mensagem detalhada pode variar por banco/driver, então verificamos existência de detail
        assertThat(node.get("detail")).isNotNull();

        long countAfter = userSystemRepository.count();
        assertThat(countAfter).isEqualTo(countBefore); // não deve ter inserido o duplicado
    }
}
