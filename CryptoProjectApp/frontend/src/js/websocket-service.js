class KrakenWebSocket {
    constructor() {
        this.ws = null;
        this.subscriptions = new Map();
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 5000;
    }

    connect() {
        this.ws = new WebSocket('wss://ws.kraken.com/v2');

        this.ws.onopen = () => {
            console.log('Connected to Kraken WebSocket');
            this.reconnectAttempts = 0;
            // Resubscribe to any existing subscriptions
            this.subscriptions.forEach((depth, symbol) => {
                this.subscribe(symbol, depth);
            });
        };

        this.ws.onclose = () => {
            console.log('Disconnected from Kraken WebSocket');
            this.handleReconnect();
        };

        this.ws.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        this.ws.onmessage = (event) => {
            const message = JSON.parse(event.data);
            this.handleMessage(message);
        };
    }

    handleReconnect() {
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            console.log(`Attempting to reconnect (${this.reconnectAttempts}/${this.maxReconnectAttempts})...`);
            setTimeout(() => this.connect(), this.reconnectDelay);
        } else {
            console.error('Max reconnection attempts reached');
        }
    }

    subscribe(symbol, depth = 10) {
        if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
            console.error('WebSocket is not connected');
            return;
        }

        const subscription = {
            method: 'subscribe',
            params: {
                channel: 'book',
                symbol: [symbol],
                depth: depth,
                snapshot: true
            }
        };

        this.ws.send(JSON.stringify(subscription));
        this.subscriptions.set(symbol, depth);
    }

    unsubscribe(symbol) {
        if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
            return;
        }

        const subscription = {
            method: 'unsubscribe',
            params: {
                channel: 'book',
                symbol: [symbol]
            }
        };

        this.ws.send(JSON.stringify(subscription));
        this.subscriptions.delete(symbol);
    }

    handleMessage(message) {
        if (message.channel === 'book') {
            if (message.type === 'snapshot') {
                this.handleSnapshot(message.data[0]);
            } else if (message.type === 'update') {
                this.handleUpdate(message.data[0]);
            }
        }
    }

    handleSnapshot(data) {
        const symbol = data.symbol;
        const bestBid = data.bids[0]?.price || 0;
        const bestAsk = data.asks[0]?.price || 0;
        const midPrice = (parseFloat(bestBid) + parseFloat(bestAsk)) / 2;

        // Update the UI with the new data
        this.updateCryptoRow(symbol, {
            price: midPrice,
            bid: bestBid,
            ask: bestAsk,
            volume: data.bids.reduce((sum, bid) => sum + parseFloat(bid.qty), 0)
        });
    }

    handleUpdate(data) {
        const symbol = data.symbol;
        const bestBid = data.bids[0]?.price || 0;
        const bestAsk = data.asks[0]?.price || 0;
        const midPrice = (parseFloat(bestBid) + parseFloat(bestAsk)) / 2;

        // Update the UI with the new data
        this.updateCryptoRow(symbol, {
            price: midPrice,
            bid: bestBid,
            ask: bestAsk
        });
    }

    updateCryptoRow(symbol, data) {
        const row = document.querySelector(`tr[data-symbol="${symbol}"]`);
        if (row) {
            const priceCell = row.querySelector('.price');
            const bidCell = row.querySelector('.bid');
            const askCell = row.querySelector('.ask');
            const volumeCell = row.querySelector('.volume');

            if (priceCell) priceCell.textContent = `$${data.price.toFixed(2)}`;
            if (bidCell) bidCell.textContent = `$${data.bid.toFixed(2)}`;
            if (askCell) askCell.textContent = `$${data.ask.toFixed(2)}`;
            if (volumeCell && data.volume) volumeCell.textContent = data.volume.toFixed(8);
        }
    }
}

// Export the WebSocket service
window.KrakenWebSocket = KrakenWebSocket; 