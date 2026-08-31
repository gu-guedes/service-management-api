package com.example.service_management.features.examrequest.model;

import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "exam_requests")
public class ExamRequest {

    public ExamRequest(MedicalRecord medicalRecord, String examName, LocalDate requestedDate) {
        this.medicalRecord = medicalRecord;
        this.examName = examName;
        this.requestedDate = requestedDate;
    }

    protected ExamRequest() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Column(name = "exam_name", nullable = false)
    private String examName;

    // opcional — se nao vier, assume a data do registro (ver onCreate)
    @Column(name = "requested_date")
    private LocalDate requestedDate;

    // columnDefinition explicito — @Lob com Postgres cai no modo "large object" (oid)
    // por padrao em algumas combinacoes de driver/Hibernate, bytea e o jeito confiavel
    @Column(name = "result_file", columnDefinition = "bytea")
    private byte[] resultFile;

    @Column(name = "result_file_name")
    private String resultFileName;

    @Column(name = "result_uploaded_at")
    private OffsetDateTime resultUploadedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = OffsetDateTime.now();
        if (this.requestedDate == null) {
            this.requestedDate = LocalDate.now();
        }
    }

    public Long getId() { return id; }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public LocalDate getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDate requestedDate) { this.requestedDate = requestedDate; }

    public byte[] getResultFile() { return resultFile; }
    public void setResultFile(byte[] resultFile) { this.resultFile = resultFile; }

    public String getResultFileName() { return resultFileName; }
    public void setResultFileName(String resultFileName) { this.resultFileName = resultFileName; }

    public OffsetDateTime getResultUploadedAt() { return resultUploadedAt; }
    public void setResultUploadedAt(OffsetDateTime resultUploadedAt) { this.resultUploadedAt = resultUploadedAt; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}
