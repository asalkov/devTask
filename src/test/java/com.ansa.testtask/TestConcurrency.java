package com.ansa.testtask;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestConcurrency {

    private ExecutionAlgorithm executionAlgorithm;
    @Before
    public void init(){
        executionAlgorithm = new ExecutionAlgorithmImpl(new String[]{"IBM", "APPL"});
    }

    @Test
    public void notSupportedProduct(){
        Price price = new Price("A", 100L);
        Assert.assertNull(executionAlgorithm.buildTradeOrNull(price));
    }

    @Test
    public void testNoLock() throws InterruptedException {
        final Price priceOne = new Price("IBM", 100L);
        final Price priceTwo = new Price("APPL", 200L);


        Thread thOne = new Thread(new Runnable() {
            @Override
            public void run() {
                executionAlgorithm.buildTradeOrNull(priceOne);
            }
        });

        thOne.setName("thOne");

        Thread thTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                executionAlgorithm.buildTradeOrNull(priceTwo);
            }
        });

        thTwo.setName("thTwo");

        thOne.start();

        //Thread.currentThread().sleep(10);
        thTwo.start();

        thOne.join();
        thTwo.join();

    }
    @Test
    public void testLockBySymbol() throws InterruptedException {
        final Price price = new Price("IBM", 100L);


        Thread thOne = new Thread(new Runnable() {
            @Override
            public void run() {
                executionAlgorithm.buildTradeOrNull(price);
            }
        });

        thOne.setName("thOne");

        Thread thTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                executionAlgorithm.buildTradeOrNull(price);
            }
        });

        thTwo.setName("thTwo");

        thOne.start();

        //Thread.currentThread().sleep(10);
        thTwo.start();

        thOne.join();
        thTwo.join();

    }

}
