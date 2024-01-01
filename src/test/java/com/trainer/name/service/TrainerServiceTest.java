package com.trainer.name.service;

import com.trainer.name.entity.Trainer;
import com.trainer.name.exception.TrainerNotFoundException;
import com.trainer.name.mapper.TrainerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @InjectMocks
    TrainerService trainerService;
    @Mock
    TrainerMapper trainerMapper;

    private static final int EXISTING_USER_ID = 1;
    private static final int NON_EXISTING_USER_ID = 999;

    @Test
    void shouldReturnUserWhenExistingUserIdIsProvided() throws TrainerNotFoundException {
        // モックの設定
        Trainer expectedTrainer = new Trainer(EXISTING_USER_ID, "ゼイユ", "Zeiyu498@merry.bluebe");
        when(trainerMapper.findById(EXISTING_USER_ID)).thenReturn(Optional.of(expectedTrainer));

        // テスト対象メソッドの呼び出し
        Optional<Trainer> actual = trainerService.findById(EXISTING_USER_ID);

        // 具体的な例外をスローするように修正
        assertThat(actual.orElseThrow(), equalTo(expectedTrainer));
    }

    @Test
    void shouldThrowExceptionWhenNonExistingUserIdIsProvided() {
        when(trainerMapper.findById(NON_EXISTING_USER_ID)).thenReturn(Optional.empty());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findById(NON_EXISTING_USER_ID));

        // 例外メッセージを定数にしておく
        final String expectedMessage = "idが" + NON_EXISTING_USER_ID + "のトレーナーはいません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void shouldReturnAllUsers() throws TrainerNotFoundException {
        // モックの設定
        List<Trainer> trainerList = Arrays.asList(
                new Trainer(1, "ゼイユ", "Zeiyu498@merry.bluebe"),
                new Trainer(2, "サザレ", "Sazare318@heisei.bluebe"),
                new Trainer(3, "ブライア", "Briar8931@usagica.bluebe"));
        when(trainerMapper.findAll()).thenReturn(trainerList);

        // テスト対象メソッドの呼び出し
        List<Trainer> trainers = trainerService.findAll();

        assertThat(trainers, equalTo(trainerList));
    }
}
