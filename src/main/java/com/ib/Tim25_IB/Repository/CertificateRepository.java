package com.ib.Tim25_IB.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.Tim25_IB.model.Certificate;
import com.ib.Tim25_IB.model.CertificateStatus;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CertificateRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Certificate> getAllCertificates() throws IOException {
        File file = new File("./src/main/resources/certificate.json");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Certificate>>() {});
    }

    public List<Certificate> getAllCertificatesSN(String serialNumber) throws IOException {
        File file = new File("./src/main/resources/certificate.json");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        List<Certificate> all =  objectMapper.readValue(file, new TypeReference<List<Certificate>>() {});
        List<Certificate> returnList = new ArrayList<>();
        for(Certificate cert: all){
            if(cert.getIssuer() != null) {
                if (cert.getIssuer().equals(serialNumber)) {
                    returnList.add(cert);
                }
            }
        }
        List<Certificate> value = new ArrayList<>();
        value.addAll(returnList);
        for(Certificate cert: returnList){
            if(cert.getIssuer()!=null) {
                for (Certificate cert2 : all) {
                    if (cert2.getIssuer() != null) {
                        if(cert.getSerialNumber().equals(cert2.getIssuer())){
                            value.add(cert2);
                        }
                    }
                }
            }
        }

        return value;
    }

    public void save(Certificate certificate) throws IOException {
        File file = new File("./src/main/resources/certificate.json");
        List<Certificate> certificates = getAllCertificates();
        certificates.add(certificate);
        objectMapper.writeValue(file, certificates);
    }

    public void saveAll(List<Certificate> certificates) throws IOException {
        File file = new File("./src/main/resources/certificate.json");
        objectMapper.writeValue(file, certificates);
    }

    public Certificate findBySerialNumber(String serialNum) throws IOException {
        List<Certificate> certificates = getAllCertificates();
        for (Certificate certificate: certificates) {
            if(certificate.getSerialNumber().equals(serialNum)){
                return certificate;
            }
        }
        return null;
    }

    public List<Certificate> revokeAll(List<Certificate> list) throws IOException {
        for(Certificate cert: list){
            cert.setCertificateStatus(CertificateStatus.NOTVALID);

        }

        saveAll(list);
        return list;
    }
}
