package com.alfa1.cv.backoffice.data.entity;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Certificate {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany()
    @NotEmpty
    private Set<Trainer> trainers;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    private Training training;

    @NotNull
    private LocalDate achievedOn;

    @NotNull
    private LocalDate validUntil;

    @URL
    @NotNull
    private String publicUrl;
}
