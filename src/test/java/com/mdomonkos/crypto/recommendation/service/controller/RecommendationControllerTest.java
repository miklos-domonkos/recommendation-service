package com.mdomonkos.crypto.recommendation.service.controller;

import com.mdomonkos.crypto.recommendation.service.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RecommendationController.class)
class RecommendationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StatisticsService statisticsService;

  @Test
  void whenValidParameterForRange_thenReturns200() throws Exception {

    mockMvc.perform(get("/recommendations/volatile")
                      .param("day", "2022-01-01T00:00:00+05:00")
           )
           .andExpect(status().isOk());

  }

  @Test
  void whenInValidParameterForRange_thenReturns400() throws Exception {

    mockMvc.perform(get("/recommendations/volatile")
                      .param("day", "2022-01-01T00:00:00")
           )
           .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

  }
}