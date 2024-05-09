package com.jovisco.batchsample;

public record ReportingData(
    BillingData billingData,
    double billingTotal
) {}
