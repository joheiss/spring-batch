package com.jovisco.batchsample;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BillingDataRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> void onError(
        RetryContext context, 
        RetryCallback<T, E> callback,
        Throwable throwable
    ) {
        log.info("*** Error occurred: retry started");
    }

    @Override
    public <T, E extends Throwable> void onSuccess(RetryContext context, RetryCallback<T, E> callback, T result) {
        log.info("*** Retry successful");
    }
}
