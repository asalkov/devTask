package com.ansa.testtask;

public interface ExecutionAlgorithm {
    /**
     * Builds a trade to be executed based on the supplied prices.
     * @param price data
     * @return trade to execute
     */
    Trade buildTradeOrNull(Price price);
}
