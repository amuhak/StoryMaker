// Audio Context and Sounds
const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

// Synthesized drum sounds
function playKick(time) {
     const osc = audioCtx.createOscillator();
     const gain = audioCtx.createGain();
     osc.type = "sine";
     osc.frequency.setValueAtTime(150, time);
    gain.gain.setValueAtTime(1, time);
    gain.gain.exponentialRampToValueAtTime(0.001, time + 0.5);
    osc.connect(gain).connect(audioCtx.destination);
    osc.start(time);
    osc.stop(time + 0.5);
}

function playSnare(time) {
    const noise = audioCtx.createBufferSource();
    const buffer = audioCtx.createBuffer(1, audioCtx.sampleRate * 0.2, audioCtx.sampleRate);
    const data = buffer.getChannelData(0);
    for (let i = 0; i < data.length; i++) {
        data[i] = Math.random() * 2 - 1; // White noise
    }
    noise.buffer = buffer;

    const gain = audioCtx.createGain();
    gain.gain.setValueAtTime(1, time);
    gain.gain.exponentialRampToValueAtTime(0.001, time + 0.2);
    noise.connect(gain).connect(audioCtx.destination);
    noise.start(time);
}

function playHiHat(time) {
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = "square";
    osc.frequency.setValueAtTime(3000, time);
    gain.gain.setValueAtTime(0.3, time);
    gain.gain.exponentialRampToValueAtTime(0.001, time + 0.1);
    osc.connect(gain).connect(audioCtx.destination);
    osc.start(time);
    osc.stop(time + 0.1);
}

function playClap(time) {
    const noise = audioCtx.createBufferSource();
    const buffer = audioCtx.createBuffer(1, audioCtx.sampleRate * 0.2, audioCtx.sampleRate);
    const data = buffer.getChannelData(0);
    for (let i = 0; i < data.length; i++) {
        data[i] = Math.random() * 2 - 1; // White noise
    }
    noise.buffer = buffer;

    const gain = audioCtx.createGain();
    gain.gain.setValueAtTime(1, time);
    gain.gain.exponentialRampToValueAtTime(0.001, time + 0.15);
    noise.connect(gain).connect(audioCtx.destination);
    noise.start(time);
}

function playTom(time) {
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = "sine";
    osc.frequency.setValueAtTime(300, time);
    gain.gain.setValueAtTime(1, time);
    gain.gain.exponentialRampToValueAtTime(0.001, time + 0.3);
    osc.connect(gain).connect(audioCtx.destination);
    osc.start(time);
    osc.stop(time + 0.3);
}

function playCowbell(time) {
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = "square";
    osc.frequency.setValueAtTime(1000, time);
    gain.gain.setValueAtTime(0.5, time);
    gain.gain.exponentialRampToValueAtTime(0.001, time + 0.1);
    osc.connect(gain).connect(audioCtx.destination);
    osc.start(time);
    osc.stop(time + 0.1);
}

// Grid Configuration
const sounds = [playKick, playSnare, playHiHat, playClap, playTom, playCowbell];
const instruments = ['🎸 Kick', '🥁 Snare', '🎶 Hi-Hat', '👏 Clap', '🍅 Tom', '🔔 Cowbell'];
const numRows = sounds.length;
const numCols = 8;
const grid = Array.from({ length: numRows }, () => Array(numCols).fill(false));

// Create Grid UI
const gridContainer = document.getElementById("beat-grid");
grid.forEach((row, rowIndex) => {
    const rowContainer = document.createElement("div");
    rowContainer.classList.add("grid-row");

    // Create instrument label
    const rowLabel = document.createElement("div");
    rowLabel.classList.add("row-label");
    rowLabel.textContent = instruments[rowIndex];
    rowContainer.appendChild(rowLabel);

    // Create buttons for the current row
    const buttonContainer = document.createElement("div");
    buttonContainer.classList.add("button-container");
    
    row.forEach((_, colIndex) => {
        const button = document.createElement("div");
        button.classList.add("grid-button");
        button.dataset.row = rowIndex;
        button.dataset.col = colIndex;
        button.addEventListener("click", () => playSoundAtSpot(button));
        buttonContainer.appendChild(button);
    });
    
    rowContainer.appendChild(buttonContainer);
    gridContainer.appendChild(rowContainer);
});

function playSoundAtSpot(button) {
    const row = button.dataset.row;
    const col = button.dataset.col;
    button.classList.toggle("active");

    // Play the corresponding sound for the clicked button
    if (button.classList.contains("active")) {
        sounds[row](audioCtx.currentTime);
    }

    grid[row][col] = !grid[row][col]; // Toggle the grid state
}

// Play and Stop Functionality
let currentStep = 0;
let playing = false;
let tempo = 120;
let intervalId = null;

function playBeat() {
    if (playing) return;
    playing = true;

    intervalId = setInterval(() => {
        grid.forEach((row, rowIndex) => {
            if (row[currentStep]) {
                sounds[rowIndex](audioCtx.currentTime);
            }
        });
        currentStep = (currentStep + 1) % numCols;
    }, (60 / tempo) * 1000 / 2);
}

function stopBeat() {
    if (!playing) return;
    playing = false;
    clearInterval(intervalId);
    currentStep = 0;
}

// Tempo Control
const tempoSlider = document.getElementById("tempo-slider");
const tempoValue = document.getElementById("tempo-value");
tempoSlider.addEventListener("input", (e) => {
    tempo = e.target.value;
    tempoValue.textContent = tempo;

    if (playing) {
        stopBeat(); // Stop the current beat
        playBeat(); // Restart with the updated tempo
    }
});

// Button Event Listeners
document.getElementById("play-btn").addEventListener("click", playBeat);
document.getElementById("stop-btn").addEventListener("click", stopBeat);