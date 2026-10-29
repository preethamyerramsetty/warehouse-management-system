
package com.company.wms.putaway_service.config;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.wms.putaway_service.controller.PutAwayTaskController;
import com.company.wms.putaway_service.service.PutAwayTaskService;

@WebMvcTest(PutAwayTaskController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PutAwayTaskService putAwayTaskService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void getTask_withoutAuthentication_returnsUnauthorized()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/putaway/tasks/" + UUID.randomUUID())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void getTask_withUserRole_isAllowed()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/putaway/tasks/" + UUID.randomUUID())
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_USER")))
        ).andExpect(status().isOk());
    }

    @Test
    void createTask_withUserRole_returnsForbidden()
            throws Exception {

        mockMvc.perform(
                post("/api/v1/putaway/tasks")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andExpect(status().isForbidden());
    }
}
