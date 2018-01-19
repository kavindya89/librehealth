package org.librehealth.convert.service;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Identifier;
import org.librehealth.convert.client.ApacheHttpClientGet;

public class FHIRAddressService {

    private String baseUrl  = "https://radiology.librehealth.io/lh-toolkit/ws/rest/v1/person/{id}/address/";
    
    public Address getAddress(String identifierId, String personId) {
        ApacheHttpClientGet apacheHttpClientGet = ApacheHttpClientGet.getInstance();
        String url = baseUrl.replace("{id}", personId);
        String addressJson = apacheHttpClientGet.executeGet(url + identifierId);
        Address address = AddressUtil.getAddressObject(addressJson);
        return address;
    }
}
