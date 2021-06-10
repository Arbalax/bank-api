package com.bootcamp.bankapi;

import com.bootcamp.bankapi.controller.EmployeeController;
import com.bootcamp.bankapi.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Alphanumeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@OrderWith(Alphanumeric.class)
@TestPropertySource("/application-test.properties")
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testControllerIsPresent() {
        Assertions.assertThat(employeeController).isNotNull();
    }

    @Test
    public void TestSynchronizedData() {
        employeeService.synchronizeBalance();
    }

    @Test
    public void testAddUser() throws Exception {
        this.mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Ivan\",\"lastName\":\"Biryukov\",\"phoneNumber\":\"+9255555555\",\"passport\":\"1234567890\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":101,\"firstName\":\"Ivan\",\"lastName\":\"Biryukov\",\"phoneNumber\":\"+9255555555\",\"passport\":\"1234567890\",\"accounts\":null,\"counterparties\":null}"));
    }

    @Test
    public void testAddAccount() throws Exception {
        this.mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"0000000000\",\"balance\":0.1,\"status\":\"Open\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":101,\"number\":\"0000000000\",\"balance\":0.1,\"status\":\"Open\",\"cards\":null,\"operations\":null}"));
    }

    @Test
    public void testConfirmCard() throws Exception {
        this.mockMvc.perform(put("/api/cards/4112169602917774")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Confirmed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":94,\"number\":\"4112169602917774\",\"expirationMonth\":3,\"expirationYear\":23,\"cvv\":\"376\",\"balance\":9.19,\"status\":\"Confirmed\"}"));
    }

    @Test
    public void testConfirmOperation() throws Exception {
        this.mockMvc.perform(put("/api/operations/18")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Confirmed"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"id\":18,\"amount\":8.7,\"action\":\"-\",\"status\":\"Unconfirmed\"}"));

        this.mockMvc.perform(put("/api/operations/30")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Confirmed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":30,\"amount\":0.59,\"action\":\"+\",\"status\":\"confirmed\"}"));
    }
}
