package com.machinecoding.Solution.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.machinecoding.Solution.models.Transaction;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransactionFetcher implements BackEndAPI {

    private static final String baseUrl = "https://blockstream.info/api/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CACHE_DIR = "./cache/";

    @Override
    public String getBlockHashFromHeight(String blockHeight) {
        String url = baseUrl + "block-height/" + blockHeight;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch block hash, status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Exception while fetching block hash", e);
        }
    }

    @Override
    public List<Transaction> getAllTransactionsFromBlock(String blockHeight, String blockHash) {
        try {
            String cacheFilePath = CACHE_DIR + blockHeight + "_transactions.json";
            File cacheFile = new File(cacheFilePath);

            // If cache exists and is non-empty, load
            if (cacheFile.exists() && cacheFile.length() > 0) {
                System.out.println("Loading transactions from cache...");
                return loadTransactionsFromCache(cacheFilePath);
            } else {
                System.out.println("Fetching transactions from API and caching...");
                List<Transaction> transactions = fetchTransactionsFromApi(blockHash);

                if (!transactions.isEmpty()) {
                    saveTransactionsToCache(transactions, cacheFilePath);
                } else {
                    System.out.println("Warning: No transactions fetched, skipping cache save.");
                }
                return transactions;
            }
        } catch (IOException e) {
            throw new RuntimeException("Exception in caching logic", e);
        }
    }

    private List<Transaction> fetchTransactionsFromApi(String blockHash) {
        List<Transaction> allTransactions = new ArrayList<>();
        int startIndex = 0;
        boolean moreData = true;

        while (moreData) {
            String url = baseUrl + "block/" + blockHash + "/txs/" + startIndex;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Handling 404 it means no more transactions
                if (response.statusCode() == 404) {
                    System.out.println("No more transactions found at start index " + startIndex + ". Ending pagination.");
                    break;
                }

                if (response.statusCode() != 200) {
                    throw new RuntimeException("Failed to fetch transactions at index " + startIndex + ", status code: " + response.statusCode());
                }

                List<Map<String, Object>> transactions = objectMapper.readValue(response.body(), List.class);

                if (transactions.isEmpty()) {
                    System.out.println("Empty response at start index " + startIndex + ". Ending pagination.");
                    break;
                }

                for (Map<String, Object> tx : transactions) {
                    String txid = (String) tx.get("txid");
                    List<Map<String, Object>> vinList = (List<Map<String, Object>>) tx.get("vin");

                    List<String> parentIds = new ArrayList<>();
                    for (Map<String, Object> vin : vinList) {
                        if (vin.containsKey("txid")) {
                            parentIds.add((String) vin.get("txid"));
                        }
                    }
                    allTransactions.add(new Transaction(txid, parentIds));
                }

                startIndex += transactions.size();

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Exception while fetching transactions at start index " + startIndex, e);
            }
        }

        return allTransactions;
    }

    private void saveTransactionsToCache(List<Transaction> transactions, String cacheFilePath) throws IOException {
        File dir = new File(CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        objectMapper.writeValue(new File(cacheFilePath), transactions);
    }

    private List<Transaction> loadTransactionsFromCache(String cacheFilePath) throws IOException {
        return Arrays.asList(objectMapper.readValue(new File(cacheFilePath), Transaction[].class));
    }
}
