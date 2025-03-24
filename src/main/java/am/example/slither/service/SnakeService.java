package am.example.slither.service;

import am.example.slither.dto.SnakeDto;
import am.example.slither.entity.Snake;

public interface SnakeService {
    SnakeDto saveSnake(SnakeDto snakeDto);

    void onFoodEaten();

    void startGame();

    SnakeDto getSnakeDto();
}
