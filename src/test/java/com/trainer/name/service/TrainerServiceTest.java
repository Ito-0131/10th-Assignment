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
import static org.mockito.Mockito.verify;
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
    void 追加しようとしたメールアドレスが既に存在する場合にDuplicateEmailExceptionがスローされる() {
        // モックの設定
        String name = "新しいトレーナー";
        String email = "existing_email@example.com";
        when(trainerMapper.countByEmail(email)).thenReturn(1);

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(DuplicateEmailException.class, () -> trainerService.insert(name, email));
    }

    @Test
    void 追加しようとした名前が既に存在する場合にDuplicateNameExceptionがスローされる() {
        // モックの設定
        String name = "既存のトレーナー";
        String email = "new_trainer@example.com";
        when(trainerMapper.countByName(name)).thenReturn(1);

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(DuplicateNameException.class, () -> trainerService.insert(name, email));
    }

    @Test
    void 既存のユーザーIDで更新が正常に行われる場合() throws TrainerNotFoundException {
        // モックの設定
        int userId = 1;
        String newName = "新しい名前";
        String newEmail = "new_email@example.com";
        Trainer existingTrainer = new Trainer(userId, "既存の名前", "existing_email@example.com");
        when(trainerMapper.findById(userId)).thenReturn(Optional.of(existingTrainer));
        when(trainerMapper.countByEmail(newEmail)).thenReturn(0);
        when(trainerMapper.countByName(newName)).thenReturn(0);

        // テスト対象メソッドの呼び出し
        trainerService.update(userId, newName, newEmail);

        // 更新されたトレーナーが正しく保存されていることを確認
        Trainer updatedTrainer = new Trainer(userId, newName, newEmail);
        verify(trainerMapper).update(updatedTrainer);
    }

    @Test
    void 存在しないユーザーIDで更新されようとした場合に例外をスローするかどうか() {
        int userId = 999;
        String name = "名前";
        String email = "email@example.com";
        when(trainerMapper.findById(userId)).thenReturn(Optional.empty());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(TrainerNotFoundException.class, () -> trainerService.update(userId, name, email));
    }

    @Test
    void 更新しようとしたメールアドレスが既に存在する場合にDuplicateEmailExceptionがスローされるかどうか() {
        // モックの設定
        int userId = 1;
        String newName = "新しい名前";
        String newEmail = "Zeiyu498@merry.bluebe";
        Trainer existingTrainer = new Trainer(userId, "既存の名前", "existing_email@example.com");
        when(trainerMapper.findById(userId)).thenReturn(Optional.of(existingTrainer));
        when(trainerMapper.countByEmail(newEmail)).thenReturn(1);

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(DuplicateEmailException.class, () -> trainerService.update(userId, newName, newEmail));
    }

    @Test
    void 更新しようとした名前が既に存在する場合にDuplicateNameExceptionがスローされるかどうか() {
        // モックの設定
        int userId = 1;
        String newName = "ゼイユ";
        String newEmail = "Saiyan8931@moimoi.redbe";
        Trainer existingTrainer = new Trainer(userId, "既存の名前", "mamyobubo@warikan.be");
        when(trainerMapper.findById(userId)).thenReturn(Optional.of(existingTrainer));
        when(trainerMapper.countByName(newName)).thenReturn(1);

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(DuplicateNameException.class, () -> trainerService.update(userId, newName, newEmail));
    }

    @Test
    void 無効な名前で更新しようとしたときに例外を返すかどうか() {
        // モックの設定
        int userId = 1;
        String newName = null;
        String newEmail = "puku85@instmail.com";
        Trainer existingTrainer = new Trainer(userId, "既存の名前", "puku7895@instmail.uk");
        when(trainerMapper.findById(userId)).thenReturn(Optional.of(existingTrainer));

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(IllegalArgumentException.class, () -> trainerService.update(userId, newName, newEmail));
    }

    @Test
    void 無効なメールアドレスで更新しようとしたときに例外を返すかどうか() {
        // モックの設定
        int userId = 1;
        String newName = "新しい名前";
        String newEmail = null;
        Trainer existingTrainer = new Trainer(userId, "既存の名前", "puku7895@instmail.uk.co.jp");
        when(trainerMapper.findById(userId)).thenReturn(Optional.of(existingTrainer));

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(IllegalArgumentException.class, () -> trainerService.update(userId, newName, newEmail));

    }

    @Test
    void 既存のトレーナーが正常に削除される() throws TrainerNotFoundException {
        // モックの設定
        int userId = 1;
        Trainer existingTrainer = new Trainer(userId, "既存の名前", "uku7895@instmail.uk.co.jp");
        when(trainerMapper.findById(userId)).thenReturn(Optional.of(existingTrainer));

        // テスト対象メソッドの呼び出し
        trainerService.delete(userId);
    }

    @Test
    void 存在しないトレーナーを削除しようとしたときに例外を返すかどうか() {
        // モックの設定
        int userId = 999;
        when(trainerMapper.findById(userId)).thenReturn(empty());

        // テスト対象メソッドの呼び出しと例外の確認を同時に行う
        assertThrows(TrainerNotFoundException.class, () -> trainerService.delete(userId));
    }
}
