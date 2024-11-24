const gridConfig = {
    easy: { rows: 8, cols: 8, mines: 10 },
    medium: { rows: 12, cols: 12, mines: 20 },
    hard: { rows: 16, cols: 16, mines: 40 },
};

const minesweeper = document.getElementById("minesweeper");
const difficultySelect = document.getElementById("difficulty");
const startButton = document.getElementById("startGame");
const treasureCounter = document.getElementById("treasureCounter");

let board = [];
let rows, cols, mines;
let isFirstClick = true;
let treasuresLeft = 0;
let score = 0;
let extraLives = 0;
let flagsRemaining = 0;

startButton.addEventListener("click", () => {
    const difficulty = difficultySelect.value;
    const config = gridConfig[difficulty];
    rows = config.rows;
    cols = config.cols;
    mines = config.mines;

    isFirstClick = true;
    flagsRemaining = mines; // Reset flags based on mine count
    createGrid(rows, cols);
    updateTreasureCounter();
    updateFlagsCounter();
});

function updateFlagsCounter() {
    treasureCounter.textContent = `Treasures Left: ${treasuresLeft} | Flags Remaining: ${flagsRemaining}`;
}

function createGrid(rows, cols) {
    minesweeper.innerHTML = "";
    minesweeper.style.gridTemplateColumns = `repeat(${cols}, 30px)`;
    board = Array.from({ length: rows }, () => Array(cols).fill(0));

    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            const cell = document.createElement("div");
            cell.classList.add("cell");

            if ((r + c) % 2 === 0) {
                cell.classList.add("dark-green");
            } else {
                cell.classList.add("light-green");
            }

            cell.dataset.row = r;
            cell.dataset.col = c;
            cell.addEventListener("click", handleLeftClick);
            cell.addEventListener("contextmenu", handleRightClick);
            minesweeper.appendChild(cell);
        }
    }
}

function placeMines(mines, safeRow, safeCol) {
    let placedMines = 0;
    const safeZone = new Set();

    for (let dr = -1; dr <= 1; dr++) {
        for (let dc = -1; dc <= 1; dc++) {
            const nr = safeRow + dr;
            const nc = safeCol + dc;
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                safeZone.add(`${nr},${nc}`);
            }
        }
    }

    while (placedMines < mines) {
        const r = Math.floor(Math.random() * rows);
        const c = Math.floor(Math.random() * cols);

        if (board[r][c] === 0 && !safeZone.has(`${r},${c}`)) {
            board[r][c] = "M";
            placedMines++;
        }
    }
}

function placeTreasures(numTreasures) {
    let placedTreasures = 0;
    treasuresLeft = numTreasures;

    while (placedTreasures < numTreasures) {
        const r = Math.floor(Math.random() * rows);
        const c = Math.floor(Math.random() * cols);

        if (board[r][c] === 0) {
            board[r][c] = "T";
            placedTreasures++;
        }
    }
}

function handleLeftClick(e) {
    const cell = e.target;
    const r = +cell.dataset.row;
    const c = +cell.dataset.col;

    if (cell.classList.contains("revealed") || cell.classList.contains("flag")) return;

    if (isFirstClick) {
        isFirstClick = false;

        placeMines(mines, r, c);
        placeTreasures(3);
        revealSafeArea(r, c);
    } else {
        if (board[r][c] === "M") {
            if (extraLives > 0) {
                extraLives--;
                alert("You hit a mine! But you had an extra life.");
            } else {
                cell.classList.add("mine");
                alert("Game Over! You hit a mine.");
                revealBoard();
            }
        } else if (board[r][c] === "T") {
            revealCell(r, c);
            grantTreasureReward();
        } else {
            revealCell(r, c);
        }
    }
    updateTreasureCounter();
}

function handleRightClick(e) {
    e.preventDefault();
    const cell = e.target;
    const r = +cell.dataset.row;
    const c = +cell.dataset.col;

    // Prevent flagging if the board hasn't been initialized (before first click)
    if (isFirstClick) return;

    // Allow flags only on unrevealed cells
    if (cell.classList.contains("revealed")) return;

    if (cell.classList.contains("flag")) {
        // Remove flag
        cell.classList.remove("flag");
        cell.textContent = ""; // Remove 🚩
        flagsRemaining++;
    } else if (flagsRemaining > 0) {
        // Add flag
        cell.classList.add("flag");
        cell.textContent = "🚩"; // Add 🚩
        flagsRemaining--;
    }

    updateFlagsCounter(); // Update flags counter
}


function checkWinCondition() {
    let correctFlags = 0;

    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            const cell = document.querySelector(`.cell[data-row="${r}"][data-col="${c}"]`);
            if (cell.classList.contains("flag") && board[r][c] === "M") {
                correctFlags++;
            }
        }
    }

    if (correctFlags === mines && flagsRemaining === 0) {
        alert("Congratulations! You've flagged all the mines correctly!");
    }
}

function revealCell(r, c) {
    if (r < 0 || r >= rows || c < 0 || c >= cols) return;
    const cell = document.querySelector(`.cell[data-row="${r}"][data-col="${c}"]`);
    if (!cell || cell.classList.contains("revealed")) return;

    cell.classList.add("revealed");

    if (board[r][c] === "M") {
        cell.classList.add("mine");
    } else if (board[r][c] === "T") {
        cell.classList.add("treasure");
        cell.textContent = "💎";
        treasuresLeft--;
    } else {
        const minesAround = countMinesAround(r, c);
        if (minesAround > 0) {
            cell.textContent = minesAround;
        } else {
            revealSafeArea(r, c);
        }
    }
}

function revealSafeArea(r, c) {
    const queue = [[r, c]];
    while (queue.length > 0) {
        const [curR, curC] = queue.shift();
        if (curR < 0 || curR >= rows || curC < 0 || curC >= cols) continue;

        const cell = document.querySelector(`.cell[data-row="${curR}"][data-col="${curC}"]`);
        if (!cell || cell.classList.contains("revealed")) continue;

        revealCell(curR, curC);

        if (countMinesAround(curR, curC) === 0) {
            for (let dr = -1; dr <= 1; dr++) {
                for (let dc = -1; dc <= 1; dc++) {
                    if (dr !== 0 || dc !== 0) queue.push([curR + dr, curC + dc]);
                }
            }
        }
    }
}

function grantTreasureReward() {
    const rewardOptions = ["extraLife", "bonusPoints", "safeZone"];
    const reward = rewardOptions[Math.floor(Math.random() * rewardOptions.length)];

    switch (reward) {
        case "extraLife":
            extraLives++;
            alert("You found a treasure! Extra life granted.");
            break;
        case "bonusPoints":
            score += 50;
            alert("You found a treasure! 50 bonus points awarded.");
            break;
        case "safeZone":
            alert("You found a treasure! Safe zone revealed.");
            break;
    }
}

function countMinesAround(r, c) {
    let count = 0;
    for (let dr = -1; dr <= 1; dr++) {
        for (let dc = -1; dc <= 1; dc++) {
            const nr = r + dr;
            const nc = c + dc;
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] === "M") {
                count++;
            }
        }
    }
    return count;
}

function revealBoard() {
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            revealCell(r, c);
        }
    }
}

function updateTreasureCounter() {
    treasureCounter.textContent = `Treasures Left: ${treasuresLeft}`;
}
