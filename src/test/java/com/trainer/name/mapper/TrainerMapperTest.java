package com.trainer.name.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.trainer.name.entity.Trainer;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainerMapperTest {
    @Autowired
    TrainerMapper trainerMapper;

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 全てのユーザーが取得できること() {
        List<Trainer> trainers = trainerMapper.findAll();

        // ログにデータを出力
        for (Trainer trainer : trainers) {
            System.out.println("ID: " + trainer.getId() + ", Name: " + trainer.getName() + ", Email: " + trainer.getEmail());
        }

        assertThat(trainers)
                .hasSize(3)
                .contains(
                        new Trainer(1, "ゼイユ", "Zeiyu498@merry.bluebe"),
                        new Trainer(2, "サザレ", "Sazare318@heisei.bluebe"),
                        new Trainer(3, "ブライア", "Briar8931@usagica.bluebe")
                );
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定された頭文字で始まるユーザーが存在する場合_ユーザーが返される() {
        // テストデータセットには "ゼ" で始まるユーザーが含まれています
        String prefix = "ゼ";

        List<Trainer> matchingTrainers = trainerMapper.findByNameStartingWith(prefix);

        assertThat(matchingTrainers)
                .hasSize(1)
                .contains(
                        new Trainer(1, "ゼイユ", "Zeiyu498@merry.bluebe")
                );
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定された頭文字で始まるユーザーが存在しない場合_空のリストが返される() {
        // テストデータセットには "ク" で始まるユーザーは含まれていません
        String prefix = "ク";

        List<Trainer> matchingTrainers = trainerMapper.findByNameStartingWith(prefix);

        assertThat(matchingTrainers).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定されたメールアドレスが存在する場合_ユーザーが返される() {
        // テストデータセットには "Zeiyu498@merry.bluebe" のメールアドレスが含まれています
        String email = "Zeiyu498@merry.bluebe";

        List<Trainer> matchingTrainers = trainerMapper.findByEmail(email);

        assertThat(matchingTrainers)
                .as("指定されたメールアドレスのユーザーがリストに含まれていることを確認します")
                .containsExactlyInAnyOrder(
                        new Trainer(1, "ゼイユ", email)
                );
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定されたメールアドレスが存在しない場合_空のListが返される() {
        // テストデータセットには存在しないメールアドレス
        String email = "nonexistent@example.com";

        List<Trainer> trainerOptional = trainerMapper.findByEmail(email);

        assertThat(trainerOptional).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定された名前のユーザーが存在する場合_ユーザーが返される() {
        // テストデータには "サザレ" の名前のユーザーが存在します
        String name = "サザレ";

        List<Trainer> matchingTrainers = trainerMapper.findByName(name);

        assertThat(matchingTrainers)
                .as("指定された名前のユーザーがリストに含まれていることを確認します")
                .containsExactlyInAnyOrder(
                        new Trainer(2, name, "Sazare318@heisei.bluebe")
                );
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void findByName_指定された名前のユーザーが存在しない場合_空のListが返される() {
        // テストデータセットには存在しない名前
        String name = "nonexistent";

        List<Trainer> trainerOptional = trainerMapper.findByName(name);

        assertThat(trainerOptional).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定されたIDのユーザーが存在する場合_ユーザーが返される() {
        // テストデータセットには ID が 3 のユーザーが含まれています
        int userId = 3;

        Optional<Trainer> trainerOptional = trainerMapper.findById(userId);

        assertThat(trainerOptional)
                .isPresent()
                .contains(
                        new Trainer(userId, "ブライア", "Briar8931@usagica.bluebe")
                );
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 指定されたIDのユーザーが存在しない場合_空のOptionalが返される() {
        // テストデータセットには存在しない ID
        int userId = 999;

        Optional<Trainer> trainerOptional = trainerMapper.findById(userId);

        assertThat(trainerOptional).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 新しいトレーナーが正常に挿入される() {
        // テストデータの作成
        Trainer newTrainer = new Trainer(null, "新しいトレーナー", "new_trainer@example.com");

        // テスト対象メソッドの呼び出し
        trainerMapper.insert(newTrainer);

        // データベースからテストデータの取得
        Trainer insertedTrainer = trainerMapper.findById(newTrainer.getId()).get();

        // 取得したデータが期待されるデータと一致することを確認
        assertEquals(newTrainer.getName(), insertedTrainer.getName());
        assertEquals(newTrainer.getEmail(), insertedTrainer.getEmail());
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 新しいトレーナーが正常に挿入されてIDが生成される() {
        // テストデータの作成
        Trainer newTrainer = new Trainer(null, "新しいトレーナー", "new_trainer@example.com");

        // テスト対象メソッドの呼び出し
        trainerMapper.insert(newTrainer);

        // IDが生成されていることを確認
        assertNotNull(newTrainer.getId());

        // データベースからテストデータの取得
        Optional<Trainer> insertedTrainer = trainerMapper.findById(newTrainer.getId());

        // 取得したデータが期待されるデータと一致することを確認
        assertTrue(insertedTrainer.isPresent());
        assertEquals(newTrainer, insertedTrainer.get());
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 重複した名前で新しいトレーナーを作成しようとすると例外がスローされる() {
        // テストデータの作成
        Trainer newTrainer = new Trainer(null, "ゼイユ", "new_email@example.com");
        // 重複エントリの確認
        assertEquals(1, trainerMapper.countByName(newTrainer.getName()));
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 重複したメールアドレスで新しいトレーナーを作成しようとすると例外がスローされる() {
        // テストデータの作成
        Trainer newTrainer = new Trainer(null, "新しいトレーナー", "Zeiyu498@merry.bluebe");
        // 重複エントリの確認
        assertEquals(1, trainerMapper.countByEmail(newTrainer.getEmail()));
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 作成しようとしたトレーナーの名前が無効な場合に例外がスローされる() {
        // 無効な名前を持つ新しいトレーナーの作成
        Trainer newTrainer = new Trainer(null, null, "new_email@example.com");
        // 例外がスローされることを検証
        assertThrows(DataIntegrityViolationException.class, () -> trainerMapper.insert(newTrainer));
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 作成しようとしたメールアドレスの情報が無効な場合に例外がスローされる() {
        // 無効なメールアドレスを持つトレーナーの作成
        Trainer newTrainer = new Trainer(null, "新しいトレーナー", null);
        // 例外がスローされることを検証
        assertThrows(DataIntegrityViolationException.class, () -> trainerMapper.insert(newTrainer));
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 既存のトレーナーが正常に更新される() {
        // テストデータの作成
        int userId = 1;
        String newName = "更新されたトレーナー";
        String newEmail = "updated_trainer@example.com";

        // テスト対象メソッドの呼び出し
        trainerMapper.update(userId, newName, newEmail);

        // データベースから更新されたトレーナーの取得
        Trainer updatedTrainer = trainerMapper.findById(userId).get();

        // 更新されたトレーナーの名前とメールアドレスが期待通りであることを確認
        assertEquals(newName, updatedTrainer.getName());
        assertEquals(newEmail, updatedTrainer.getEmail());
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 更新しようとしたトレーナーの名前が無効な場合に例外がスローされる() {
        // 無効な名前を持つ新しいトレーナーの作成
        int userId = 1;
        String newName = null;
        String newEmail = "aqwsedrf@hakusai.com";

        // 例外がスローされることを検証
        assertThrows(DataIntegrityViolationException.class, () -> trainerMapper.update(userId, newName, newEmail));

    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 更新しようとしたトレーナーのメールアドレスが無効な場合に例外がスローされる() {
        // 無効なメールアドレスを持つトレーナーの作成
        int userId = 1;
        String newName = "新しいトレーナー";
        String newEmail = null;

        // 例外がスローされることを検証
        assertThrows(DataIntegrityViolationException.class, () -> trainerMapper.update(userId, newName, newEmail));
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 更新しようとしたトレーナーのメールアドレスが他のトレーナーと重複する場合_例外がスローされる() {
        // テストデータの作成
        int userId = 1;
        String newName = "ザレ";
        String duplicatedEmail = "Briar8931@usagica.bluebe";

        // 例外がスローされることを検証
        assertThrows(DataIntegrityViolationException.class, () -> {
            // テスト対象メソッドの呼び出し
            trainerMapper.update(userId, newName, duplicatedEmail);
        });
    }

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void 更新しようとしたトレーナーの名前が他のトレーナーと重複する場合_例外がスローされる() {
        // テストデータの作成
        int userId = 1;
        String duplicatedName = "サザレ";  // 他のトレーナーと重複する名前
        String newEmail = "Briar8931@usagica.bluebe";  // 新しいメールアドレス

        // 例外がスローされることを検証
        assertThrows(DataIntegrityViolationException.class, () -> {
            // テスト対象メソッドの呼び出し
            trainerMapper.update(userId, duplicatedName, newEmail);
        });
    }

    @Test
    void 存在しないユーザーIDで更新されようとした場合に何も更新されない() {
        int nonExistentUserId = 999;
        String newName = "新しいトレーナー";
        String newEmail = "new_email@example.com";

        // 存在しないユーザーIDで更新を試みる
        int updatedRows = trainerMapper.update(nonExistentUserId, newName, newEmail);

        // 更新された行数が0であることを検証
        assertEquals(0, updatedRows);
    }


}
