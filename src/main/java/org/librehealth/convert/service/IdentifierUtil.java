package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IdentifierUtil {

    public static Identifier getIdentifierObject(String payload) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject identifierOb = (JSONObject) parser.parse(payload);
            Identifier identifier = new Identifier();

            String uuid = (String) identifierOb.get("uuid");
            identifier.setId(uuid);

            String identifierValue = (String) identifierOb.get("identifier");
            identifier.setValue(identifierValue);

            Boolean preferred = (Boolean) identifierOb.get("preferred");
            if (preferred) {
                identifier.setUse(Identifier.IdentifierUse.USUAL);
            } else {
                identifier.setUse(Identifier.IdentifierUse.SECONDARY);
            }

            JSONObject identifierTypeOb = (JSONObject) identifierOb.get("identifierType");
            String identifierType = (String) identifierTypeOb.get("display");
            identifier.setSystem(identifierType);
            return identifier;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
