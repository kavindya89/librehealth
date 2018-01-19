package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientUtil {

    public static Patient getFHIRPatientObject(String payload) {
        try {
            FHIRIdentifierService fhirIdentifierService = new FHIRIdentifierService();
            FHIRNameService fhirNameService = new FHIRNameService();
            FHIRAddressService fhirAddressService = new FHIRAddressService();
            Patient patient = new Patient();
            JSONParser parser = new JSONParser();
            JSONObject patientOb = (JSONObject) parser.parse(payload);

            JSONObject personOb = (JSONObject) patientOb.get("person");

            String patientUuid = (String) personOb.get("uuid");
            patient.setId(patientUuid);
            String birthDate = (String) personOb.get("birthdate");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(birthDate);
            patient.setBirthDate(date);

            String gender = (String) personOb.get("gender");
            if ("M".equalsIgnoreCase(gender)) {
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            } else if ("F".equalsIgnoreCase(gender)) {
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            } else {
                patient.setGender(Enumerations.AdministrativeGender.OTHER);
            }

            boolean isVoided = (Boolean) personOb.get("voided");

            if (isVoided) {
                patient.setActive(true);
            } else {
                patient.setActive(false);
            }


             //Set identifiers
            JSONArray identifiersArray = (JSONArray) patientOb.get("identifiers");
            int identifiersSize =  identifiersArray.size();

            for (int i=0; i< identifiersSize; i++){
                JSONObject identifierObj = (JSONObject) identifiersArray.get(i);
                String identifierUuid = (String) identifierObj.get("uuid");
                patient.addIdentifier(fhirIdentifierService.getIdentifier(identifierUuid, patientUuid));
            }



            //Set addresss
            JSONObject preferredAddress = (JSONObject) personOb.get("preferredAddress");
            if (preferredAddress != null) {
                String addressUuid = (String) preferredAddress.get("uuid");
                Address address = fhirAddressService.getAddress(addressUuid, patientUuid);
                patient.addAddress(address);
            }

             //Set name
            JSONObject preferredName = (JSONObject) personOb.get("preferredName");
            if (preferredName != null) {
                String nameUuid = (String) preferredName.get("uuid");
                HumanName patientName = fhirNameService.getName(nameUuid, patientUuid);
                patient.addName(patientName);
            }
            return patient;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
