package com.luffy.testautomation.prjhelpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Money implements Comparable<Money> {

    public BigDecimal amount;
    public String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money(String amount, String currency) {
        this.amount = BigDecimal.valueOf(MoneyHelpers.parseAmount(amount));
        this.currency = currency;
    }

    public Money(int amount, String currency) {
        this.amount = new BigDecimal(amount);
        this.currency = currency;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Money && compareTo((Money) other) == 0;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(amount).append(currency).toHashCode();
    }

    @Override
    public int compareTo(Money other) {
        if (currency.equalsIgnoreCase(other.currency)) {
            return amount.compareTo(other.amount);
        } else {
            // Comparison at this point is arbitrary without a set conversion ratio between currency
            // Seems more appropriate to just indicate difference
            return currency.compareTo(other.currency);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount.setScale(2, RoundingMode.HALF_EVEN), currency);
    }
}
