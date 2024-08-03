package com.assu.study.cleanme.account.adapter.in.web;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assu.study.cleanme.account.application.port.in.SendMoneyCommand;
import com.assu.study.cleanme.account.application.port.in.SendMoneyUseCase;
import com.assu.study.cleanme.account.domain.Account;
import com.assu.study.cleanme.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest 의 controllers 속성을 사용하지 않으면 애플리케이션에 포함된 모든 컨트롤러 클래스 스캔
@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private SendMoneyUseCase sendMoneyUseCase;

  @Test
  void testSendMoney() throws Exception {
    mockMvc
        .perform(
            post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500)
                .header("Content-Type", MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    SendMoneyCommand command =
        new SendMoneyCommand(
            new Account.AccountId(41L), new Account.AccountId(42L), Money.of(500L));

    then(sendMoneyUseCase).should().sendMoney(eq(command));
  }
}
