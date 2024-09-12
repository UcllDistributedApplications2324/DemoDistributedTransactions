package be.ucll.da.accountingservice.domain;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccountingService {

    private Integer accountId = 0;
    private Map<Integer, Integer> patientAccountId = new HashMap<>();

    // Simulate the fact that insurance situation can change
    // Returns -1 if  not successful and patient is not insured, accountId otherwise
    public Integer openAccount(Integer patientId) {
        takesMinTenAndMaxTwentySeconds();

        if (patientAccountId.get(patientId) == null) {
            if (Math.random() > 0.3) { // Big chance to get account and be insured
                patientAccountId.put(patientId, ++accountId);
                return patientAccountId.get(patientId);
            }

            return -1;
        } else {
            if (Math.random() > 0.3) { // Big chance to lose insurance
                patientAccountId.remove(patientId);
                return -1;
            }

            return patientAccountId.get(patientId);
        }
    }

    private static void takesMinTenAndMaxTwentySeconds() {
        try {
            Thread.sleep((long) (Math.max(0.5, Math.random()) * 20000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAccount(Integer patientId) {
        patientAccountId.remove(patientId);
    }
}
