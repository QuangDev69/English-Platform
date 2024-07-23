package com.example.Platform.controller;

import com.example.Platform.dto.LevelDTO;
import com.example.Platform.entity.Level;
import com.example.Platform.service.LevelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/levels")
@RequiredArgsConstructor
public class LevelController {
    private final LevelService levelService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllLevels(){
        List<Level> levels = levelService.getAllLevel();
        return ResponseEntity.ok(levels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLevelById(@PathVariable Long id){
        try {
            Level level = levelService.getLevelById(id);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createLevel(@RequestBody LevelDTO levelDTO){
        Level newLevel = levelService.createLevel(levelDTO);
        System.out.println(levelDTO.getLevelName());
        return ResponseEntity.ok("Insert category successfully: " + newLevel.getLevelName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLevel(@PathVariable Long id){
        levelService.deleteLevel(id);
        return ResponseEntity.ok("Delete Level " +id+ " successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLevel(@RequestBody LevelDTO levelDTO, @PathVariable Long id){
        levelService.updateLevel(levelDTO,id);
        return ResponseEntity.ok("Update " +levelDTO.getLevelName()+ " successfully!");
    }
}
