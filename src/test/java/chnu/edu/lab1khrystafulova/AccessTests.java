package chnu.edu.lab1khrystafulova;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AccessTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    // ---------------- ANONYMOUS (без логіна) ----------------

    @Test
    @WithAnonymousUser
    @DisplayName("1. Anonymous -> GET /items = 401 Unauthorized")
    void whenAnonymousGetAllThen401() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items"));
        assertEquals(401, result.andReturn().getResponse().getStatus());
        assertEquals("Unauthorized", result.andReturn().getResponse().getErrorMessage());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("2. Anonymous -> /hello/admin = 401 Unauthorized")
    void whenAnonymousHelloAdminThen401() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/admin"));
        assertEquals(401, result.andReturn().getResponse().getStatus());
    }

    // ---------------- /hello/admin (тільки ADMIN) ----------------

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    @DisplayName("3. ADMIN -> /hello/admin = 200")
    void whenAdminHelloAdminThen200() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/admin"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
        assertEquals("Hello, Admin!", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    @DisplayName("4. USER -> /hello/admin = 403 Forbidden")
    void whenUserHelloAdminThen403() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/admin"));
        assertEquals(403, result.andReturn().getResponse().getStatus());
        assertEquals("Forbidden", result.andReturn().getResponse().getErrorMessage());
    }

    // ---------------- /hello/user (тільки USER) ----------------

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    @DisplayName("5. USER -> /hello/user = 200")
    void whenUserHelloUserThen200() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/user"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
        assertEquals("Hello, User!", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    @DisplayName("6. ADMIN -> /hello/user = 403 Forbidden")
    void whenAdminHelloUserThen403() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/user"));
        assertEquals(403, result.andReturn().getResponse().getStatus());
        assertEquals("Forbidden", result.andReturn().getResponse().getErrorMessage());
    }

    // ---------------- /hello/man (ADMIN або SUPERADMIN) ----------------

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    @DisplayName("7. ADMIN -> /hello/man = 200")
    void whenAdminHelloManThen200() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/man"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
        assertEquals("Hello, man!", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "superadmin", password = "superadmin", roles = {"SUPERADMIN"})
    @DisplayName("8. SUPERADMIN -> /hello/man = 200")
    void whenSuperAdminHelloManThen200() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/man"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    @DisplayName("9. USER -> /hello/man = 403 Forbidden")
    void whenUserHelloManThen403() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/man"));
        assertEquals(403, result.andReturn().getResponse().getStatus());
    }

    // ---------------- /hello/superadmin (тільки SUPERADMIN) ----------------

    @Test
    @WithMockUser(username = "superadmin", password = "superadmin", roles = {"SUPERADMIN"})
    @DisplayName("10. SUPERADMIN -> /hello/superadmin = 200")
    void whenSuperAdminHelloSuperAdminThen200() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/superadmin"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
        assertEquals("Hello, SuperAdmin!", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    @DisplayName("11. ADMIN -> /hello/superadmin = 403 Forbidden")
    void whenAdminHelloSuperAdminThen403() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/superadmin"));
        assertEquals(403, result.andReturn().getResponse().getStatus());
    }

    // ---------------- /hello/unknown (без анотації: будь-хто авторизований) ----------------

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    @DisplayName("12. USER -> /hello/unknown = 200 (немає анотації)")
    void whenUserHelloUnknownThen200() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/items/hello/unknown"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
        assertEquals("Hello, Unknown!", result.andReturn().getResponse().getContentAsString());
    }

    // ---------------- DELETE (тільки SUPERADMIN) ----------------

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    @DisplayName("13. ADMIN -> DELETE /items/3 = 403 Forbidden")
    void whenAdminDeleteThen403() throws Exception {
        ResultActions result = mockMvc.perform(delete("/api/v1/items/3"));
        assertEquals(403, result.andReturn().getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "superadmin", password = "superadmin", roles = {"SUPERADMIN"})
    @DisplayName("14. SUPERADMIN -> DELETE /items/3 = 200")
    void whenSuperAdminDeleteThen200() throws Exception {
        ResultActions result = mockMvc.perform(delete("/api/v1/items/3"));
        assertEquals(200, result.andReturn().getResponse().getStatus());
    }
}