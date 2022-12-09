package com.alfa1.cv.backoffice.views.trainings;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import com.alfa1.cv.backoffice.data.entity.Training;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainingDetailsView extends FormLayout {

    private final TextField trainers = new TextField("Trainers");
    private final TextField name = new TextField("Name");
    private final TextField certificateName = new TextField("Name on certificate");
    private final Anchor participationTemplate = new Anchor();
    private final Anchor completionTemplate = new Anchor();

    public TrainingDetailsView() {
        Stream.of(trainers, name, certificateName).forEach(field -> field.setEnabled(false));

        Stream.of(participationTemplate, completionTemplate).forEach(field -> {
            participationTemplate.getElement().setAttribute("download", true);
            participationTemplate.removeAll();
        });

        participationTemplate.add(new Button("Download participation template", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        completionTemplate.add(new Button("Download completion template", new Icon(VaadinIcon.DOWNLOAD_ALT)));

        setResponsiveSteps(new ResponsiveStep("0", 3));

        Stream.of(trainers, name, certificateName, participationTemplate, completionTemplate)
                .forEach(field -> setColspan(field, 1));

        add(trainers, name, certificateName, participationTemplate, completionTemplate);
    }

    public void setTraining(Training training) {
        trainers.setValue(training.getTrainers().stream()
                .map(Trainer::getFullName)
                .collect(Collectors.joining(",")));
        name.setValue(training.getTrainingName());
        certificateName.setValue(training.getCertificateName());

        participationTemplate.setHref(new StreamResource("template.pdf", () -> new ByteArrayInputStream(training.getTemplateCertParticipated())));
        completionTemplate.setHref(new StreamResource("template.pdf", () -> new ByteArrayInputStream(training.getTemplateCertCompleted())));
    }
}
