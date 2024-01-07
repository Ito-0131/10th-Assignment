package com.trainer.name.controller;

import com.trainer.name.entity.Trainer;
import com.trainer.name.exception.TrainerNotFoundException;
import com.trainer.name.service.TrainerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // GET(Read処理)
    @GetMapping("/trainers")
    public List<Trainer> findTrainers(
            @RequestParam(required = false) String startingWith,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) throws TrainerNotFoundException {

        if (startingWith != null) {
            return trainerService.findByNameStartingWith(startingWith);
        }

        if (name != null) {
            return trainerService.findByName(name);
        }

        if (email != null) {
            return trainerService.findByEmail(email);
        }

        return trainerService.findAll();
    }

    // GET(Read処理)
    @GetMapping("/trainers/{id}")
    public Trainer findTrainer(@PathVariable Integer id) throws TrainerNotFoundException {
        return trainerService.findById(id);
    }

    /*例：http://localhost:8080/trainers?email=Sazare318@heisei.bluebe
      例：http://localhost:8080/trainers/1
      例：http://localhost:8080/trainers
      例：http://localhost:8080/trainers?name=ゼイユ
      例：http://localhost:8080/trainers?startingWith=あ */
}
