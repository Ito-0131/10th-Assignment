package com.trainer.name.mapper;

import com.trainer.name.entity.Trainer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TrainerMapper {

    @Select("SELECT * FROM trainers")
    List<Trainer> findAll();

    @Select("SELECT * FROM trainers WHERE name LIKE CONCAT(#{startingWith}, '%')")
    List<Trainer> findByNameStartingWith(String startingWith);

    @Select("SELECT * FROM trainers WHERE email = #{email}")
    List<Trainer> findByEmail(String email);

    @Select("SELECT * FROM trainers WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Trainer> findByName(String name);

    @Select("SELECT * FROM trainers WHERE trainer_id = #{trainerId}")
    Optional<Trainer> findByTrainerId(int trainerId);
}
