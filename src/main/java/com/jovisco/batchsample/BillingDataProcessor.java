package com.jovisco.batchsample;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BillingDataProcessor implements ItemProcessor<BillingData, ReportingData> {

    private final PricingService pricingService;

    @Value("${spring.batchsample.pricing.data:0.01}")
    private float dataPricing;

    @Value("${spring.batchsample.pricing.call:0.5}")
    private float callPricing;

    @Value("${spring.batchsample.pricing.sms:0.1}")
    private float smsPricing;

    @Value("${spring.batchsample.spending.treshold:150}")
    private float spendingThreshold;

    @Override
    @Nullable
    public ReportingData process(@NonNull BillingData item) throws Exception {
       
        double billingTotal = item.dataUsage() * pricingService.getDataPricing() 
            + item.callDuration() * pricingService.getCallPricing()
            + item.smsCount() * pricingService.getSmsPricing();

        if (billingTotal < spendingThreshold) {
            return null;
        }

        return new ReportingData(item, billingTotal);
    }
    
}
