package be.ucll.da.patientservice.adapters.rest.incoming;

import be.ucll.da.patientservice.api.PatientApiDelegate;
import be.ucll.da.patientservice.api.model.ApiPrescription;
import be.ucll.da.patientservice.api.model.ApiPrescriptionResponse;
import be.ucll.da.patientservice.domain.prescription.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionController implements PatientApiDelegate {

    private PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @Override
    public ResponseEntity<ApiPrescriptionResponse> apiV1PatientPatientIdPrescriptionPost(Integer patientId, ApiPrescription apiPrescription) {
        ApiPrescriptionResponse response = new ApiPrescriptionResponse();
        response.prescriptionRequestNumber(prescriptionService.registerRequest(patientId, apiPrescription));

        return ResponseEntity.ok(response);
    }
}
