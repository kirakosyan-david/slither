const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

const snake = [{ x: Math.floor(canvas.width / 2), y: Math.floor(canvas.height / 2) }];
let dx = 0;
let dy = 0;
let food = [];
let speed = 5;
let score = 0;
let boost = false;

function spawnFood() {
    food.push({
        x: Math.floor(Math.random() * (canvas.width - 20) + 10),
        y: Math.floor(Math.random() * (canvas.height - 20) + 10),
        color: `hsl(${Math.random() * 360}, 100%, 50%)`
    });
}

for (let i = 0; i < 5; i++) {
    spawnFood();
}

function drawFood() {
    food.forEach(f => {
        ctx.fillStyle = f.color;
        ctx.beginPath();
        ctx.arc(f.x, f.y, 7, 0, Math.PI * 2);
        ctx.fill();
        ctx.closePath();
    });
}

function drawSnake() {
    snake.forEach((part, index) => {
        ctx.fillStyle = index === 0 ? '#4caf50' : '#76c7c0';
        ctx.beginPath();
        ctx.arc(part.x, part.y, 10, 0, Math.PI * 2);
        ctx.fill();
        ctx.closePath();
    });
}

function moveSnake() {
    const head = {
        x: snake[0].x + dx,
        y: snake[0].y + dy
    };

    if (head.x < 0 || head.x > canvas.width || head.y < 0 || head.y > canvas.height) {
        gameOver();
        return;
    }

    if (snake.some(part => part.x === head.x && part.y === head.y)) {
        gameOver();
        return;
    }

    snake.unshift(head);

    let ateFood = false;
    food = food.filter(f => {
        if (Math.hypot(f.x - head.x, f.y - head.y) < 12) {
            score++;
            console.log(`✅ Еда съедена! Новый счёт: ${score}`);
            updateScore();

            setTimeout(() => {
                console.log("✅ Отправляем данные через Wicket...");
                handleFoodEaten();
            }, 0);

            ateFood = true;
            return false;
        }
        return true;
    });

    if (ateFood) {
        spawnFood();
    } else {
        snake.pop();
    }
}

function gameOver() {
    alert(`Game Over! Your score: ${score}`);
    resetGame();
}

function resetGame() {
    snake.length = 0;
    snake.push({ x: Math.floor(canvas.width / 2), y: Math.floor(canvas.height / 2) });
    score = 0;
    dx = 0;
    dy = 0;
    food = [];
    for (let i = 0; i < 5; i++) {
        spawnFood();
    }
}

function updateScore() {
    document.getElementById('length').innerText = score;
    document.getElementById('rank').innerText = Math.max(1, Math.floor(100 - score / 2));
}

function saveGameState(score, rank) {
    if (typeof Wicket !== 'undefined' && Wicket.Ajax) {
        console.log(`✅ Сохранение данных: score = ${score}, rank = ${rank}`);
        Wicket.Ajax.post({
            u: Wicket.$('snakeForm').action,
            ep: {
                'length': score,
                'rank': rank
            },
            cache: false, // ✅ Отключаем кэширование
            onSuccess: () => {
                console.log("✅ Данные сохранены через Wicket");
                document.getElementById('length').innerText = score;
                document.getElementById('rank').innerText = rank;
            },
            onFailure: () => {
                console.error("❌ Ошибка при сохранении данных!");
            }
        });
    } else {
        console.log("❌ Wicket AJAX не доступен");
    }
}


function handleFoodEaten() {
    const rank = Math.max(1, Math.floor(100 - score / 2));
    document.getElementById('length').innerText = score;
    document.getElementById('rank').innerText = rank;

    // ✅ Сохраняем данные через Wicket
    saveGameState(score, rank);
}

function gameLoop() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawFood();
    drawSnake();
    moveSnake();

    setTimeout(gameLoop, 1000 / 30); // 30 FPS
}

canvas.addEventListener('mousemove', (event) => {
    const mouseX = event.clientX;
    const mouseY = event.clientY;

    // Вычисляем угол к курсору мыши
    const angle = Math.atan2(mouseY - snake[0].y, mouseX - snake[0].x);

    dx = Math.cos(angle) * speed;
    dy = Math.sin(angle) * speed;

    if (boost) {
        dx *= 1.5;
        dy *= 1.5;
    }
});

document.addEventListener('keydown', (event) => {
    if (event.key === ' ') {
        boost = true;
    }
});

document.addEventListener('keyup', (event) => {
    if (event.key === ' ') {
        boost = false;
    }
});

document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('form').addEventListener('submit', () => {
        console.log('✅ Форма отправлена!');
    });
    gameLoop();
});
