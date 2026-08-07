package com.example.service_management.features.examrequest.controller;

import com.example.service_management.features.examrequest.dto.ExamRequestRequestDTO;
import com.example.service_management.features.examrequest.dto.ExamRequestResponseDTO;
import com.example.service_management.features.examrequest.model.ExamRequest;
import com.example.service_management.features.examrequest.service.ExamRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/exam-requests")
public class ExamRequestController {

    private final ExamRequestService examRequestService;

    public ExamRequestController(ExamRequestService examRequestService) {
        this.examRequestService = examRequestService;
    }

    @GetMapping
    public List<ExamRequestResponseDTO> getAll() {
        return examRequestService.findAll();
    }

    @GetMapping("/medical-record/{medicalRecordId}")
    public List<ExamRequestResponseDTO> getByMedicalRecordId(@PathVariable Long medicalRecordId) {
        return examRequestService.findByMedicalRecordId(medicalRecordId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamRequestResponseDTO create(@Valid @RequestBody ExamRequestRequestDTO dto) {
        return examRequestService.create(dto);
    }

    @PostMapping(value = "/{id}/result", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ExamRequestResponseDTO uploadResult(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return examRequestService.uploadResult(id, file);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<byte[]> downloadResult(@PathVariable Long id) {
        ExamRequest exam = examRequestService.getEntityWithResult(id);

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(exam.getResultFileName() != null ? exam.getResultFileName() : "resultado.pdf")
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(exam.getResultFile());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        examRequestService.delete(id);
    }
}
