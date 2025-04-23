// Mock fetch function
global.fetch = jest.fn();

// Mock DOM elements
document.body.innerHTML = `
    <div id="loginBtn"></div>
    <div id="registerBtn"></div>
    <div id="logoutBtn"></div>
    <div id="userInfo"></div>
    <div id="usernameDisplay"></div>
    <div id="balanceDisplay"></div>
    <div id="marketView"></div>
    <div id="portfolioView"></div>
    <div id="transactionsView"></div>
    <div id="cryptoTableBody"></div>
    <div id="portfolioTableBody"></div>
    <div id="transactionsTableBody"></div>
    <div class="container"></div>
`;

// Mock Bootstrap Modal
global.bootstrap = {
    Modal: jest.fn().mockImplementation(() => ({
        show: jest.fn(),
        hide: jest.fn()
    }))
};

// Import the app.js file
require('../static/app.js');

describe('Crypto Trading App', () => {
    beforeEach(() => {
        // Reset fetch mock
        fetch.mockReset();
        
        // Reset global variables
        currentUser = null;
        cryptocurrencies = [];
        portfolio = [];
        transactions = [];
    });

    describe('Authentication', () => {
        it('should handle successful login', async () => {
            const mockUser = { username: 'testuser', balance: 10000 };
            fetch.mockResolvedValueOnce({
                ok: true,
                json: () => Promise.resolve(mockUser)
            });

            await handleLogin({ preventDefault: jest.fn() });

            expect(currentUser).toEqual(mockUser);
            expect(fetch).toHaveBeenCalledWith('/api/auth/login', expect.any(Object));
        });

        it('should handle failed login', async () => {
            fetch.mockResolvedValueOnce({
                ok: false
            });

            await handleLogin({ preventDefault: jest.fn() });

            expect(currentUser).toBeNull();
        });

        it('should handle successful registration', async () => {
            fetch.mockResolvedValueOnce({
                ok: true
            });

            await handleRegister({ preventDefault: jest.fn() });

            expect(fetch).toHaveBeenCalledWith('/api/auth/register', expect.any(Object));
        });
    });

    describe('Data Loading', () => {
        it('should load cryptocurrencies', async () => {
            const mockCryptos = [
                { symbol: 'BTC', name: 'Bitcoin', currentPrice: 50000, priceChangePercentage24h: 2.5 }
            ];
            fetch.mockResolvedValueOnce({
                ok: true,
                json: () => Promise.resolve(mockCryptos)
            });

            await loadCryptocurrencies();

            expect(cryptocurrencies).toEqual(mockCryptos);
            expect(fetch).toHaveBeenCalledWith('/api/cryptocurrencies');
        });

        it('should load portfolio when user is logged in', async () => {
            currentUser = { username: 'testuser' };
            const mockPortfolio = [
                { symbol: 'BTC', quantity: 1, currentPrice: 50000 }
            ];
            fetch.mockResolvedValueOnce({
                ok: true,
                json: () => Promise.resolve(mockPortfolio)
            });

            await loadPortfolio();

            expect(portfolio).toEqual(mockPortfolio);
            expect(fetch).toHaveBeenCalledWith('/api/portfolio');
        });

        it('should not load portfolio when user is not logged in', async () => {
            await loadPortfolio();
            expect(fetch).not.toHaveBeenCalled();
        });
    });

    describe('Trading', () => {
        it('should calculate trade total correctly', () => {
            document.getElementById('tradeAmount').value = '2';
            document.getElementById('currentPrice').value = '50000';
            
            calculateTradeTotal();
            
            expect(document.getElementById('tradeTotal').value).toBe('100000.00');
        });

        it('should handle successful trade', async () => {
            currentUser = { username: 'testuser' };
            fetch.mockResolvedValueOnce({
                ok: true
            });

            await handleTrade({ preventDefault: jest.fn() });

            expect(fetch).toHaveBeenCalledWith('/api/trades', expect.any(Object));
        });
    });

    describe('UI Updates', () => {
        it('should update UI for logged in user', () => {
            currentUser = { username: 'testuser', balance: 10000 };
            
            updateUIForLoggedInUser();
            
            expect(document.getElementById('loginBtn').style.display).toBe('none');
            expect(document.getElementById('registerBtn').style.display).toBe('none');
            expect(document.getElementById('logoutBtn').style.display).toBe('block');
            expect(document.getElementById('usernameDisplay').textContent).toBe('testuser');
            expect(document.getElementById('balanceDisplay').textContent).toBe('€10000.00');
        });

        it('should update UI for logged out user', () => {
            updateUIForLoggedOutUser();
            
            expect(document.getElementById('loginBtn').style.display).toBe('block');
            expect(document.getElementById('registerBtn').style.display).toBe('block');
            expect(document.getElementById('logoutBtn').style.display).toBe('none');
            expect(document.getElementById('userInfo').style.display).toBe('none');
        });
    });
}); 