package com.trainer.name.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrainerRestApiIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/trainers.yml")
    @Transactional
    void ユーザーが全件取得できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/trainers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONAssert.assertEquals(
                "[{\"id\":1,\"name\":\"ゼイユ\",\"email\":\"Zeiyu498@merry.bluebe\"}," +
                        "{\"id\":2,\"name\":\"サザレ\",\"email\":\"Sazare318@heisei.bluebe\"}," +
                        "{\"id\":3,\"name\":\"ブライア\",\"email\":\"Briar8931@usagica.bluebe\"}]",
                response, JSONCompareMode.STRICT);
    }

}

