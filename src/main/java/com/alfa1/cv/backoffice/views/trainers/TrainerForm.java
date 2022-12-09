package com.alfa1.cv.backoffice.views.trainers;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import com.alfa1.cv.backoffice.views.upload.UploadField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class TrainerForm extends FormLayout {
    private Trainer trainer;

    Binder<Trainer> binder = new BeanValidationBinder<>(Trainer.class);

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    UploadField signature = new UploadField("Signature", "image/png", ".png");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public TrainerForm() {
        addClassName("contact-form");

        binder.bindInstanceFields(this);

        add(firstName, lastName, signature, createButtonsLayout());
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        binder.readBean(trainer);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(trainer);
            fireEvent(new SaveEvent(this, trainer));
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, trainer)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public static class ContactFormEvent extends ComponentEvent<TrainerForm> {
        private final Trainer trainer;

        protected ContactFormEvent(TrainerForm source, Trainer trainer) {
            super(source, false);
            this.trainer = trainer;
        }

        public Trainer getTrainer() {
            return trainer;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(TrainerForm source, Trainer trainer) {
            super(source, trainer);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(TrainerForm source, Trainer trainer) {
            super(source, trainer);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(TrainerForm source) {
            super(source, null);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
