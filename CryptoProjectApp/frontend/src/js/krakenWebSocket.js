class KrakenWebSocket {
    constructor() {
        this.ws = null;
        this.subscriptions = new Set();
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 5000;
    }

    connect() {
        this.ws = new WebSocket('wss://ws.kraken.com');

        this.ws.onopen = () => {
            console.log('Connected to Kraken WebSocket');
            this.reconnectAttempts = 0;
            // Resubscribe to all previous subscriptions
            this.subscriptions.forEach(symbol => this.subscribe(symbol));
        };

        this.ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            this.handleMessage(data);
        };

        this.ws.onclose = () => {
            console.log('WebSocket connection closed');
            this.handleReconnect();
        };

        this.ws.onerror = (error) => {
            console.error('WebSocket error:', error);
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

    subscribe(symbol) {
        if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
            console.warn('WebSocket not connected, subscription will be attempted on connection');
            this.subscriptions.add(symbol);
            return;
        }

        const subscription = {
            event: 'subscribe',
            pair: [symbol],
            subscription: {
                name: 'ticker'
            }
        };

        this.ws.send(JSON.stringify(subscription));
        this.subscriptions.add(symbol);
    }

    unsubscribe(symbol) {
        if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
            return;
        }

        const unsubscription = {
            event: 'unsubscribe',
            pair: [symbol],
            subscription: {
                name: 'ticker'
            }
        };

        this.ws.send(JSON.stringify(unsubscription));
        this.subscriptions.delete(symbol);
    }

    handleMessage(data) {
        if (Array.isArray(data)) {
            const [channelId, tickerData, channelName, pair] = data;
            
            if (tickerData && typeof tickerData === 'object') {
                this.updateCryptoTable(pair, tickerData);
            }
        }
    }

    updateCryptoTable(symbol, data) {
        const row = document.querySelector(`tr[data-symbol="${symbol}"]`);
        if (!row) return;

        const price = parseFloat(data.c[0]);
        const bid = parseFloat(data.b[0]);
        const ask = parseFloat(data.a[0]);
        const volume = parseFloat(data.v[1]);

        // Update table row
        row.querySelector('.price').textContent = `$${price.toFixed(2)}`;
        row.querySelector('.bid').textContent = `$${bid.toFixed(2)}`;
        row.querySelector('.ask').textContent = `$${ask.toFixed(2)}`;
        row.querySelector('.volume').textContent = volume.toFixed(4);

        // Update trade modal if it's open for this symbol
        const tradeModal = document.getElementById('tradeModal');
        if (tradeModal.style.display === 'block') {
            const modalTitle = document.getElementById('tradeModalTitle');
            if (modalTitle.textContent.includes(symbol)) {
                const currentPrice = document.getElementById('currentPrice');
                const tradeAmount = document.getElementById('tradeAmount');
                const tradeTotal = document.getElementById('tradeTotal');
                
                currentPrice.value = `$${price.toFixed(2)}`;
                
                // Recalculate total if amount is entered
                const amount = parseFloat(tradeAmount.value) || 0;
                if (amount > 0) {
                    tradeTotal.value = (amount * price).toFixed(2);
                }
            }
        }
    }

    disconnect() {
        if (this.ws) {
            this.ws.close();
        }
    }
} 