package com.example.Platform.controller;


import com.example.Platform.dto.TopicDTO;
import com.example.Platform.entity.Level;
import com.example.Platform.entity.Topic;
import com.example.Platform.service.TopicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTopics(){
        List<Topic> topics = topicService.getAllTopic();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable Long id){
        try {
            Topic topic = topicService.getTopicById(id);
            return new ResponseEntity<>(topic, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createTopic(@RequestBody TopicDTO topicDTO){
        Topic newTopic = topicService.createTopic(topicDTO);
        System.out.println(topicDTO.getTopicName());
        return ResponseEntity.ok("Insert category successfully: " + newTopic.getTopicName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id){
        topicService.deleteTopic(id);
        return ResponseEntity.ok("Delete Topic " +id+ " successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTopic(@RequestBody TopicDTO topicDTO, @PathVariable Long id){
        topicService.updateTopic(topicDTO, id);
        System.out.println("Topic name con:"+topicDTO.getTopicName());

        return ResponseEntity.ok("Update " +id+ " successfully!");
    }

}
