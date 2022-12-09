package com.alfa1.cv.backoffice.views.participants;

import com.alfa1.cv.backoffice.data.entity.Participant;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class ParticipantForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    DatePicker dateOfBirth = new DatePicker("Date of birth");
    EmailField email = new EmailField("Email");
    TextField linkedInUrl = new TextField("LinkedIn URL");

    private Participant participant;

    Binder<Participant> binder = new BeanValidationBinder<>(Participant.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public ParticipantForm() {
        addClassName("contact-form");

        binder.bindInstanceFields(this);

        add(firstName, lastName, dateOfBirth, email, linkedInUrl, createButtonsLayout());
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
        binder.readBean(participant);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(participant);
            fireEvent(new SaveEvent(this, participant));
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, participant)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> checkValid());
        return new HorizontalLayout(save, delete, close);
    }

    private void checkValid() {
        save.setEnabled(binder.isValid());
    }

    public static class ContactFormEvent extends ComponentEvent<ParticipantForm> {
        private final Participant participant;

        protected ContactFormEvent(ParticipantForm source, Participant participant) {
            super(source, false);
            this.participant = participant;
        }

        public Participant getParticipant() {
            return participant;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ParticipantForm source, Participant participant) {
            super(source, participant);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ParticipantForm source, Participant participant) {
            super(source, participant);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ParticipantForm source) {
            super(source, null);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
