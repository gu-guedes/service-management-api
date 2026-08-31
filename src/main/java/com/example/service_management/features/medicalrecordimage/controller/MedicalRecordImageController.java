package com.example.service_management.features.medicalrecordimage.controller;

import com.example.service_management.features.medicalrecordimage.dto.MedicalRecordImageResponseDTO;
import com.example.service_management.features.medicalrecordimage.model.MedicalRecordImage;
import com.example.service_management.features.medicalrecordimage.service.MedicalRecordImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordImageController {

    private final MedicalRecordImageService imageService;

    public MedicalRecordImageController(MedicalRecordImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public List<MedicalRecordImageResponseDTO> getAll() {
        return imageService.findAll();
    }

    @GetMapping("/{medicalRecordId}/images")
    public List<MedicalRecordImageResponseDTO> getByMedicalRecordId(@PathVariable Long medicalRecordId) {
        return imageService.findByMedicalRecordId(medicalRecordId);
    }

    @PostMapping(value = "/{medicalRecordId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordImageResponseDTO upload(@PathVariable Long medicalRecordId, @RequestParam("file") MultipartFile file) {
        return imageService.upload(medicalRecordId, file);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        MedicalRecordImage image = imageService.getEntityWithImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getImageData());
    }

    @DeleteMapping("/images/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        imageService.delete(id);
    }
}
