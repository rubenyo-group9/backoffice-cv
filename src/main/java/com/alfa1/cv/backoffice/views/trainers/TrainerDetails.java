package com.alfa1.cv.backoffice.views.trainers;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

public class TrainerDetails extends FormLayout {

    private final TextField firstNameField = new TextField("First name");
    private final TextField lastNameField = new TextField("Last name");
    private final Image signatureField = new Image("signature", null);

    public TrainerDetails() {
        Stream.of(firstNameField, lastNameField, signatureField).forEach(field -> {
            field.setEnabled(false);
            add(field);
        });

        setResponsiveSteps(new ResponsiveStep("0", 3));
        setColspan(firstNameField, 3);
        setColspan(lastNameField, 3);
        setColspan(signatureField, 3);
    }

    public void setTrainer(Trainer trainer) {
        firstNameField.setValue(trainer.getFirstName());
        lastNameField.setValue(trainer.getLastName());
        signatureField.setSrc(new StreamResource("signature",
                            () -> new ByteArrayInputStream(trainer.getSignature())));
    }
}
