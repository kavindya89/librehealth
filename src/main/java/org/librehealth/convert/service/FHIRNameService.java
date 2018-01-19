package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.librehealth.convert.client.ApacheHttpClientGet;

public class FHIRNameService {

    private String baseUrl  = "https://radiology.librehealth.io/lh-toolkit/ws/rest/v1/person/{id}/name/";
    
    public HumanName getName(String identifierId, String personId) {
        ApacheHttpClientGet apacheHttpClientGet = ApacheHttpClientGet.getInstance();
        String url = baseUrl.replace("{id}", personId);
        String nameJson = apacheHttpClientGet.executeGet(url + identifierId);
        HumanName humanName = NameUtil.getNameObject(nameJson);
        return humanName;
    }
}
