package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.librehealth.convert.client.ApacheHttpClientGet;

public class FHIRIdentifierService {

    private String baseUrl  = "https://radiology.librehealth.io/lh-toolkit/ws/rest/v1/patient/{id}/identifier/";
    
    public Identifier getIdentifier(String identifierId, String personId) {
        ApacheHttpClientGet apacheHttpClientGet = ApacheHttpClientGet.getInstance();
        String url = baseUrl.replace("{id}", personId);
        String identifierJson = apacheHttpClientGet.executeGet(url + identifierId);
        Identifier identifier = IdentifierUtil.getIdentifierObject(identifierJson);
        return identifier;
    }
}
