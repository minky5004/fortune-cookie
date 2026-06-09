package com.example.fortunecookie.service;

import com.example.fortunecookie.entity.Fortune;
import com.example.fortunecookie.entity.FortuneCategory;
import com.example.fortunecookie.repository.FortuneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FortuneServiceTest {

    @Mock
    private FortuneRepository fortuneRepository;

    @InjectMocks
    private FortuneService fortuneService;

    @Test
    void 랜덤_운세를_반환한다() {
        Fortune fortune = new Fortune("오늘은 행운의 날입니다.", FortuneCategory.LUCK);
        given(fortuneRepository.findRandom()).willReturn(Optional.of(fortune));

        Fortune result = fortuneService.getRandomFortune();

        assertThat(result.getMessage()).isEqualTo("오늘은 행운의 날입니다.");
        assertThat(result.getCategory()).isEqualTo(FortuneCategory.LUCK);
    }

    @Test
    void 운세_데이터가_없으면_예외가_발생한다() {
        given(fortuneRepository.findRandom()).willReturn(Optional.empty());

        assertThatThrownBy(() -> fortuneService.getRandomFortune())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("운세 데이터가 없습니다.");
    }
}
