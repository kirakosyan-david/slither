package am.example.slither.service.impl;

import am.example.slither.dto.SnakeDto;
import am.example.slither.entity.Snake;
import am.example.slither.mapper.SnakeMapper;
import am.example.slither.repository.SnakeRepository;
import am.example.slither.service.SnakeService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class SnakeServiceImpl implements SnakeService {

    private final SnakeRepository snakeRepository;
    private final SnakeMapper snakeMapper;
    private final EntityManager entityManager;
    private SnakeDto snakeDto = new SnakeDto();

    @Override
    @Transactional
    public SnakeDto saveSnake(SnakeDto snakeDto) {
        Snake snake = snakeMapper.mapToSnake(snakeDto);

        if (snakeDto.getId() > 0) {
            snake.setId(snakeDto.getId());
        }
        entityManager.flush();

        Snake savedSnake = snakeRepository.saveAndFlush(snake);
        this.snakeDto = snakeMapper.mapToDto(savedSnake);

        return this.snakeDto;
    }


    @Override
    @Transactional
    public void onFoodEaten() {
        snakeDto.setLength(snakeDto.getLength() + 1);
        snakeDto.setRank(Math.max(1, 100 - (snakeDto.getLength() / 2)));
        saveSnake(snakeDto);
    }

    @Override
    @Transactional
    public void startGame() {
        Snake lastState = snakeRepository.findAll()
                .stream()
                .max(Comparator.comparing(Snake::getId))
                .orElse(null);

        if (lastState != null) {
            snakeDto = snakeMapper.mapToDto(lastState);
        } else {
            snakeDto.setLength(1);
            snakeDto.setRank(100);
            saveSnake(snakeDto);
        }
    }

    @Override
    public SnakeDto getSnakeDto() {
        return snakeDto;
    }
}
