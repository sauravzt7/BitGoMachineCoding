package com.machinecoding.Solution.services;



import com.machinecoding.Solution.models.Transaction;

import java.util.List;

public interface BackEndAPI {
    String getBlockHashFromHeight(String blockHeight);
    List<Transaction> getAllTransactionsFromBlock(String blockHeight, String blockHash);
}