package com.trainer.name.mapper;

import com.trainer.name.entity.Trainer;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TrainerMapper {

    @Select("SELECT * FROM trainers")
    List<Trainer> findAll();

    @Select("SELECT * FROM trainers WHERE name LIKE CONCAT('%', #{startingWith}, '%')")
    List<Trainer> findByNameStartingWith(@Param("startingWith") String startingWith);

    @Select("SELECT * FROM trainers WHERE email = #{email}")
    List<Trainer> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM trainers WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Trainer> findByName(@Param("name") String name);

    @Select("SELECT * FROM trainers WHERE id = #{id}")
    Optional<Trainer> findById(@Param("id") int id);

    @Insert("INSERT INTO trainers (name, email) VALUES (#{name}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Trainer trainer);

    @Select("SELECT COUNT(*) FROM trainers WHERE email = #{email}")
    int countByEmail(String email);

    @Select("SELECT COUNT(*) FROM trainers WHERE name = #{name}")
    int countByName(String name);

    @Update("UPDATE trainers SET name = #{name}, email = #{email} WHERE id = #{id}")
    void update(int id, String name, String email);
}
