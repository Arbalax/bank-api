package com.bootcamp.bankapi;

import com.bootcamp.bankapi.controller.UserController;
import com.bootcamp.bankapi.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@OrderWith(Alphanumeric.class)
@TestPropertySource("/application-test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testControllerIsPresent() {
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    public void TestSynchronizedData() {
        employeeService.synchronizeBalance();
    }

    @Test
    public void testGetCardsByAccountNumber() throws Exception {
        this.mockMvc.perform(get("/api/users/5/accounts/5130737042/cards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":10,\"number\":\"4460843099361200\",\"expirationMonth\":8,\"expirationYear\":24,\"cvv\":\"262\",\"balance\":1.23,\"status\":\"Open\"}]"));

        this.mockMvc.perform(get("/api/users/5/accounts/51307370420000/cards"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCardByAccountNumber() throws Exception {
        this.mockMvc.perform(post("/api/users/1/accounts/2235583113/cards")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"number\":\"0000000000000000\",\"expirationMonth\":6,\"expirationYear\":24,\"cvv\":\"294\",\"balance\":3.16,\"status\":\"Unconfirmed\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":101,\"number\":\"0000000000000000\",\"expirationMonth\":6,\"expirationYear\":24,\"cvv\":\"294\",\"balance\":3.16,\"status\":\"Unconfirmed\"}"));

        this.mockMvc.perform(post("/api/users/1/accounts/2235583113/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"0000000000000000\",\"expirationMonth\":6,\"expirationYear\":24,\"cvv\":\"294\",\"balance\":3.16,\"status\":\"Unconfirmed\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"id\":101,\"number\":\"0000000000000000\",\"expirationMonth\":6,\"expirationYear\":24,\"cvv\":\"294\",\"balance\":3.16,\"status\":\"Unconfirmed\"}"));

    }

    @Test
    public void testAddFunds() throws Exception {
        this.mockMvc.perform(put("/api/users/1/accounts/2515667789/cards/4554723037303527")
        .contentType(MediaType.APPLICATION_JSON)
        .content("17.75"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":101,\"amount\":17.75,\"action\":\"+\",\"status\":\"unconfirmed\"}"));

        this.mockMvc.perform(put("/api/users/1/accounts/2515667789/cards/4554723037303527")
                .contentType(MediaType.APPLICATION_JSON)
                .content("17.75"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":102,\"amount\":17.75,\"action\":\"+\",\"status\":\"unconfirmed\"}"));

    }

    @Test
    public void testGetBalance() throws Exception {
        this.mockMvc.perform(get("/api/users/7/accounts/8987604645/cards/1992656808580991/balance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("4.24"));
    }

    @Test
    public void testGetCounterparties() throws Exception {
        this.mockMvc.perform(get("/api/users/51/counterparties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":12,\"name\":\"Sed Turpis Nec Industries\",\"bankBIK\":572870788,\"account\":\"4100933552\"},{\"id\":51,\"name\":\"Ad Litora LLP\",\"bankBIK\":255146254,\"account\":\"7374242282\"}]"));
    }

    @Test
    public void testAddCounterparty() throws Exception {
        this.mockMvc.perform(post("/api/users/3/counterparties")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Sit Amet Industries\",\"bankBIK\":961854456,\"account\":\"1948457560\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":101,\"name\":\"Sit Amet Industries\",\"bankBIK\":961854456,\"account\":\"1948457560\"}"));

    }

    @Test
    public void transferMoneyToCounterparty() throws Exception {
        this.mockMvc.perform(put("/api/users/5/counterparties/Arcu Vel LLC/accounts/5952878724")
                .contentType(MediaType.APPLICATION_JSON)
                .content("2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("6.76"));
    }
}
