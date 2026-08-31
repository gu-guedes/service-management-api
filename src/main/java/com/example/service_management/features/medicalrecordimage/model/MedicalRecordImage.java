package com.example.service_management.features.medicalrecordimage.model;

import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "medical_record_images")
public class MedicalRecordImage {

    public MedicalRecordImage(MedicalRecord medicalRecord, String fileName, String contentType, byte[] imageData) {
        this.medicalRecord = medicalRecord;
        this.fileName = fileName;
        this.contentType = contentType;
        this.imageData = imageData;
    }

    protected MedicalRecordImage() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    // columnDefinition explicito — @Lob com Postgres cai no modo "large object" (oid)
    // por padrao em algumas combinacoes de driver/Hibernate, bytea e o jeito confiavel
    @Column(name = "image_data", nullable = false, columnDefinition = "bytea")
    private byte[] imageData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}