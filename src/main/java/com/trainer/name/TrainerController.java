package com.trainer.name;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    //GET(Read処理)
    @GetMapping("/trainers")
    public List<Trainer> findTrainers(
            @RequestParam(required = false) String startingWith,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer trainerId) throws TrainerNotFoundException {

        if (startingWith != null) {
            return trainerService.findByNameStartingWith(startingWith);
        }

        if (name != null) {
            return trainerService.findByName(name);
        }

        if (trainerId != null) {
            return findTrainerById(trainerId);
        }

        return trainerService.findAll();
    }

    private List<Trainer> findTrainerById(Integer trainerId) throws TrainerNotFoundException {
        Optional<Trainer> trainer = trainerService.findByTrainerId(trainerId);
        return trainer.map(Collections::singletonList).orElse(Collections.emptyList());
    }
}
