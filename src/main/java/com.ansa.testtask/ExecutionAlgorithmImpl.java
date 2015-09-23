package com.ansa.testtask;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutionAlgorithmImpl implements ExecutionAlgorithm{

    private static final int LIST_SIZE = 4;
    private ConcurrentMap<String, Lock> locks = new ConcurrentHashMap<>();
    private ConcurrentMap<String, LinkedList<Long>> cache = new ConcurrentHashMap<>();

    public ExecutionAlgorithmImpl(String[] productNames){

        for (String productName : Arrays.asList(productNames)){
            locks.put(productName, new ReentrantLock());
            cache.put(productName, new LinkedList<Long>());
        }
    }

    public Trade buildTradeOrNull(Price price) {
        Trade trade = null;
        String productName =  price.getProductName();
        if (!locks.containsKey(productName)){
            System.err.println("Product with name " + price.getProductName() + " is not supported");
            return null;
        }


        locks.get(productName).lock();

        LinkedList<Long> lastPrices = cache.get(price.getProductName());

        if (lastPrices.size() < LIST_SIZE - 1){
            lastPrices.add(price.getPrice());
            locks.get(productName).unlock();
            return trade;
        }

        lastPrices.add(price.getPrice());
        if (lastPrices.size() > LIST_SIZE) {
            lastPrices.pollFirst();
        }

        long avg = calcAvg(lastPrices);

        if (avg > lastPrices.peek()){
            trade = new Trade(price.getProductName(), price.getPrice(), Direction.BUY, 1000L);
        }

        // unlock
        locks.get(productName).unlock();
        return trade;
    }

    private long calcAvg(LinkedList<Long> lastPrices) {
        long sum = 0;
        for (long price : lastPrices){
            sum +=price;
        }
        return sum/4;
    }
}
