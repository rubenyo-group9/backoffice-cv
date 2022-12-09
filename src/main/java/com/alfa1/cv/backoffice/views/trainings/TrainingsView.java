package com.alfa1.cv.backoffice.views.trainings;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import com.alfa1.cv.backoffice.data.entity.Training;
import com.alfa1.cv.backoffice.data.service.TrainerService;
import com.alfa1.cv.backoffice.data.service.TrainingService;
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

import javax.annotation.security.PermitAll;
import java.util.stream.Collectors;

@PageTitle("Trainings")
@Route(value = "trainings", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class TrainingsView extends VerticalLayout {
    Grid<Training> grid = new Grid<>(Training.class);
    TextField filterText = new TextField();
    TrainingForm form;
    TrainingService trainingService;
    TrainerService trainerService;

    public TrainingsView(TrainingService trainingService, TrainerService trainerService) {
        this.trainingService = trainingService;
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
        form = new TrainingForm(trainerService.getTrainers());
        form.setWidth("25em");

        form.addListener(TrainingForm.SaveEvent.class, this::saveTraining);
        form.addListener(TrainingForm.DeleteEvent.class, this::deleteTraining);
        form.addListener(TrainingForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveTraining(TrainingForm.SaveEvent event) {
        trainingService.addTraining(event.getTraining());
        updateList();
        closeEditor();
    }

    private void deleteTraining(TrainingForm.DeleteEvent event) {
        trainingService.deleteTraining(event.getTraining());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("training-grid");
        grid.setSizeFull();
        grid.setColumns("trainingName", "certificateName");
        grid.addColumn(this::getTrainerNames).setHeader("Trainers");
        grid.addColumn(createToggleDetailsRenderer(grid));

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setDetailsVisibleOnClick(false);

        grid.setItemDetailsRenderer(createTrainingDetailsRenderer());

        grid.asSingleSelect().addValueChangeListener(event -> editTraining(event.getValue()));
    }

    private static Renderer<Training> createToggleDetailsRenderer(
            Grid<Training> grid) {
        return LitRenderer.<Training>of(
                        "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Toggle details</vaadin-button>")
                .withFunction("handleClick",
                        trainer -> grid.setDetailsVisible(trainer, !grid.isDetailsVisible(trainer)));
    }

    private static ComponentRenderer<TrainingDetailsView, Training> createTrainingDetailsRenderer() {
        return new ComponentRenderer<>(TrainingDetailsView::new,
                TrainingDetailsView::setTraining);
    }

    private String getTrainerNames(Training training) {
        if (training.getTrainers().isEmpty()) {
            return "Geen";
        }

        return training.getTrainers().stream()
                .map(Trainer::getFullName)
                .collect(Collectors.joining(","));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addTrainingButton = new Button("Add training");
        addTrainingButton.addClickListener(click -> addTraining());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addTrainingButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTraining(Training training) {
        if (training == null) {
            closeEditor();
        } else {
            form.setTraining(training);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTraining(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTraining() {
        grid.asSingleSelect().clear();
        editTraining(new Training());
    }

    private void updateList() {
        grid.setItems(trainingService.getTrainings());
    }
}
