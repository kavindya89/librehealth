package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.librehealth.convert.model.ModelRepresentation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static String getFHIRPatientObject(Patient patient, String uri) {
        JSONObject patientOb = new JSONObject();
        String patientUuid = patient.getId();

        JSONObject personObj = new JSONObject();
        patientOb.put("uuid", patientUuid);
        //Set identifier
        ModelRepresentation identifierRep = null;
        String identifierDisplay = "";
        JSONArray idetifiers  = new JSONArray();
        List<Identifier> identifierList = patient.getIdentifier();
        for (Identifier identifier : identifierList) {
            identifierRep = IdentifierUtil.getIdentifierObject(identifier, uri);
            identifierDisplay = identifierRep.getDisplay();
            idetifiers.add(identifierRep.getRepresentation());
        }

        patientOb.put("identifiers", idetifiers);

        //Set name expects 1
        ModelRepresentation nameRep = null;
        String nameDisplay = "";
        List<HumanName> nameList = patient.getName();
        for (HumanName humanName : nameList) {
            nameRep = NameUtil.getNameObject(humanName, uri);
            nameDisplay = nameRep.getDisplay();
            personObj.put("preferredName", nameRep.getRepresentation());
        }

        //Set name expects 1
        ModelRepresentation addressRep;
        String addressDisplay = "";
        List<Address> addressList = patient.getAddress();
        for (Address address : addressList) {
            addressRep = AddressUtil.getAddressObject(address, uri);
            addressDisplay = addressRep.getDisplay();
            personObj.put("preferredAddress", addressRep.getRepresentation());
        }

        String display = identifierRep.getValue() + "-" + nameRep.getDisplay();
        patientOb.put("display", display);

        personObj.put("uuid", patientUuid);
        personObj.put("display", nameRep.getDisplay());

        if(patient.getGender() == Enumerations.AdministrativeGender.FEMALE) {
            personObj.put("gender", "F");
        } else {
            personObj.put("gender", "M");
        }

        if(patient.getBirthDate() != null) {
            personObj.put("birthdate", patient.getBirthDate().toString());
        }

        JSONArray personLinks = new JSONArray();
        JSONObject link = new JSONObject();
        String personUri = uri + "person/" + patientUuid;
        link.put("ref", "self");
        link.put("uri", personUri);
        personLinks.add(link);

        JSONObject fullLink = new JSONObject();
        String personFulUri = uri + "person/" + patientUuid + "?v=full";
        fullLink.put("ref", "ful");
        fullLink.put("uri", personFulUri);
        personLinks.add(fullLink);

        personObj.put("links", personLinks);

        JSONArray patientLinks = new JSONArray();
        JSONObject patientLink = new JSONObject();
        String patientUri = uri + "patient/" + patientUuid;
        patientLink.put("ref", "self");
        patientLink.put("uri", patientUri);
        patientLinks.add(patientLink);

        JSONObject patientFullLink = new JSONObject();
        String patientFullUri = uri + "patient/" + patientUuid + "?v=full";
        fullLink.put("ref", "ful");
        fullLink.put("uri", patientFullUri);
        patientLinks.add(patientFullLink);

        patientOb.put("links", patientLinks);

        patientOb.put("person", personObj);
        return patientOb.toJSONString();
    }
}
