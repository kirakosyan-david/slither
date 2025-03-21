const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

const snake = [{ x: canvas.width / 2, y: canvas.height / 2 }];

let food = [];
let speed = 5;
let dx = speed;
let dy = 0;
let score = 0;
let rank = 0;
let boost = false;

// Создаём много еды
for (let i = 0; i < 50; i++) {
    spawnFood();
}

document.addEventListener('keydown', changeDirection);
document.addEventListener('keyup', stopBoost);
canvas.addEventListener('mousemove', followMouse);

function changeDirection(event) {
    if (event.key === 'ArrowUp' && dy === 0) {
        dx = 0;
        dy = -speed;
    } else if (event.key === 'ArrowDown' && dy === 0) {
        dx = 0;
        dy = speed;
    } else if (event.key === 'ArrowLeft' && dx === 0) {
        dx = -speed;
        dy = 0;
    } else if (event.key === 'ArrowRight' && dx === 0) {
        dx = speed;
        dy = 0;
    } else if (event.key === ' ') {
        boost = true;
    }
}

function stopBoost(event) {
    if (event.key === ' ') {
        boost = false;
    }
}

function followMouse(event) {
    const angle = Math.atan2(event.clientY - snake[0].y, event.clientX - snake[0].x);
    dx = Math.cos(angle) * speed;
    dy = Math.sin(angle) * speed;
}

// Создаём случайную еду
function spawnFood() {
    food.push({
        x: Math.random() * canvas.width,
        y: Math.random() * canvas.height,
        color: `hsl(${Math.random() * 360}, 100%, 50%)`
    });
}

// Рисуем еду
function drawFood() {
    food.forEach(f => {
        ctx.fillStyle = f.color;
        ctx.beginPath();
        ctx.arc(f.x, f.y, 5, 0, Math.PI * 2);
        ctx.fill();
        ctx.closePath();
    });
}

// Рисуем змейку с градиентом
function drawSnake() {
    for (let i = 0; i < snake.length; i++) {
        const gradient = ctx.createLinearGradient(
            snake[i].x - 10,
            snake[i].y - 10,
            snake[i].x + 10,
            snake[i].y + 10
        );
        gradient.addColorStop(0, '#76c7c0');
        gradient.addColorStop(1, '#4caf50');

        ctx.fillStyle = gradient;
        ctx.beginPath();
        ctx.arc(snake[i].x, snake[i].y, 10, 0, Math.PI * 2);
        ctx.fill();
        ctx.closePath();
    }
}

// Двигаем змейку
function moveSnake() {
    let head = { x: snake[0].x + dx, y: snake[0].y + dy };

    // Ускоряем при нажатии пробела
    if (boost) {
        head.x += dx * 0.5;
        head.y += dy * 0.5;
    }

    // Столкновение с границами
    if (head.x < 0 || head.x > canvas.width || head.y < 0 || head.y > canvas.height) {
        gameOver();
        return;
    }

    // Столкновение с телом
    if (snake.some(part => part.x === head.x && part.y === head.y)) {
        gameOver();
        return;
    }

    snake.unshift(head);

    // Проверяем еду
    let ateFood = false;
    food = food.filter(f => {
        if (Math.hypot(f.x - head.x, f.y - head.y) < 10) {
            score++;
            ateFood = true;
            return false;
        }
        return true;
    });

    if (!ateFood) {
        snake.pop();
    } else {
        spawnFood();
    }
}

function gameOver() {
    alert(`Game Over! Final Length: ${score}`);
    snake.length = 0;
    snake.push({ x: canvas.width / 2, y: canvas.height / 2 });
    score = 0;
    dx = speed;
    dy = 0;
}

function updateScore() {
    document.getElementById('length').innerText = score;
    document.getElementById('rank').innerText = Math.max(1, Math.floor(100 - score / 2));
}

// Основной цикл игры
function gameLoop() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    drawFood();
    drawSnake();
    moveSnake();
    updateScore();

    setTimeout(gameLoop, 20); // Ускоряем цикл до 20 мс
}

// Запуск игры
gameLoop();


// Пример данных для таблицы лидеров
let leaderboard = [
    { name: "x3i", score: 17201 },
    { name: "Selman", score: 10775 },
    { name: "tasa srbija", score: 10222 },
    { name: "123", score: 10038 },
    { name: "burcin", score: 9970 },
    { name: "bot (bot)", score: 8641 },
    { name: "GFT", score: 7762 },
    { name: "Milo", score: 7049 },
    { name: "Snake", score: 6411 },
    { name: "Pro", score: 5913 },
];

// Функция для обновления таблицы лидеров
function updateLeaderboard() {
    const leaderboardList = document.getElementById('leaderboard-list');
    leaderboardList.innerHTML = '';

    leaderboard
        .sort((a, b) => b.score - a.score) // Сортируем по убыванию очков
        .slice(0, 10) // Оставляем только топ-10
        .forEach((player, index) => {
            const listItem = document.createElement('li');
            listItem.innerHTML = `#${index + 1} <strong>${player.name}</strong> — ${player.score}`;
            leaderboardList.appendChild(listItem);
        });
}

// Добавим игрока в таблицу при поедании еды
function addScore(name, score) {
    const existingPlayer = leaderboard.find(p => p.name === name);
    if (existingPlayer) {
        existingPlayer.score += score;
    } else {
        leaderboard.push({ name, score });
    }
    updateLeaderboard();
}

// Для теста — добавляем игрока после каждой еды
function handleFoodEaten() {
    addScore("You", 100);
}

// Запускаем обновление лидерборда каждую секунду
setInterval(updateLeaderboard, 1000);
