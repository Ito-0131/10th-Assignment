package com.trainer.name.service;

import com.trainer.name.entity.Trainer;
import com.trainer.name.exception.DuplicateEmailException;
import com.trainer.name.exception.DuplicateNameException;
import com.trainer.name.exception.TrainerNotFoundException;
import com.trainer.name.mapper.TrainerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Trainer findById(int id) throws TrainerNotFoundException {
        return trainerMapper.findById(id).orElseThrow(() -> new TrainerNotFoundException("idが" + id + "のトレーナーはいません"));
    }

    private boolean isEmailUnique(String email) {
        int count = trainerMapper.countByEmail(email);
        return count == 0;
    }

    public Trainer insert(String name, String email) throws DuplicateEmailException, DuplicateNameException {
        if (!isEmailUnique(email)) {
            throw new DuplicateEmailException("このメールアドレスは既に使用されています");
        }
        if (!isNameUnique(name)) {
            throw new DuplicateNameException("この名前は既に使用されています");
        }

        Trainer trainer = new Trainer(null, name, email);
        trainerMapper.insert(trainer);
        return trainer;
    }

    private boolean isNameUnique(String name) {
        int count = trainerMapper.countByName(name);
        return count == 0;
    }

    public void update(int id, String name, String email) throws TrainerNotFoundException, DuplicateEmailException, DuplicateNameException {
        // 既存のトレーナーを取得
        Trainer trainer = findById(id);
        if (trainer == null) {
            throw new TrainerNotFoundException("idが" + id + "のトレーナーはいません");
        }

        // パラメータの検証
        validateUpdateParameters(name, email, trainer);

        // 更新処理
        trainerMapper.update(id, name, email);
    }

    private void validateUpdateParameters(String name, String email, Trainer existingTrainer)
            throws DuplicateEmailException, DuplicateNameException {
        // 名前がnullまたは空文字の場合
        if (name == null || name.isEmpty()) {
            throw new DuplicateNameException("名前は必須です");
        }

        // メールアドレスがnullまたは空文字の場合
        if (email == null || email.isEmpty()) {
            throw new DuplicateEmailException("メールアドレスは必須です");
        }

        if (!email.equals(existingTrainer.getEmail()) && !isEmailUnique(email)) {
            throw new DuplicateEmailException("このメールアドレスは既に使用されています");
        }

        if (!name.equals(existingTrainer.getName()) && !isNameUnique(name)) {
            throw new DuplicateNameException("この名前は既に使用されています");
        }
    }

}
