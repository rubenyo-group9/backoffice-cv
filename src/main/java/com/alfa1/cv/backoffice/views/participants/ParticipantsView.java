package com.alfa1.cv.backoffice.views.participants;

import com.alfa1.cv.backoffice.data.entity.Participant;
import com.alfa1.cv.backoffice.data.service.ParticipantService;
import com.alfa1.cv.backoffice.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("Participants")
@Route(value = "participants", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class ParticipantsView extends VerticalLayout {
    Grid<Participant> grid = new Grid<>(Participant.class);
    TextField filterText = new TextField();
    ParticipantForm form;
    ParticipantService participantService;

    public ParticipantsView(ParticipantService participantService) {
        this.participantService = participantService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ParticipantForm();
        form.setWidth("25em");

        form.addListener(ParticipantForm.SaveEvent.class, this::saveParticipant);
        form.addListener(ParticipantForm.DeleteEvent.class, this::deleteParticipant);
        form.addListener(ParticipantForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveParticipant(ParticipantForm.SaveEvent event) {
        participantService.addParticipant(event.getParticipant());
        updateList();
        closeEditor();
    }

    private void deleteParticipant(ParticipantForm.DeleteEvent event) {
        participantService.deleteParticipant(event.getParticipant());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("participant-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "dateOfBirth", "email", "linkedInUrl");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setDetailsVisibleOnClick(false);

        grid.asSingleSelect().addValueChangeListener(event -> editParticipant(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addParticipantButton = new Button("Add participant");
        addParticipantButton.addClickListener(click -> addParticipant());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addParticipantButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editParticipant(Participant participant) {
        if (participant == null) {
            closeEditor();
        } else {
            form.setParticipant(participant);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setParticipant(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addParticipant() {
        grid.asSingleSelect().clear();
        editParticipant(new Participant());
    }

    private void updateList() {
        grid.setItems(participantService.getParticipants());
    }
}
