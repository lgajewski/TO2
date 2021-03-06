package pl.edu.agh.iet.to2.employees.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import pl.edu.agh.iet.to2.app.Presenter;
import pl.edu.agh.iet.to2.employees.model.Employee;
import pl.edu.agh.iet.to2.employees.model.operation.IOperation;
import pl.edu.agh.iet.to2.employees.model.operation.OccupationOperation;
import pl.edu.agh.iet.to2.employees.model.operation.SalaryOperation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EmployeeHistoryController {

    private final Presenter presenter;
    private final Employee employee;

    @FXML
    private ListView<OccupationOperation> occupationListView;

    @FXML
    private ListView<SalaryOperation> salaryListView;

    @FXML
    private ImageView closeButton;

    public EmployeeHistoryController(Presenter presenter, Employee employee) {
        this.presenter = presenter;
        this.employee = employee;
    }

    @FXML
    private void initialize() {
        occupationListView.setItems(employee.getOccupationHistory());
        occupationListView.setCellFactory(param -> new SimpleListCell());

        salaryListView.setItems(employee.getSalaryHistory());
        salaryListView.setCellFactory(param -> new SimpleListCell());

        closeButton.setOnMouseClicked(event -> presenter.closeCurrentStage());
    }

    private <T> String createText(IOperation<T> operation) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = df.format(operation.getDate());
        return date + " ->  '" + operation.getValue() + "'";
    }

    class SimpleListCell<T> extends ListCell<IOperation<T>> {
        @Override
        protected void updateItem(IOperation<T> item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                setText(createText(item));
            }
        }
    }
}
