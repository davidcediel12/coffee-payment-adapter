package com.cordilleracoffee.paymentadapter.config;

import com.cordilleracoffee.paymentadapter.application.exception.InvalidPaymentException;

import java.util.function.Predicate;

public class IgnoreExceptionPredicate implements Predicate<Throwable> {
    @Override
    public boolean test(Throwable throwable) {
        return throwable instanceof InvalidPaymentException;
    }
}
