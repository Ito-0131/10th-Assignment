package com.trainer.name.service;

import com.trainer.name.entity.Trainer;
import com.trainer.name.exception.DuplicateEmailException;
import com.trainer.name.exception.DuplicateNameException;
import com.trainer.name.exception.TrainerNotFoundException;
import com.trainer.name.mapper.TrainerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @InjectMocks
    TrainerService trainerService;
    @Mock
    TrainerMapper trainerMapper;
    @Captor
    ArgumentCaptor<Trainer> trainerCaptor;

    private static final int EXISTING_USER_ID = 1;
    private static final int NON_EXISTING_USER_ID = 999;

    @Test
    void 存在するユーザーIDが提供された場合にユーザーを返す() throws TrainerNotFoundException {
        // モックの設定
        Trainer expectedTrainer = new Trainer(EXISTING_USER_ID, "ゼイユ", "Zeiyu498@merry.bluebe");
        when(trainerMapper.findById(EXISTING_USER_ID)).thenReturn(Optional.of(expectedTrainer));

        // テスト対象メソッドの呼び出し
        Trainer actual = trainerService.findById(EXISTING_USER_ID);

        // 具体的な例外をスローするように修正
        assertThat(actual, equalTo(expectedTrainer));
    }

    @Test
    void 存在しないユーザーIDが提供された場合に例外をスローするかどうか() {
        when(trainerMapper.findById(NON_EXISTING_USER_ID)).thenReturn(empty());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findById(NON_EXISTING_USER_ID));

        // 例外メッセージを定数にしておく
        final String expectedMessage = "idが" + NON_EXISTING_USER_ID + "のトレーナーはいません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void その頭文字から始まるユーザーが存在する場合にユーザーを返す() throws TrainerNotFoundException {
        // モックの設定
        String prefix = "ゼ";
        List<Trainer> matchingTrainers = Arrays.asList(
                new Trainer(1, "ゼイユ", "Zeiyu498@merry.bluebe"));
        when(trainerMapper.findByNameStartingWith(prefix)).thenReturn(matchingTrainers);

        // テスト対象メソッドの呼び出し
        List<Trainer> actual = trainerService.findByNameStartingWith(prefix);

        // 期待される結果と一致することを確認
        assertThat(actual, equalTo(matchingTrainers));
    }

    @Test
    void その頭文字から始まるユーザーが存在しない場合に例外をスローする() {
        // モックの設定
        String prefix = "ク";
        when(trainerMapper.findByNameStartingWith(prefix)).thenReturn(Collections.emptyList());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findByNameStartingWith(prefix));

        // 例外メッセージを定数にしておく
        final String expectedMessage = prefix + "で始まる名前のトレーナーは存在していません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void emailが使用されている場合に該当のユーザーを返す() throws TrainerNotFoundException {
        // モックの設定
        String email = "Zeiyu498@merry.bluebe";
        List<Trainer> matchingTrainers = Arrays.asList(new Trainer(1, "ゼイユ", email));
        when(trainerMapper.findByEmail(email)).thenReturn(matchingTrainers);

        // テスト対象メソッドの呼び出し
        List<Trainer> actual = trainerService.findByEmail(email);

        // 期待される結果と一致することを確認
        assertEquals(matchingTrainers, actual);

    }

    @Test
    void emailが使用されていない場合に例外をスローする() {
        // モックの設定
        String email = "unknown@example.com";
        when(trainerMapper.findByEmail(email)).thenReturn(Collections.emptyList());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findByEmail(email));

        // 例外メッセージを定数にしておく
        final String expectedMessage = email + "というメールアドレスを使っているトレーナーは存在していません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void その名前が存在する場合に該当のユーザーを返す() throws TrainerNotFoundException {
        // モックの設定
        String name = "ゼイユ";
        List<Trainer> matchingTrainers = Collections.singletonList(
                new Trainer(1, name, "Zeiyu498@merry.bluebe"));
        when(trainerMapper.findByName(name)).thenReturn(matchingTrainers);

        // テスト対象メソッドの呼び出し
        List<Trainer> actual = trainerService.findByName(name);

        // 期待される結果と一致することを確認
        assertThat(actual, equalTo(matchingTrainers));
    }

    @Test
    void その名前が存在しない場合に例外をスローする() {
        // モックの設定
        String name = "ボタン";
        when(trainerMapper.findByName(name)).thenReturn(Collections.emptyList());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findByName(name));

        // 例外メッセージを定数にしておく
        final String expectedMessage = name + "という名前のトレーナーは存在していません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void すべてのユーザーを返すかどうか() throws TrainerNotFoundException {
        // モックの設定
        when(trainerMapper.findAll()).thenReturn(Collections.emptyList());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findAll());

        // 例外メッセージを定数にしておく
        final String expectedMessage = "トレーナーはいません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void すべてのユーザーが存在しない場合にエラーを返すかどうか() {
        // モックの設定
        when(trainerMapper.findAll()).thenReturn(Collections.emptyList());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        TrainerNotFoundException thrown = assertThrows(TrainerNotFoundException.class, () ->
                trainerService.findAll());

        // 例外メッセージを表示させる
        final String expectedMessage = "トレーナーはいません";
        assertThat(thrown.getMessage(), equalTo(expectedMessage));
    }

    @Test
    void すべてのユーザーが存在する場合に全てのユーザーを返す() throws TrainerNotFoundException {
        // モックの設定
        List<Trainer> allTrainers = Arrays.asList(
                new Trainer(1, "ユーザー1", "user1@example.com"),
                new Trainer(2, "ユーザー2", "user2@example.com")
        );
        when(trainerMapper.findAll()).thenReturn(allTrainers);

        // テスト対象メソッドの呼び出し
        List<Trainer> actual = trainerService.findAll();

        // 期待される結果と一致することを確認
        assertThat(actual, equalTo(allTrainers));
    }

    @Test
    void メールアドレスと名前が一意である場合新規トレーナーが正常に追加される() throws DuplicateEmailException, DuplicateNameException {
        // モックの設定
        String name = "新しいトレーナー";
        String email = "new_trainer@example.com";
        Trainer expectedTrainer = new Trainer(null, name, email);
        when(trainerMapper.countByEmail(email)).thenReturn(0);
        when(trainerMapper.countByName(name)).thenReturn(0);

        // テスト対象メソッドの呼び出し
        Trainer actual = trainerService.insert(name, email);

        // 期待される結果と一致することを確認
        assertEquals(expectedTrainer, actual);
    }

    @Test
    void メールアドレスが既に存在する場合にDuplicateEmailExceptionがスローされる() {
        // モックの設定
        String name = "新しいトレーナー";
        String email = "existing_email@example.com";
        when(trainerMapper.countByEmail(email)).thenReturn(1);

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(DuplicateEmailException.class, () -> trainerService.insert(name, email));
    }

    @Test
    void 名前が既に存在する場合にDuplicateNameExceptionがスローされる() {
        // モックの設定
        String name = "既存のトレーナー";
        String email = "new_trainer@example.com";
        when(trainerMapper.countByName(name)).thenReturn(1);

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(DuplicateNameException.class, () -> trainerService.insert(name, email));
    }

}
