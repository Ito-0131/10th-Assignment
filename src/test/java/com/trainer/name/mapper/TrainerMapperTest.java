package com.trainer.name.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.trainer.name.entity.Trainer;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 指定されたメールアドレスが存在しない場合_空のOptionalが返される() {
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
    void findByName_指定された名前のユーザーが存在しない場合_空のOptionalが返される() {
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


}
