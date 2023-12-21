package com.trainer.name;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    private final TrainerMapper trainerMapper;

    public TrainerService(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    // GET(Read処理)
    public List<Trainer> findByNameStartingWith(String startingWith) throws TrainerNotFoundException {
        List<Trainer> trainers = trainerMapper.findByNameStartingWith(startingWith);
        if (trainers.isEmpty()) {
            throw new TrainerNotFoundException(startingWith + "で始まる名前のトレーナーは存在していません");
        }
        return trainers;
    }

    public List<Trainer> findByEmail(String email) throws TrainerNotFoundException {
        List<Trainer> trainers = trainerMapper.findByEmail(email);
        if (trainers.isEmpty()) {
            throw new TrainerNotFoundException(email + "というメールアドレスを使っているトレーナーは存在していません");
        }
        return trainers;
    }

    public List<Trainer> findByName(String name) throws TrainerNotFoundException {
        List<Trainer> trainers = trainerMapper.findByName(name);
        if (trainers.isEmpty()) {
            throw new TrainerNotFoundException(name + "という名前のトレーナーは存在していません");
        }
        return trainers;
    }

    public List<Trainer> findAll() throws TrainerNotFoundException {
        List<Trainer> trainers = trainerMapper.findAll();
        if (trainers.isEmpty()) {
            throw new TrainerNotFoundException("トレーナーはいません");
        }
        return trainers;
    }

    public Optional<Trainer> findByTrainerId(int trainerId) throws TrainerNotFoundException {
        Optional<Trainer> trainer = trainerMapper.findByTrainerId(trainerId);
        if (!trainer.isPresent()) {
            throw new TrainerNotFoundException("trainerIdが" + trainerId + "のトレーナーはいません");
        }
        return trainer;
    }

}
