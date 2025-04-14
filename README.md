## Cryptocurrency Portfolio Management
This is a Spring Boot application that allows users to manage their cryptocurrency portfolios. It interacts with the CoinMarketCap API to fetch the latest cryptocurrency data and provides functionalities to view and manage portfolios, coins, and wallets.

### Table of Contents
1. Features
2. Technologies
3. Getting started
3. API Overview
4. Controllers
    1. CoinController
    2. PortfolioController
    3. UserController
    4. WalletController

### Features
- Fetch the latest cryptocurrency data from CoinMarketCap API.
- View and manage cryptocurrency portfolio.
- Add coins to wallets.
- User registration and authentication.
- View and manage wallets.

### Technologies
- Java 11
- Spring Boot
- Spring Security
- Thymeleaf
- CoinMarketCap API

### Getting Started
Prerequisites:
- Java 11 or above

Update the CoinMarketCap API key in CoinMarketCapAPI class:

````
private static final String API_KEY = "your-coinmarketcap-api-key";
````
````
Open your browser and navigate to http://localhost:8080.
````

### API Overview
CoinMarketCapAPI
This class handles fetching the latest cryptocurrency data from the CoinMarketCap API.


### Controllers
#### CoinController
Handles requests related to coins.

````
GET /coin: Fetches all coins and displays them.
GET /coin/add: Displays the form to add a coin to a wallet.
````

#### PortfolioController
Handles requests related to the user's portfolio.

````
GET /home: Displays the user's portfolio summary.
GET /latest: Returns the latest portfolio entries.
````

#### UserController
Handles user registration and updates.

````
PATCH /user/user: Updates user details.
GET /user/add: Displays the form to add a new user.
POST /user/add: Handles user registration.
````

#### WalletController
Handles requests related to wallets.

````
GET /wallet: Displays all wallets for the logged-in user.
GET /wallet/add: Displays the form to add a new wallet.
POST /wallet/add: Adds a new wallet.
DELETE /wallet/delete/{id}: Deletes a wallet by ID.
GET /wallet/show: Shows details of a specific wallet.
POST /wallet/add-coin: Adds a coin to a wallet.
DELETE /wallet/delete-coin: Removes a coin from a wallet.
````