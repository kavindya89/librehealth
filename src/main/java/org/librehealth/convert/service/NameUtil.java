package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.librehealth.convert.model.ModelRepresentation;

import java.util.List;

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

    public static ModelRepresentation getNameObject(HumanName humanName, String uri) {
        String id = humanName.getId();
        ModelRepresentation nameRep = new ModelRepresentation();
        JSONObject nameObj = new JSONObject();
        String display = "";
        List<StringType> givenNames = humanName.getGiven();
        for(StringType givenName : givenNames) {
            display += givenName + " ";
        }

        display = humanName.getFamily();

        nameObj.put("uuid", id);
        nameObj.put("display", display);
        JSONArray links = new JSONArray();
        JSONObject link = new JSONObject();
        String nameUri = uri + "name/" + id;
        link.put("ref", "self");
        link.put("uri", nameUri);
        links.add(link);
        nameObj.put("links", links);
        nameRep.setId(id);
        nameRep.setDisplay(display);
        nameRep.setRepresentation(nameObj);
        return nameRep;
    }

}
