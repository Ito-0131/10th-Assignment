package com.trainer.name.controller;

import com.trainer.name.controller.request.TrainerRequest;
import com.trainer.name.controller.response.TrainerResponse;
import com.trainer.name.entity.Trainer;
import com.trainer.name.exception.TrainerNotFoundException;
import com.trainer.name.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Validated
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

    // POST（Create処理）
    @PostMapping("/trainers")
    public ResponseEntity<TrainerResponse> insert(@Valid @RequestBody TrainerRequest trainerRequest, UriComponentsBuilder uriBuilder) {
        Trainer trainer = trainerService.insert(trainerRequest.getName(), trainerRequest.getEmail());
        URI location = uriBuilder.path("/trainers/{id}").buildAndExpand(trainer.getId()).toUri();
        TrainerResponse body = new TrainerResponse("トレーナーを作成しました");
        return ResponseEntity.created(location).body(body);
    }

    // PATCH（Update処理）
    @PatchMapping("/trainers/{id}")
    public TrainerResponse update(@PathVariable Integer id, @Valid @RequestBody TrainerRequest trainerRequest) throws TrainerNotFoundException {
        trainerService.update(id, trainerRequest.getName(), trainerRequest.getEmail());
        return new TrainerResponse("トレーナーを更新しました");
    }

    // DELETE（Delete処理）
    @DeleteMapping("/trainers/{id}")
    public TrainerResponse delete(@PathVariable Integer id) throws TrainerNotFoundException {
        trainerService.delete(id);
        return new TrainerResponse("トレーナーを削除しました");
    }
}
