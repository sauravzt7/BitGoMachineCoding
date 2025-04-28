package com.machinecoding.Solution.utils;



import com.machinecoding.Solution.models.Transaction;

import java.util.Map;
import java.util.Set;

public class Util{

    public static void buildAncestry(String txnId, Map<String, Transaction> txnMap, Set<String> ancestrySet) {
        if (!txnMap.containsKey(txnId)) return;

        for (String parent : txnMap.get(txnId).parentIds) {
            // Only explore parent if the parent is also part of this block
            if (txnMap.containsKey(parent) && !ancestrySet.contains(parent)) {
                ancestrySet.add(parent);
                buildAncestry(parent, txnMap, ancestrySet);
            }
        }
    }


}
