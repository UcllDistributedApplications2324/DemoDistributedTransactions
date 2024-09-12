package be.ucll.da.patientservice.domain.prescription;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.UUID;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prescriptionNumber;

    private Long patientId;

    private String medicationId;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    protected Prescription() {
        // For Hibernate
    }

    public Prescription(Integer patientId, String medicationId) {
        this.patientId = patientId.longValue();
        this.prescriptionNumber = UUID.randomUUID().toString();
        this.medicationId = medicationId;
        this.status = PrescriptionStatus.REQUEST_REGISTERED;
    }

    public Long getId() {
        return id;
    }

    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void ok() {
        this.status = PrescriptionStatus.ACCEPTED;
    }

    public void noInsurance() {
        this.status = PrescriptionStatus.REJECTED;
    }
}
