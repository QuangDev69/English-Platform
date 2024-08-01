package com.example.Platform.convert;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.dto.LessonDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.entity.Lesson;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonConvert {
    private final TypeMap<LessonDTO, Lesson> typeMap;

    @Autowired

    LessonConvert(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(LessonDTO.class, Lesson.class);
        this.typeMap.addMappings(mapper -> {
            mapper.skip(Lesson::setLessonId);

        });
    }
    public Lesson toEntity(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        this.typeMap.map(lessonDTO, lesson);
        return lesson;
    }

    public void updateEntity(LessonDTO lessonDTO, Lesson lesson) {
        this.typeMap.map(lessonDTO, lesson);
    }
}
