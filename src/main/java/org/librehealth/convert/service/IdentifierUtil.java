package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.librehealth.convert.model.ModelRepresentation;

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

    public static ModelRepresentation getIdentifierObject(Identifier identifier, String uri) {
        String id = identifier.getId();
        String system = identifier.getSystem();
        String value = identifier.getValue();
        String display = system + "=" + value;
        ModelRepresentation identifierRep = new ModelRepresentation();
        JSONObject identifierObj = new JSONObject();
        identifierObj.put("uuid", id);
        identifierObj.put("display",display);
        JSONArray links = new JSONArray();
        JSONObject link = new JSONObject();
        String identifierUri = uri + "identifier/" + id;
        link.put("ref", "self");
        link.put("uri", identifierUri);
        links.add(link);
        identifierObj.put("links", links);
        identifierRep.setId(id);
        identifierRep.setDisplay(display);
        identifierRep.setSystem(system);
        identifierRep.setValue(value);
        identifierRep.setRepresentation(identifierObj);
        return identifierRep;
    }
}
