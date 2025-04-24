# Crypto Trading Platform

A real-time cryptocurrency trading platform that allows users to track and trade the top 20 cryptocurrencies using live market data from Kraken.

## Features

- **Real-time Market Data**: Track live prices, bids, asks, and volumes for the top 20 cryptocurrencies
- **User Authentication**: Secure login and registration system
- **Portfolio Management**: View and manage your cryptocurrency holdings
- **Trading Interface**: Buy and sell cryptocurrencies with real-time price updates
- **Transaction History**: Keep track of all your trades
- **Responsive Design**: Works on desktop and mobile devices

## Technologies Used

### Backend
- Java Spring Boot
- Spring Security
- MySQL Database
- WebSocket for real-time data
- RESTful API

### Frontend
- HTML5
- CSS3 (Bootstrap 5)
- JavaScript
- WebSocket API
- Fetch API

## Prerequisites

- Java JDK 17 or higher
- MySQL Server
- Node.js and npm
- Modern web browser

## Setup Instructions

### Backend Setup

1. Navigate to the backend directory:
```terminal
cd backend
```

2. Configure the database in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crypto_sim?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run the Spring Boot application:
```terminal
./mvnw spring-boot:run
```

### Frontend Setup

1. Navigate to the frontend directory:
```terminal
cd frontend
```

2. Install dependencies:
```terminal
npm install
```

3. Start the development server:
```terminal
npm start
```

4. Open your browser and navigate to:
```
http://localhost:3000
```

## Usage Guide

### Registration
1. Click the "Register" button
2. Fill in your username, email, and password
3. Submit the form to create your account

### Login
1. Click the "Login" button
2. Enter your username and password
3. Click submit to access your account

### Trading
1. View the market data in the "Market" tab
2. Click "Trade" on any cryptocurrency
3. Enter the amount you want to buy/sell
4. Review the total cost
5. Submit the trade

### Portfolio Management
1. Navigate to the "Portfolio" tab to view your holdings
2. See your current balance and cryptocurrency positions
3. Track your investment performance

### Transaction History
1. Go to the "Transactions" tab
2. View your complete trading history
3. See details of each trade including date, type, and amount

## Security Features

- Password encryption
- JWT-based authentication
- Secure WebSocket connections
- Input validation
- CSRF protection

## API Endpoints

### Authentication
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - User login
- POST `/api/auth/logout` - User logout

### Market Data
- GET `/api/crypto/top` - Get top 20 cryptocurrencies
- WebSocket `/ws/crypto` - Real-time price updates

### Trading
- POST `/api/trades` - Execute trades
- GET `/api/portfolio` - Get user portfolio
- GET `/api/transactions` - Get transaction history

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

P.S. The project is still being worked on so some of the features might be missing!
