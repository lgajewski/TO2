package pl.edu.agh.iet.to2.projects.controller;


import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.edu.agh.iet.to2.employees.IEmployee;
import pl.edu.agh.iet.to2.projects.model.Project;
import pl.edu.agh.iet.to2.projects.presenter.ProjectPresenter;
import pl.edu.agh.iet.to2.teams.ITeam;
import pl.edu.agh.iet.to2.teams.ITeamMember;

import java.io.IOException;
import java.math.BigDecimal;


public class ProjectFinancialOverviewController {

    private Project project;

    private ProjectPresenter presenter;

    ObservableList<IEmployee> members = FXCollections.observableArrayList();

    public ProjectFinancialOverviewController(ProjectPresenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    private TableView<IEmployee> membersTable;

    @FXML
    private TableColumn<IEmployee, String> firstNameColumn;

    @FXML
    private TableColumn<IEmployee, String> lastNameColumn;

    @FXML
    private TableColumn<IEmployee, String> occupationColumn;

    @FXML
    private TableColumn<IEmployee, String> salaryColumn;
    @FXML
    private Button backButton;

    public void setProject(Project project) {
        this.project = project;
    }

    private void updateData() {
        members.clear();
        members.addAll(project.getMembers());
        membersTable.setItems(members);
    }

    public void initialize() {
        membersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        firstNameColumn.setCellValueFactory(
                dataValue -> new SimpleStringProperty(dataValue.getValue().getName()));
        lastNameColumn.setCellValueFactory(
                dataValue -> new SimpleStringProperty(dataValue.getValue().getSurname()));

        occupationColumn.setCellValueFactory(
                dataValue -> new SimpleStringProperty(dataValue.getValue().getOccupation()));

        occupationColumn.setCellValueFactory(
                dataValue -> new SimpleStringProperty(dataValue.getValue().getSalary().toString()));
        updateData();
    }

    @FXML
    private void handleBackAction() throws IOException {

        presenter.onProjectOverview();

    }

}