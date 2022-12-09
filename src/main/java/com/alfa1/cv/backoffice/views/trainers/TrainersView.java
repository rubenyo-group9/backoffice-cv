package com.alfa1.cv.backoffice.views.trainers;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import com.alfa1.cv.backoffice.data.service.TrainerService;
import com.alfa1.cv.backoffice.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.PermitAll;

@PageTitle("Trainers")
@Route(value = "trainers", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class TrainersView extends VerticalLayout {
    Grid<Trainer> grid = new Grid<>(Trainer.class);
    TextField filterText = new TextField();
    TrainerForm form;
    TrainerService trainerService;

    public TrainersView(TrainerService trainerService) {
        this.trainerService = trainerService;
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
        form = new TrainerForm();
        form.setWidth("25em");

        form.addListener(TrainerForm.SaveEvent.class, this::saveTrainer);
        form.addListener(TrainerForm.DeleteEvent.class, this::deleteTrainer);
        form.addListener(TrainerForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveTrainer(TrainerForm.SaveEvent event) {
        trainerService.addTrainer(event.getTrainer());
        updateList();
        closeEditor();
    }

    private void deleteTrainer(TrainerForm.DeleteEvent event) {
        trainerService.deleteTrainer(event.getTrainer().getId());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("trainer-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addColumn(createToggleDetailsRenderer(grid));

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createTrainerDetailsRenderer());

        grid.asSingleSelect().addValueChangeListener(event -> editTrainer(event.getValue()));
    }

    private static Renderer<Trainer> createToggleDetailsRenderer(
            Grid<Trainer> grid) {
        return LitRenderer.<Trainer>of(
                        "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Toggle details</vaadin-button>")
                .withFunction("handleClick",
                        trainer -> grid.setDetailsVisible(trainer, !grid.isDetailsVisible(trainer)));
    }

    private static ComponentRenderer<TrainerDetails, Trainer> createTrainerDetailsRenderer() {
        return new ComponentRenderer<>(TrainerDetails::new,
                TrainerDetails::setTrainer);
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addTrainerButton = new Button("Add trainer");
        addTrainerButton.addClickListener(click -> addTrainer());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addTrainerButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTrainer(Trainer trainer) {
        if (trainer == null) {
            closeEditor();
        } else {
            form.setTrainer(trainer);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTrainer(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTrainer() {
        grid.asSingleSelect().clear();
        editTrainer(new Trainer());
    }

    private void updateList() {
        grid.setItems(trainerService.getTrainers());
    }
}
