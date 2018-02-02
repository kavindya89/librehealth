package org.librehealth.convert;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Patient;
import org.librehealth.convert.service.FHIRPatientService;

public class Main {
    public static void main(String[] args) {
        String patientUuid = "f19c6540-0058-4bb2-9019-d61dd6afd0ce";
        FhirContext ctx = FhirContext.forDstu3();
        FHIRPatientService patientService = new FHIRPatientService();
        Patient patient = patientService.getPatient(patientUuid);
        String encoded = ctx.newJsonParser().encodeResourceToString(patient);
        Patient fhirPatient = ctx.newJsonParser().parseResource(Patient.class, encoded);
        String librehealthJson = patientService.getPatient(fhirPatient);
        System.out.println("CONVERTED LIBRE HEALTH PATIENT TO FHIR");
        System.out.println(encoded);
        System.out.println("CONVERTED FHIR PATIENT TO LIBRE HEALTH");
        System.out.println(librehealthJson);
    }
}
