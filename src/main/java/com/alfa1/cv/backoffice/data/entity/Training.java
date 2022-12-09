package com.alfa1.cv.backoffice.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Training {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String trainingName;

    private String certificateName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "training_trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private Set<Trainer> trainers;

    @NotEmpty
    @Lob
    private byte[] templateCertParticipated;

    @NotEmpty
    @Lob
    private byte[] templateCertCompleted;
}
