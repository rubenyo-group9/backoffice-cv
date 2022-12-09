package com.alfa1.cv.backoffice.views.trainings;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import com.alfa1.cv.backoffice.data.entity.Training;
import com.alfa1.cv.backoffice.views.upload.UploadField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class TrainingForm extends FormLayout {
    private Training training;

    MultiSelectComboBox<Trainer> trainers = new MultiSelectComboBox<>("Trainers");
    TextField trainingName = new TextField("Name");
    TextField certificateName = new TextField("Name on certificate");

    UploadField templateCertParticipated = new UploadField("Participation template", "application/pdf", ".pdf");
    UploadField templateCertCompleted = new UploadField("Completion template", "application/pdf", ".pdf");

    Binder<Training> binder = new BeanValidationBinder<>(Training.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public TrainingForm(List<Trainer> trainerList) {
        addClassName("training-form");

        binder.bindInstanceFields(this);

        trainers.setItems(trainerList);
        trainers.setItemLabelGenerator(Trainer::getFullName);

        add(trainers, trainingName, certificateName,
                templateCertParticipated, templateCertCompleted, createButtonsLayout());
    }

    public void setTraining(Training training) {
        this.training = training;
        binder.readBean(training);
    }

    private void validateAndSave() {
        try {
            if (certificateName.isEmpty()) {
                certificateName.setValue(trainingName.getValue());
            }

            binder.writeBean(training);
            fireEvent(new SaveEvent(this, training));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, training)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public static class ContactFormEvent extends ComponentEvent<TrainingForm> {
        private final Training training;

        protected ContactFormEvent(TrainingForm source, Training training) {
            super(source, false);
            this.training = training;
        }

        public Training getTraining() {
            return training;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(TrainingForm source, Training training) {
            super(source, training);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(TrainingForm source, Training training) {
            super(source, training);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(TrainingForm source) {
            super(source, null);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
