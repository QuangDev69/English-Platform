package com.example.Platform.service;

import com.example.Platform.dto.LevelDTO;
import com.example.Platform.entity.Level;

import java.util.List;

public interface LevelService {

    Level createLevel (LevelDTO levelDTO);

    Level getLevelById (Long id);

    List<Level> getAllLevel();

    void deleteLevel(Long id);

    void updateLevel(LevelDTO levelDTO, Long id);

}
