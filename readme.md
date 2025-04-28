```markdown
# Bitcoin Transaction Ancestry Set Calculator

## Overview
This project calculates the ancestry sets of Bitcoin transactions within a specific block and identifies the top 10 transactions with the largest ancestry sets. The ancestry set of a transaction includes all its direct and indirect parent transactions within the same block.

## Features
1. **Fetch Transactions**: Retrieves all transactions for a given Bitcoin block using the Esplora HTTP API.
2. **Ancestry Calculation**: Determines the ancestry set for each transaction in the block.
3. **Caching**: Implements caching to store API responses locally, reducing redundant API calls and avoiding rate limits.
4. **Error Handling**: Provides basic error reporting for API failures and other exceptions.
5. **Modular Design**: The code is structured into reusable and maintainable components.

## Requirements
- **Programming Language**: Java
- **Build Tool**: Maven
- **API**: Esplora HTTP API from Blockstream
- **Dependencies**:
  - `jackson-databind` for JSON parsing
  - `java.net.http` for making HTTP requests

## How It Works
1. **Fetching Transactions**:
   - The program fetches all transactions for a given block using the Esplora API.
   - Transactions are paginated and fetched iteratively.

2. **Caching**:
   - API responses are cached locally in JSON files to avoid redundant API calls.

3. **Ancestry Calculation**:
   - For each transaction, the program recursively determines its ancestry set by traversing its parent transactions.

4. **Sorting and Output**:
   - The transactions are sorted by the size of their ancestry sets.
   - The top 10 transactions with the largest ancestry sets are printed.

## Key Components
### `Main.java`
- Entry point of the application.
- Fetches transactions, calculates ancestry sets, and prints the top 10 results.

### `TransactionFetcher.java`
- Handles API calls to fetch transactions from the Esplora API.
- Implements caching logic to save and load transactions locally.

### `Transaction.java`
- Represents a Bitcoin transaction with its ID and parent transaction IDs.

### Utility Functions
- `buildAncestry`: Recursively builds the ancestry set for a given transaction.

## Example Output
```
Top 10 transactions with largest ancestry sets:

Transaction id: 7d08f0c61cda9379bdf1fa68095f827199a0d4cb6b466a6da3f0dc956772c52b with Ancestry size: 14
Transaction id: b2bab595112517e8b6a06aa9f616272b479e57e21b4da52877ddf385316aa19b with Ancestry size: 13
...
```

## How to Run
1. Clone the repository.
2. Add the required dependencies in the `pom.xml` file: **{if its not there in the pom.xml}**
   ```xml
   <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
       <version>2.15.2</version>
   </dependency>
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```
4. Run the `Main` class in your IDE or using the command line.

## Notes
- The code includes a variable named `varOcg` and a comment with the keyword `__define-ocg__` as per the problem requirements.
- Ensure you have internet access to fetch data from the Esplora API.
- Cached data is stored in the `CACHE_DIR` directory to avoid redundant API calls.

## References
- [Esplora API Documentation](https://github.com/Blockstream/esplora/blob/master/API.md)
```