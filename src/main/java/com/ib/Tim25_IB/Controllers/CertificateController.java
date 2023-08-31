package com.ib.Tim25_IB.Controllers;

import com.ib.Tim25_IB.DTOs.*;
import com.ib.Tim25_IB.model.Certificate;
import com.ib.Tim25_IB.model.CertificateStatus;
import com.ib.Tim25_IB.services.CertificateGenerator;
import com.ib.Tim25_IB.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    private CertificateGenerator certificateGenerator;

    @PostMapping
    public ResponseEntity<Certificate> issueCertificate(@RequestBody CertificateRequestDTO request) {

        try {
            Certificate certificate = certificateGenerator.issueCertificate(
                    request.getIssuerSN(),
                    request.getSubjectUsername(),
                    request.getKeyUsageFlags(),
                    LocalDateTime.parse(request.getValidTo())
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Certificate>> getCertificates() throws IOException {
        List<Certificate> certificates = certificateService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(certificates);
    }


    @PostMapping("/root")
    public ResponseEntity<Certificate> rootCertificate(@RequestBody RootCertifaceDTO request) {

        try {
            Certificate certificate = certificateGenerator.rootIssueCertificate(
                    request.getSubjectUsername(),
                    request.getKeyUsageFlags(),
                    LocalDateTime.parse(request.getValidTo())
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // VALIDATE A CERTIFICATE WITH ITS ID
    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validateCertificate(@RequestBody CertIdDTO id){
        boolean expired = false;
        CertificateStatus validState = CertificateStatus.NOTVALID;
        try {
            Certificate cert = certificateService.getOne(id.getSerialNumber());
            Date nowDate = Date.from(Instant.now());
            if(cert.getValidTo().after(nowDate)){
                expired = true;
            }
            validState = cert.getCertificateStatus();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        boolean valid = validState == CertificateStatus.VALID;

        return new ResponseEntity<Boolean>(valid && expired, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) throws IOException {
        // Load file as Resource

        Path filePath = Paths.get("src/main/resources/Certs/" + filename);
        Resource resource = new UrlResource(filePath.toUri());

        // Check if the file exists
        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        // Set the content type and attachment header for the response
        String contentType = Files.probeContentType(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", filename);

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Create the upload directory if it doesn't exist
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file to the server
            Path filePath = Path.of(uploadPath, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String name  = file.getOriginalFilename().split("\\.")[0];
            Certificate cert = certificateService.getOne(name);
            if(cert == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching certificate to validate!");
            }
            boolean expired = false;
            Date nowDate = Date.from(Instant.now());
            if(cert.getValidTo().after(nowDate)){
                expired = true;
            }
            boolean valid = cert.getCertificateStatus() == CertificateStatus.VALID;

            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully, certificate validity: "+ (valid && expired));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @PutMapping("/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody RevokeDTO request){
        List<Certificate> list = null;
        Certificate main = null;
        try {
            list = certificateService.getAll();
            main = certificateService.getOne(request.getSerialNumber());

            if(!request.getEmail().equals("admin")) {
                if (!main.getUsername().equals(request.getEmail())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No permission to revoke this certificate!");
                }
            }
            if(list == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No Certificates in db");
            }

            if(main == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificate doesnt exist!");
            }

            List<Certificate> finalList = certificateService.getAllBySN(request.getSerialNumber());
            finalList.add(main);
            finalList = certificateService.revokeAll(finalList);
            return ResponseEntity.status(HttpStatus.OK).body(finalList);


        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}
