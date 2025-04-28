package com.machinecoding;

import com.machinecoding.Solution.models.Transaction;
import com.machinecoding.Solution.services.BackEndAPI;
import com.machinecoding.Solution.services.TransactionFetcher;

import java.util.*;

import static com.machinecoding.Solution.utils.Util.buildAncestry;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        BackEndAPI api = new TransactionFetcher();

        String blockHeight = "680000";
//         String blockHash = api.getBlockHashFromHeight(blockHeight);
        //cache the blockHash
        String blockHash = "000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732";


       List<Transaction> transactions = api.getAllTransactionsFromBlock(blockHeight, blockHash);

        Map<String, Transaction> txnMap = new HashMap<>();
        for (Transaction txn : transactions) {
            txnMap.put(txn.txnId, txn);
        }

        Map<String, Set<String>> ancestrySets = new HashMap<>();

        for (Transaction txn : transactions) {
            Set<String> varOcg = new HashSet<>();
            buildAncestry(txn.txnId, txnMap, varOcg);
            ancestrySets.put(txn.txnId, varOcg);
        }

        List<Map.Entry<String, Set<String>>> sortedList = new ArrayList<>(ancestrySets.entrySet());
        sortedList.sort((a, b) -> b.getValue().size() - a.getValue().size());

        System.out.println("\nTop 10 transactions with largest ancestry sets:\n");
        for (int i = 0; i < Math.min(10, sortedList.size()); i++) {
            String txnId = sortedList.get(i).getKey();
            int ancestrySize = sortedList.get(i).getValue().size();
            System.out.println("Transaction id: " + txnId + " with Ancestry size: " + ancestrySize);
        }
    }
}