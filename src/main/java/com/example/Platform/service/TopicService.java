package com.example.Platform.service;

import com.example.Platform.dto.LevelDTO;
import com.example.Platform.dto.TopicDTO;
import com.example.Platform.entity.Level;
import com.example.Platform.entity.Topic;

import java.util.List;

public interface TopicService {


    Topic createTopic (TopicDTO topicDTO);

    Topic getTopicById (Long id);

    List<Topic> getAllTopic();

    void deleteTopic(Long id);

    void updateTopic(TopicDTO topicDTO, Long id);
}
