package com.example.greenplusdb.service;

import com.example.greenplusdb.model.Consommation;
import com.example.greenplusdb.model.User;
import com.example.greenplusdb.utils.DateUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class ConsommationService {

  /*  public Double averageByPeriod(User user, Timestamp startDate, Timestamp endDate) {

        if (!startDate.isAfter(endDate)) {
            List<Consommation> consumptions = user.getConsommations();

            List<Timestamp> dates =

            return (consumptions
                    .stream()
                    .filter(e -> DateUtils.isDateAvailable(e.getStartDate, e.getEndDate(), dates))
                    .mapToDouble(consumptions::getConsumptionImpact)
                    .sum()) / (double) dates.size();
        }
        return 0.0;
    }*/
}
