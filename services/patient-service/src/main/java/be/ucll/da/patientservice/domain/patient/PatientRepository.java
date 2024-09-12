package be.ucll.da.patientservice.domain.patient;

import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Long> {
}
