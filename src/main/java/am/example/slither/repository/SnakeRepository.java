package am.example.slither.repository;

import am.example.slither.entity.Snake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnakeRepository extends JpaRepository<Snake, Integer> {
}
