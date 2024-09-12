package be.ucll.da.pharmacyservice.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PharmacyService {

    public LocalDate reserveMedication(String medicationId) {
        // Medication is always reservable
        //   50% change it is available today
        //   50% change it will be here tomorrow in the next delivery

        if (Math.random() > 0.5) {
            return LocalDate.now();
        }

        return LocalDate.now().plusDays(1);
    }
}
