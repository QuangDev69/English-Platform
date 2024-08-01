package com.example.Platform.convert;


import com.example.Platform.dto.CourseDTO;
import com.example.Platform.entity.Course;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter {
    private final TypeMap<CourseDTO, Course> typeMap;

    @Autowired
    public CourseConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(CourseDTO.class, Course.class);
        this.typeMap.addMappings(mapper -> {
            mapper.skip(Course::setCourseId);

        });
        this.typeMap.addMappings(mapper -> {
            mapper.skip(Course::setImages);

        });
    }

    public Course toEntity(CourseDTO courseDTO) {
        Course course = new Course();
        this.typeMap.map(courseDTO, course);
        return course;
    }

    public void updateEntity(CourseDTO courseDTO, Course course) {
        this.typeMap.map(courseDTO, course);
    }
}
