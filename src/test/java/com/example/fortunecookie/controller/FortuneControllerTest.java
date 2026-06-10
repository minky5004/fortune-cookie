package com.example.fortunecookie.controller;

import com.example.fortunecookie.entity.Fortune;
import com.example.fortunecookie.entity.FortuneCategory;
import com.example.fortunecookie.exception.GlobalExceptionHandler;
import com.example.fortunecookie.service.FortuneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FortuneControllerTest {

    private MockMvc mockMvc;
    private FortuneService fortuneService;

    @BeforeEach
    void setUp() {
        fortuneService = mock(FortuneService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FortuneController(fortuneService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 메인_페이지_요청시_index_뷰를_반환한다() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void 운세_API가_JSON을_반환한다() throws Exception {
        Fortune fortune = new Fortune("오늘은 행운의 날입니다.", FortuneCategory.LUCK);
        given(fortuneService.getRandomFortune()).willReturn(fortune);

        mockMvc.perform(get("/api/fortune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("오늘은 행운의 날입니다."))
                .andExpect(jsonPath("$.category").value("행운"));
    }

    @Test
    void 운세_데이터_없을_때_500_에러_응답을_반환한다() throws Exception {
        given(fortuneService.getRandomFortune()).willThrow(new IllegalStateException("운세 데이터가 없습니다."));

        mockMvc.perform(get("/api/fortune"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("운세 데이터가 없습니다."));
    }
}
