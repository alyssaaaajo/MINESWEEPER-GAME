const gridConfig = {
    easy: { rows: 8, cols: 8, mines: 10 },
    medium: { rows: 12, cols: 12, mines: 20 },
    hard: { rows: 16, cols: 16, mines: 40 },
};

const minesweeper = document.getElementById("minesweeper");
const difficultySelect = document.getElementById("difficulty");
const startButton = document.getElementById("startGame");

let board = [];
let rows, cols, mines;

function createGrid(rows, cols) {
    minesweeper.innerHTML = "";
    minesweeper.style.gridTemplateColumns = `repeat(${cols}, 30px)`;
    board = Array.from({ length: rows }, () => Array(cols).fill(0));

    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            const cell = document.createElement("div");
            cell.classList.add("cell");
            cell.dataset.row = r;
            cell.dataset.col = c;
            cell.addEventListener("click", handleLeftClick);
            cell.addEventListener("contextmenu", handleRightClick);
            minesweeper.appendChild(cell);
        }
    }
}

function placeMines(mines) {
    let placedMines = 0;
    while (placedMines < mines) {
        const r = Math.floor(Math.random() * rows);
        const c = Math.floor(Math.random() * cols);
        if (board[r][c] === 0) {
            board[r][c] = "M"; // M represents a mine
            placedMines++;
        }
    }
}

function handleLeftClick(e) {
    const cell = e.target;
    const r = +cell.dataset.row;
    const c = +cell.dataset.col;

    if (board[r][c] === "M") {
        cell.classList.add("mine");
        alert("Game Over! You hit a mine.");
        revealBoard();
    } else {
        revealCell(r, c);
    }
}

function handleRightClick(e) {
    e.preventDefault();
    const cell = e.target;
    if (!cell.classList.contains("revealed")) {
        cell.classList.toggle("flag");
    }
}

function revealCell(r, c) {
    const cell = document.querySelector(`.cell[data-row="${r}"][data-col="${c}"]`);
    if (!cell || cell.classList.contains("revealed")) return;

    cell.classList.add("revealed");

    if (board[r][c] === "M") {
        cell.classList.add("mine");
    } else {
        const minesAround = countMinesAround(r, c);
        if (minesAround > 0) {
            cell.textContent = minesAround;
        } else {
            // Reveal neighboring cells
            for (let dr = -1; dr <= 1; dr++) {
                for (let dc = -1; dc <= 1; dc++) {
                    revealCell(r + dr, c + dc);
                }
            }
        }
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
            const cell = document.querySelector(`.cell[data-row="${r}"][data-col="${c}"]`);
            if (board[r][c] === "M") {
                cell.classList.add("mine");
            } else {
                cell.classList.add("revealed");
                const minesAround = countMinesAround(r, c);
                if (minesAround > 0) {
                    cell.textContent = minesAround;
                }
            }
        }
    }
}

startButton.addEventListener("click", () => {
    const difficulty = difficultySelect.value;
    const config = gridConfig[difficulty];
    rows = config.rows;
    cols = config.cols;
    mines = config.mines;

    createGrid(rows, cols);
    placeMines(mines);
});
