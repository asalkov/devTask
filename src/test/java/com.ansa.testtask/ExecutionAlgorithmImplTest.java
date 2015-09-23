package com.ansa.testtask;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExecutionAlgorithmImplTest {

    private ExecutionAlgorithm executionAlgorithm;
    @Before
    public void init(){
        executionAlgorithm = new ExecutionAlgorithmImpl(new String[]{"IBM", "APPL"});
    }

    @Test
    public void notSupportedProduct(){
        Price price = new Price("A", 100L);
        assertNull(executionAlgorithm.buildTradeOrNull(price));
    }

    @Test
    public void normal(){
        assertNull(executionAlgorithm.buildTradeOrNull(new Price("IBM", 1L)));
        assertNull(executionAlgorithm.buildTradeOrNull(new Price("IBM", 2L)));
        assertNull(executionAlgorithm.buildTradeOrNull(new Price("IBM", 3L)));
        assertNotNull(executionAlgorithm.buildTradeOrNull(new Price("IBM", 4L)));
        assertNotNull(executionAlgorithm.buildTradeOrNull(new Price("IBM", 5L)));
        executionAlgorithm.buildTradeOrNull(new Price("IBM", 6L));
        assertNull(executionAlgorithm.buildTradeOrNull(new Price("IBM", 4L)));

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
        thTwo.start();

        thOne.join();
        thTwo.join();
    }

}
