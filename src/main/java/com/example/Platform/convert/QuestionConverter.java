package com.example.Platform.convert;

import com.example.Platform.dto.CourseDTO;
import com.example.Platform.dto.QuestionDTO;
import com.example.Platform.entity.Course;
import com.example.Platform.entity.Question;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {
    private final TypeMap<QuestionDTO, Question> typeMap;

    @Autowired
    public QuestionConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(QuestionDTO.class, Question.class);
        this.typeMap.addMappings(mapper -> {
            mapper.skip(Question::setQuestionId);
        });

    }

    public Question toEntity(QuestionDTO questionDTO) {
        return this.typeMap.map(questionDTO);

    }
}
