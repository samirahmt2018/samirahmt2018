//package com.gundan.terragold;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gundan.terragold.dto.request.*;
//import com.gundan.terragold.enums.EmployeeRole;
//import com.gundan.terragold.repository.*;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@Testcontainers
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class TerraGoldIntegrationTest {
//
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
//            .withDatabaseName("terragold_test")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @Autowired MockMvc mockMvc;
//    @Autowired ObjectMapper objectMapper;
//
//    @Autowired EmployeeRepository employeeRepo;
//    @Autowired ItemTypeRepository itemTypeRepo;
//    @Autowired MachineRepository machineRepo;
//    @Autowired EmployeeAdvanceAccountRepository accountRepo;
//    @Autowired EmployeeAdvanceTransactionRepository txRepo;
//
//    static Long purchaserId;
//    static Long dieselId;
//    static Long excavatorId;
//    static String authToken; // store JWT or session
//
//    @BeforeAll
//    static void setupData(@Autowired MockMvc mvc,
//                          @Autowired ObjectMapper mapper) throws Exception {
//
//        // --- LOGIN FIRST ---
//        var loginJson = """
//                {"username":"admin","password":"admin123"}
//                """;
//
//        // Perform login request and capture response
//        MvcResult loginResult = mvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andReturn(); // do NOT assert status yet
//        String resp = loginResult.getResponse().getContentAsString();
//        System.out.println("Login response body: " + resp);
//
//// Print full response for debugging
//        String responseBody = loginResult.getResponse().getContentAsString();
//        System.out.println("Login response: " + responseBody);
//
//// Now you can assert the status if you want
//        int status = loginResult.getResponse().getStatus();
//        System.out.println("Login HTTP status: " + status);
//
//// Optional: check status after printing
//        assertThat(status).isEqualTo(200);
//
//        var loginResp = mapper.readTree(loginResult.getResponse().getContentAsString());
//        authToken = loginResp.get("data").get("token").asText(); // adjust if your login returns token differently
//        System.out.println("Login token: " + authToken);
//
//        // --- 1. Create Purchaser ---
//        var empReq = new EmployeeCreateRequest(
//                "Kidane Gebremichael", "EMP-001", "0911123456", EmployeeRole.PURCHASER, "Finance", LocalDate.now(),new BigDecimal("25000"),new BigDecimal("2000"),new BigDecimal("8383"),new BigDecimal("3377"), "Experienced purchaser for mining supplies");
//        String empJson = mapper.writeValueAsString(empReq);
//
//        String empResponse = mvc.perform(post("/api/employees/purchaser")
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(empJson))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        purchaserId = mapper.readTree(empResponse).get("id").asLong();
//        System.out.println("Created Purchaser ID: " + purchaserId);
//
//        // --- 2. Create Diesel Item ---
//        String dieselJson = """
//                {"name":"Diesel","unitOfMeasurement":"LITER"}
//                """;
//        String dieselResp = mvc.perform(post("/api/items")
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(dieselJson))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        dieselId = mapper.readTree(dieselResp).get("id").asLong();
//        System.out.println("Created Diesel Item ID: " + dieselId);
//
//        // --- 3. Create Excavator Machine ---
//        String machineJson = """
//                {"machineType":"Excavator","assetMachineId":"EX-01"}
//                """;
//        String machineResp = mvc.perform(post("/api/machines")
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(machineJson))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        excavatorId = mapper.readTree(machineResp).get("id").asLong();
//        System.out.println("Created Excavator ID: " + excavatorId);
//    }
//
//    // Tests unchanged, but make sure to include auth header
//    @Test
//    @Order(1)
//    void fullBusinessFlow_RealEthiopianMiningDay() throws Exception {
//
//        // Step 1: Top-up 10,000,000 ETB
//        var topUp = new TopUpRequest(new BigDecimal("10000000"), "Cash for raw gold purchase in Shire");
//        mockMvc.perform(post("/api/advance/topup/" + purchaserId)
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(topUp)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.balanceAfter").value(10000000));
//
//        // Verify balance
//        var account = accountRepo.findByEmployeeId(purchaserId).get();
//        assertThat(account.getCurrentBalance()).isEqualByComparingTo(new BigDecimal("10000000"));
//
//        // Step 2: Spend 34,000 ETB on diesel
//        var spendFuel = new SpendRequest(new BigDecimal("34000"), "500L Diesel at Total Humera", "INV-7788");
//        mockMvc.perform(post("/api/advance/spend/" + purchaserId)
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(spendFuel)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.balanceAfter").value(9966000));
//
//        // Step 3: Pay 42,000 ETB to daily diggers
//        var laborReq = new DailyLaborRequest("Group of 60 diggers", "Digger", new BigDecimal("42000"), "Cash payment");
//        mockMvc.perform(post("/api/labor/advance/" + purchaserId)
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(laborReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalPaid").value(42000))
//                .andExpect(jsonPath("$.paidFrom").value("Purchaser: Kidane Gebremichael"));
//
//        // Step 4: Record 182.4 quantityGrams gold produced
//        var prodReq = new ProductionCreateRequest((new BigDecimal("182.4"), new BigDecimal("92.5"), 5L, "SHIRE-2025-11-29");
//        mockMvc.perform(post("/api/production/log")
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(prodReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.quantityGrams").value(182.4));
//
//        // Step 5: Dashboard summary
//        mockMvc.perform(get("/api/dashboard/summary")
//                        .header("Authorization", "Bearer " + authToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalGoldThisMonth").value(182.4))
//                .andExpect(jsonPath("$.totalLaborCostToday").value(42000))
//                .andExpect(jsonPath("$.lowBalanceAgents[0].currentBalance").value(9924000));
//    }
//
//    @Test
//    @Order(2)
//    void shouldPreventOverSpending() throws Exception {
//        var overSpend = new SpendRequest(new BigDecimal("20000000"), "Trying to overspend", null);
//        mockMvc.perform(post("/api/advance/spend/" + purchaserId)
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(overSpend)))
//                .andExpect(status().is5xxServerError())
//                .andExpect(jsonPath("$.message").value("Insufficient balance"));
//    }
//}
