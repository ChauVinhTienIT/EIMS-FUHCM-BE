package com.fullsnacke.eimsfuhcmbe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "exam_slots")
public class ExamSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_exam_id", nullable = false)
    SubjectExam subjectExam;

    @Column(name = "status", nullable = false)
    Integer status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    User createdBy;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "updated_by", nullable = false)
    User updatedBy;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "start_at", nullable = false)
    Instant startAt;

    @Column(name = "end_at", nullable = false)
    Instant endAt;

}