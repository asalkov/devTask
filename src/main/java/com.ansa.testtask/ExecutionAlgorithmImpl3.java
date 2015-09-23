package com.ansa.testtask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutionAlgorithmImpl3 implements ExecutionAlgorithm {

    class Record{
        private String productName;
        private LinkedList<Long> lastPrices = new LinkedList<>();

        public Record(String productName) {
            this.productName = productName;
        }
    }

    private ReentrantLock lock = new ReentrantLock();
    private Set<String> supportedProductNames = new HashSet<>();
    private ConcurrentHashMap<String, Boolean> productState = new ConcurrentHashMap<>();

    private ConcurrentMap<String, Record> cache = new ConcurrentHashMap<>();

    public ExecutionAlgorithmImpl3(String[] productNames){
        supportedProductNames = new HashSet<>(Arrays.asList(productNames));

        for (String symbol : supportedProductNames){
            productState.put(symbol, false);
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

        if (record == null){
            record = new Record(price.getProductName());
            cache.put(price.getProductName(), record);

        }


        lockOrWaitProduct(productName);


        if (record.lastPrices.size() < 3){
            record.lastPrices.add(price.getPrice());
            unlockProduct(productName);
            return trade;
        }

        record.lastPrices.add(price.getPrice());
        if (record.lastPrices.size() > 4) {
            record.lastPrices.pollFirst();
        }


        long avg = caclAvg(record.lastPrices);

        if (avg > record.lastPrices.peek()){
            trade = new Trade(price.getProductName(), price.getPrice(), Direction.BUY, 1000L);
        }
        // unlock
        unlockProduct(productName);
        return trade;
    }

    private long caclAvg(LinkedList<Long> lastPrices) {
        long sum = 0;
        for (long price : lastPrices){
            sum +=price;
        }
        return sum/4;
    }

    public static void log(String message){
        System.out.println(Thread.currentThread().getName() + ": " + message);

    }

    private void doLock(String productName){
        //loop while locked



    }

    private void lockOrWaitProduct(String productName){
        lock.lock();
        if (isLocked(productName)){
            lock.unlock();
            log("product is locked. wait");
            while (productState.get(productName)&&!lock.isLocked()){
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("Thread is interrupted");
                }
            }
            log("Exit wait loop");
            lock.lock();
        }

        log("locked product");

        productState.put(productName, true);
        if (lock.isLocked())
            lock.unlock();
    }
    private boolean isLocked(String productName){
        return productState.get(productName).equals(Boolean.TRUE);

    }
    private void lockProduct(String productName){
        productState.put(productName, true);
    }

    private void unlockProduct(String productName){
        log("Unlock product");
        productState.put(productName, false);
    }
}
