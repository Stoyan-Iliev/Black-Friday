package com.store.payload.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeResponse {
    private List<DayIncome> dayIncome;
    private BigDecimal totalIncome;

    public IncomeResponse() {
        dayIncome = new ArrayList<>();
        totalIncome = BigDecimal.ZERO;
    }

    public void addDayIncome(LocalDate date, BigDecimal income) {
        dayIncome.add(new DayIncome(date, income));
        totalIncome = totalIncome.add(income);
    }

    public List<DayIncome> getDayIncome() {
        return dayIncome;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    private static class DayIncome {
        private LocalDate date;
        private BigDecimal income;

        public DayIncome(LocalDate date, BigDecimal income) {
            this.date = date;
            this.income = income;
        }

        public LocalDate getDate() {
            return date;
        }

        public BigDecimal getIncome() {
            return income;
        }
    }
}
