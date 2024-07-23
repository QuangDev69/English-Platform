package com.example.Platform.service.serviceImpl;

import com.example.Platform.dto.LevelDTO;
import com.example.Platform.entity.Level;
import com.example.Platform.repository.LevelRepository;
import com.example.Platform.service.LevelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;

    @Override
    public Level createLevel(LevelDTO levelDTO) {
        Level newLevel = Level.builder().levelName(levelDTO.getLevelName()).build();
        System.out.println("Hihi: "+newLevel.getLevelName());
        return levelRepository.save(newLevel);
    }

    @Override
    public Level getLevelById(Long id) {
        return levelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Level not found with id:" +id));
    }

    @Override
    public List<Level> getAllLevel() {
        return levelRepository.findAll();
    }

    @Override
    public void deleteLevel(Long id) {
        levelRepository.deleteById(id);
    }

    @Override
    public void updateLevel(LevelDTO levelDTO, Long id) {
        Level exLevel = getLevelById(id);
        exLevel.setLevelName(levelDTO.getLevelName());
        levelRepository.save(exLevel);
    }
}
