package com.alfa1.cv.backoffice.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Lob
    @NotNull
    @NotEmpty
    private byte[] signature;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<Training> trainings;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
