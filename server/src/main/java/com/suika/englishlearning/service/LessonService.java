package com.suika.englishlearning.service;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.mapper.LessonMapper;
import com.suika.englishlearning.model.Lesson;
import com.suika.englishlearning.model.UserEntity;
import com.suika.englishlearning.model.dto.lesson.LessonRequestDto;
import com.suika.englishlearning.model.dto.lesson.LessonResponseDto;
import com.suika.englishlearning.repository.LessonRepository;
import com.suika.englishlearning.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository, UserRepository userRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.lessonMapper = lessonMapper;
    }

    public LessonResponseDto createLesson(LessonRequestDto requestDto, String userName) {
        UserEntity author = userRepository.findByEmail(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Lesson lesson = lessonMapper.toEntity(requestDto, author);
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    public List<LessonResponseDto> getLessons() {
        return lessonMapper.toDtoList(lessonRepository.findAll());
    }

    public LessonResponseDto getLesson(Integer id) {
        return lessonMapper.toDto(lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found")));
    }

    public void deleteLesson(Integer id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found");
        }
        lessonRepository.deleteById(id);
    }}
