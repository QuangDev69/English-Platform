package com.example.Platform.service.serviceImpl;

import com.example.Platform.dto.TopicDTO;
import com.example.Platform.entity.Level;
import com.example.Platform.entity.Topic;
import com.example.Platform.repository.TopicRepository;
import com.example.Platform.service.TopicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public Topic createTopic(TopicDTO topicDTO) {
        Topic newTopic = Topic.builder().topicName(topicDTO.getTopicName()).build();
        System.out.println("Hihi: "+newTopic.getTopicName());
        return topicRepository.save(newTopic);
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic not found with id:" +id));

    }

    @Override
    public List<Topic> getAllTopic() {

        return topicRepository.findAll();
    }

    @Override
    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    @Override
    public void updateTopic(TopicDTO topicDTO, Long id) {
        Topic exTopic = getTopicById(id);
        System.out.println("Topic name serv:"+exTopic.getTopicName());
        exTopic.setTopicName(topicDTO.getTopicName());
        topicRepository.save(exTopic);
    }
}
