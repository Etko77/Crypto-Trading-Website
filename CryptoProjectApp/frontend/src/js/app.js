// Global variables
let currentUser = null;
let cryptocurrencies = [];
let portfolio = [];
let transactions = [];

// DOM Elements
const loginBtn = document.getElementById('loginBtn');
const registerBtn = document.getElementById('registerBtn');
const logoutBtn = document.getElementById('logoutBtn');
const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
const registerModal = new bootstrap.Modal(document.getElementById('registerModal'));
const tradeModal = new bootstrap.Modal(document.getElementById('tradeModal'));
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const tradeForm = document.getElementById('tradeForm');
const marketLink = document.getElementById('marketLink');
const portfolioLink = document.getElementById('portfolioLink');
const transactionsLink = document.getElementById('transactionsLink');
const userInfo = document.getElementById('userInfo');
const usernameDisplay = document.getElementById('usernameDisplay');
const balanceDisplay = document.getElementById('balanceDisplay');

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
    checkAuthStatus();
    loadCryptocurrencies();
    setupEventListeners();
});

function setupEventListeners() {
    loginBtn.addEventListener('click', () => loginModal.show());
    registerBtn.addEventListener('click', () => registerModal.show());
    logoutBtn.addEventListener('click', handleLogout);
    
    loginForm.addEventListener('submit', handleLogin);
    registerForm.addEventListener('submit', handleRegister);
    tradeForm.addEventListener('submit', handleTrade);
    
    marketLink.addEventListener('click', showMarketView);
    portfolioLink.addEventListener('click', showPortfolioView);
    transactionsLink.addEventListener('click', showTransactionsView);
    
    document.getElementById('tradeAmount').addEventListener('input', calculateTradeTotal);
}

// Authentication Functions
async function checkAuthStatus() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/status');
        if (response.ok) {
            const user = await response.json();
            currentUser = user;
            updateUIForLoggedInUser();
        } else {
            updateUIForLoggedOutUser();
        }
    } catch (error) {
        console.error('Error checking auth status:', error);
        updateUIForLoggedOutUser();
    }
}

async function handleLogin(event) {
    event.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        console.log('Attempting login with:', { username });
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        console.log('Login response status:', response.status);
        const data = await response.json();
        console.log('Login response data:', data);
        
        if (response.ok) {
            currentUser = data;
            loginModal.hide();
            updateUIForLoggedInUser();
            loadPortfolio();
            loadTransactions();
            showAlert('Login successful!', 'success');
        } else {
            showAlert(data || 'Login failed. Please try again.');
        }
    } catch (error) {
        console.error('Login error details:', {
            message: error.message,
            stack: error.stack,
            response: error.response
        });
        showAlert('An error occurred during login. Please try again.');
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const username = document.getElementById('registerUsername').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    
    try {
        console.log('Attempting registration with:', { username, email });
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, email, password })
        });
        
        console.log('Register response status:', response.status);
        const data = await response.json();
        console.log('Register response data:', data);
        
        if (response.ok) {
            registerModal.hide();
            showAlert('Registration successful! Please login.', 'success');
        } else {
            showAlert(data || 'Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Registration error details:', {
            message: error.message,
            stack: error.stack,
            response: error.response
        });
        showAlert('An error occurred during registration. Please try again.');
    }
}

async function handleLogout() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/logout', { 
            method: 'POST' 
        });
        
        if (response.ok) {
            currentUser = null;
            updateUIForLoggedOutUser();
            showAlert('Logged out successfully!', 'success');
        }
    } catch (error) {
        console.error('Logout error:', error);
    }
}

// Data Loading Functions
async function loadCryptocurrencies() {
    try {
        const response = await fetch('http://localhost:8080/api/crypto/top');
        if (!response.ok) {
            throw new Error('Failed to fetch cryptocurrencies');
        }
        cryptocurrencies = await response.json();
        updateCryptocurrencyTable();
        
        // Refresh data every 30 seconds
        setTimeout(loadCryptocurrencies, 30000);
    } catch (error) {
        console.error('Error loading cryptocurrencies:', error);
        showAlert('Failed to load cryptocurrency data. Please try again later.');
    }
}

async function loadPortfolio() {
    if (!currentUser) return;
    
    try {
        const response = await fetch('http://localhost:8080/api/portfolio');
        if (response.ok) {
            portfolio = await response.json();
            updatePortfolioTable();
        }
    } catch (error) {
        console.error('Error loading portfolio:', error);
    }
}

async function loadTransactions() {
    if (!currentUser) return;
    
    try {
        const response = await fetch('http://localhost:8080/api/transactions');
        if (response.ok) {
            transactions = await response.json();
            updateTransactionsTable();
        }
    } catch (error) {
        console.error('Error loading transactions:', error);
    }
}

// Trading Functions
function openTradeModal(crypto, type) {
    if (!currentUser) {
        loginModal.show();
        return;
    }

    document.getElementById('tradeModalTitle').textContent = `${type} ${crypto.symbol}`;
    document.getElementById('tradeType').value = type.toLowerCase();
    document.getElementById('currentPrice').value = typeof crypto === 'string' ? 
        cryptocurrencies.find(c => c.symbol === crypto)?.lastPrice : 
        crypto.currentPrice;
    document.getElementById('tradeAmount').value = '';
    document.getElementById('tradeTotal').value = '';
    tradeModal.show();
}

async function handleTrade(event) {
    event.preventDefault();
    if (!currentUser) return;
    
    const type = document.getElementById('tradeType').value;
    const amount = parseFloat(document.getElementById('tradeAmount').value);
    const price = parseFloat(document.getElementById('currentPrice').value);
    const symbol = document.getElementById('tradeModalTitle').textContent.split(' ')[1];
    
    if (isNaN(amount) || amount <= 0) {
        showAlert('Please enter a valid amount', 'danger');
        return;
    }
    
    try {
        const response = await fetch('http://localhost:8080/api/trades', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                symbol,
                type,
                amount,
                price
            })
        });
        
        if (response.ok) {
            tradeModal.hide();
            loadPortfolio();
            loadTransactions();
            checkAuthStatus(); // Update balance
            showAlert('Trade executed successfully!', 'success');
        } else {
            const error = await response.json();
            showAlert(error.message || 'Trade failed.', 'danger');
        }
    } catch (error) {
        console.error('Trade error:', error);
        showAlert('An error occurred during the trade.', 'danger');
    }
}

function calculateTradeTotal() {
    const amount = parseFloat(document.getElementById('tradeAmount').value) || 0;
    const price = parseFloat(document.getElementById('currentPrice').value);
    document.getElementById('tradeTotal').value = (amount * price).toFixed(2);
}

// UI Update Functions
function updateUIForLoggedInUser() {
    loginBtn.style.display = 'none';
    registerBtn.style.display = 'none';
    logoutBtn.style.display = 'block';
    userInfo.style.display = 'flex';
    usernameDisplay.textContent = currentUser.username;
    balanceDisplay.textContent = `€${currentUser.balance.toFixed(2)}`;
}

function updateUIForLoggedOutUser() {
    loginBtn.style.display = 'block';
    registerBtn.style.display = 'block';
    logoutBtn.style.display = 'none';
    userInfo.style.display = 'none';
    document.getElementById('portfolioView').style.display = 'none';
    document.getElementById('transactionsView').style.display = 'none';
    document.getElementById('marketView').style.display = 'block';
}

function updateCryptocurrencyTable() {
    const tableBody = document.getElementById('cryptoTableBody');
    tableBody.innerHTML = '';

    cryptocurrencies.forEach(crypto => {
        const row = document.createElement('tr');
        
        // Format numbers
        const lastPrice = parseFloat(crypto.lastPrice).toFixed(2);
        const volume = parseFloat(crypto.volume).toLocaleString();
        const high = parseFloat(crypto.high).toFixed(2);
        const low = parseFloat(crypto.low).toFixed(2);
        
        // Determine color for 24h change
        const change24h = crypto.change24h;
        const changeColor = change24h.startsWith('-') ? 'text-danger' : 'text-success';
        
        row.innerHTML = `
            <td>${crypto.symbol}</td>
            <td>${crypto.symbol}</td>
            <td>$${lastPrice}</td>
            <td class="${changeColor}">${change24h}</td>
            <td>$${volume}</td>
            <td>
                <button class="btn btn-sm btn-primary me-1" onclick="openTradeModal('${crypto.symbol}', 'buy')">Buy</button>
                <button class="btn btn-sm btn-danger" onclick="openTradeModal('${crypto.symbol}', 'sell')">Sell</button>
            </td>
        `;
        
        tableBody.appendChild(row);
    });
}

function updatePortfolioTable() {
    const tbody = document.getElementById('portfolioTableBody');
    tbody.innerHTML = '';
    
    portfolio.forEach(holding => {
        const row = document.createElement('tr');
        const totalValue = holding.quantity * holding.currentPrice;
        
        row.innerHTML = `
            <td>${holding.symbol}</td>
            <td>${holding.quantity.toFixed(8)}</td>
            <td>$${holding.currentPrice.toFixed(2)}</td>
            <td>$${totalValue.toFixed(2)}</td>
            <td>
                <button class="btn btn-sm btn-primary me-1" onclick="openTradeModal(${JSON.stringify(holding)}, 'BUY')">Buy</button>
                <button class="btn btn-sm btn-danger" onclick="openTradeModal(${JSON.stringify(holding)}, 'SELL')">Sell</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function updateTransactionsTable() {
    const tbody = document.getElementById('transactionsTableBody');
    tbody.innerHTML = '';
    
    transactions.forEach(transaction => {
        const row = document.createElement('tr');
        const typeClass = transaction.type === 'BUY' ? 'price-up' : 'price-down';
        
        row.innerHTML = `
            <td>${new Date(transaction.timestamp).toLocaleString()}</td>
            <td class="${typeClass}">${transaction.type}</td>
            <td>${transaction.symbol}</td>
            <td>${transaction.quantity.toFixed(8)}</td>
            <td>$${transaction.price.toFixed(2)}</td>
            <td>$${transaction.totalAmount.toFixed(2)}</td>
        `;
        tbody.appendChild(row);
    });
}

// View Navigation Functions
function showMarketView() {
    document.getElementById('marketView').style.display = 'block';
    document.getElementById('portfolioView').style.display = 'none';
    document.getElementById('transactionsView').style.display = 'none';
    updateActiveNavLink('marketLink');
}

function showPortfolioView() {
    if (!currentUser) {
        loginModal.show();
        return;
    }
    document.getElementById('marketView').style.display = 'none';
    document.getElementById('portfolioView').style.display = 'block';
    document.getElementById('transactionsView').style.display = 'none';
    updateActiveNavLink('portfolioLink');
    loadPortfolio();
}

function showTransactionsView() {
    if (!currentUser) {
        loginModal.show();
        return;
    }
    document.getElementById('marketView').style.display = 'none';
    document.getElementById('portfolioView').style.display = 'none';
    document.getElementById('transactionsView').style.display = 'block';
    updateActiveNavLink('transactionsLink');
    loadTransactions();
}

function updateActiveNavLink(activeId) {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    document.getElementById(activeId).classList.add('active');
}

// Utility Functions
function showAlert(message, type = 'danger') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
} 