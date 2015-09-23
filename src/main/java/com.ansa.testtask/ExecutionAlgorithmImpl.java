package com.ansa.testtask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by andrey on 23.09.2015.
 */
public class ExecutionAlgorithmImpl implements ExecutionAlgorithm{
    class Record{
        private String productName;
        private LinkedList<Long> lastPrices = new LinkedList<>();

        public Record(String productName) {
            this.productName = productName;
        }
    }

    private ReentrantLock lock = new ReentrantLock();
    private Set<String> supportedProductNames = new HashSet<>();
    private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    private ConcurrentMap<String, Record> cache = new ConcurrentHashMap<>();

    public ExecutionAlgorithmImpl(String[] productNames){
        supportedProductNames = new HashSet<>(Arrays.asList(productNames));

        for (String symbol : supportedProductNames){
            locks.put(symbol, new ReentrantLock());
        }
    }

    public Trade buildTradeOrNull(Price price) {
        Trade trade = null;
        String productName =  price.getProductName();
        if (!supportedProductNames.contains(price.getProductName())){
            System.err.println("Product with name " + price.getProductName() + " is not supported");
            return null;
        }

        Record record = cache.get(price.getProductName());
        locks.get(productName).lock();

        if (record == null){
            record = new Record(price.getProductName());
            cache.put(price.getProductName(), record);
        }

        if (record.lastPrices.size() < 3){
            record.lastPrices.add(price.getPrice());
            locks.get(productName).unlock();
            return trade;
        }

        record.lastPrices.add(price.getPrice());
        if (record.lastPrices.size() > 4) {
            record.lastPrices.pollFirst();
        }

        long avg = calcAvg(record.lastPrices);

        if (avg > record.lastPrices.peek()){
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
