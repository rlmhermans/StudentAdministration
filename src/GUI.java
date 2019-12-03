import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class GUI extends Application {
    List<Student> students;
    Database db = new Database(); //Dit wil je natuurlijk niet direct in de GUI aanmaken, maar 'injecteren'.
    ComboBox<Student> cb;
    TextField field;

    @Override
    public void start(Stage stage) {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10));

        cb = new ComboBox<>();
        field = new TextField();
        refreshStudents();

        Button newButton = new Button("Nieuw");
        Button deleteButton = new Button("Verwijder");
        Button saveButton = new Button("Opslaan");

        newButton.setOnAction(actionEvent -> {
            field.setText("");
            cb.getSelectionModel().clearSelection();
        });

        deleteButton.setOnAction(actionEvent -> {
            Student student = cb.getSelectionModel().getSelectedItem();
            boolean succeeded = db.remove(student);
            if (succeeded) {
                refreshStudents();
                new Alert(Alert.AlertType.INFORMATION, "Student verwijderd.").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to delete student.").show();
            }
        });

        saveButton.setOnAction(actionEvent -> {
            Student student = cb.getSelectionModel().getSelectedItem();
            if (student == null) {
                boolean succeeded = db.create(field.getText());
                if (succeeded) {
                    refreshStudents();
                    new Alert(Alert.AlertType.INFORMATION, "Student aangemaakt.").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to create student.").show();
                }
            } else {
                student.setName(field.getText());
                boolean succeeded = db.update(student);
                if (succeeded) {
                    refreshStudents();
                    new Alert(Alert.AlertType.INFORMATION, "Student geüpdatet.").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to update student.").show();
                }
            }
        });

        cb.valueProperty().addListener((observableValue, prevStudent, student) -> {
            if (student != null) {
                field.setText(student.getName());
            }
        });

        pane.addColumn(0, new Label("Kies een student:"), newButton, deleteButton);
        pane.addColumn(1, cb, new Label("Naam:"));
        pane.add(field, 2, 1);
        pane.add(saveButton, 2, 2);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Studentadministratie");
        stage.show();

    }

    private void refreshStudents() {
        students = db.getAll();
        ObservableList cbList = FXCollections.observableList(students);
        cb.setItems(cbList);
        field.setText("");
    }
}
