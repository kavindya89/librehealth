package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NameUtil {

    public static HumanName getNameObject(String payload) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject nameOb = (JSONObject) parser.parse(payload);
            HumanName humanName = new HumanName();

            String uuid = (String) nameOb.get("uuid");
            humanName.setId(uuid);

            String familyName = (String) nameOb.get("familyName");
            humanName.setFamily(familyName);

            String givenName = (String) nameOb.get("givenName");
            humanName.addGiven(givenName);

            String middleName = (String) nameOb.get("middleName");
            humanName.addGiven(middleName);
            return humanName;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
