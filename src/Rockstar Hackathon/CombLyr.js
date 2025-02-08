class QueueSystem {
    constructor() {
        this.password = null;
        this.positionCheckInterval = null;
        this.gameHistoryInterval = null;
        this.turnTimeout = null;
        this.elements = {
            position: document.getElementById('position'),
            joinButton: document.getElementById('joinButton'),
            leaveButton: document.getElementById('leaveButton'),
            wordInput: document.getElementById('wordInput'),
            wordInputContainer: document.getElementById('wordInputContainer'),
            submitWordButton: document.getElementById('submitWordButton'),
            gameHistory: document.getElementById('gameHistory')
        };
        this.initializeEventListeners();
        this.startGameHistoryUpdates();
    }

    initializeEventListeners() {
        this.elements.joinButton.addEventListener('click', () => this.joinQueue());
        this.elements.leaveButton.addEventListener('click', () => this.leaveQueue());
        this.elements.submitWordButton.addEventListener('click', () => this.submitWord());
        window.addEventListener('beforeunload', () => this.handleBeforeUnload());
    }

    async joinQueue() {
        try {
            const response = await fetch('/queue/join', { method: 'POST' });
            const data = await response.json();
            this.password = data.Password;
            this.elements.joinButton.classList.add('hidden');
            this.startPositionCheck();
        } catch (error) {
            console.error('Failed to join queue:', error);
        }
    }

    async leaveQueue() {
        try {
            await fetch(`/queue/remove-first?password=${this.password}`, { method: 'POST' });
            this.resetQueueState();
        } catch (error) {
            console.error('Failed to leave queue:', error);
        }
    }

    async submitWord() {
        const word = this.elements.wordInput.value;
        try {
            await fetch(`/queue/remove-first?password=${this.password}&word=${word}`, {
                method: 'POST'
            });
            this.resetQueueState();
            this.elements.wordInput.value = '';
        } catch (error) {
            console.error('Failed to submit word:', error);
        }
    }

    async checkPosition() {
        if (!this.password) return;

        try {
            const response = await fetch(`/queue/get-position?password=${this.password}`);
            const position = await response.json();
            this.updateUI(position);
        } catch (error) {
            console.error('Failed to check position:', error);
            this.resetQueueState();
        }
    }

    updateUI(position) {
        this.elements.position.textContent = position;
        const isUserTurn = position === 0;
        this.elements.leaveButton.classList.toggle('hidden', !isUserTurn);
        this.elements.wordInputContainer.classList.toggle('hidden', !isUserTurn);

        if (isUserTurn) {
            this.startTurnTimer();
        } else {
            this.clearTurnTimer();
        }
    }

    resetQueueState() {
        this.password = null;
        clearInterval(this.positionCheckInterval);
        this.clearTurnTimer();
        this.elements.position.textContent = 'Not in queue';
        this.elements.joinButton.classList.remove('hidden');
        this.elements.leaveButton.classList.add('hidden');
        this.elements.wordInputContainer.classList.add('hidden');
    }

    startPositionCheck() {
        this.positionCheckInterval = setInterval(() => this.checkPosition(), 1000);
        this.checkPosition();
    }

    async updateGameHistory() {
        try {
            const response = await fetch('/data');
            const data = await response.text();
            this.elements.gameHistory.textContent = data;
        } catch (error) {
            console.error('Failed to update game history:', error);
        }
    }

    startGameHistoryUpdates() {
        this.updateGameHistory();
        this.gameHistoryInterval = setInterval(() => this.updateGameHistory(), 1000);
    }

    startTurnTimer() {
        this.clearTurnTimer();
        this.turnTimeout = setTimeout(() => {
            this.leaveQueue();
        }, 5000);
    }

    clearTurnTimer() {
        if (this.turnTimeout) {
            clearTimeout(this.turnTimeout);
            this.turnTimeout = null;
        }
    }

    async handleBeforeUnload() {
        console.log('User is leaving the page, removing from queue if necessary.');
        if (this.password) {
            await fetch(`/queue/leave?password=${this.password}`, { method: 'POST' });
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new QueueSystem();
});