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


}
