package com.example.Platform.convert;

import com.example.Platform.dto.AnswerDTO;
import com.example.Platform.dto.QuestionDTO;
import com.example.Platform.entity.Answer;
import com.example.Platform.entity.Question;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnswerConverter {
    private final TypeMap<AnswerDTO, Answer> typeMap;

    @Autowired
    public AnswerConverter(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.typeMap = modelMapper.createTypeMap(AnswerDTO.class, Answer.class);
        this.typeMap.addMappings(mapper -> {
            mapper.skip(Answer::setAnswerId);

        });
    }

    public Answer toEntity(AnswerDTO answerDTO) {
        return this.typeMap.map(answerDTO);
    }

    public void updateEntity(AnswerDTO answerDTO, Answer answer) {
        this.typeMap.map(answerDTO, answer);
    }
}
