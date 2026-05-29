package com.student;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class Controller {

    // input field
    @FXML private TextField          txtName;
    @FXML private TextField          txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;

    // table
    @FXML private TableView<Student>           table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String>  colName;
    @FXML private TableColumn<Student, String>  colCourse;
    @FXML private TableColumn<Student, String>  colYear;

    // status label
    @FXML private Label lblStatus;

    // state
    private final ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    @FXML
    public void initialize() {
        conn = DBConnection.connect();

        if (conn == null) {
            showStatus("Database connection failed. Check DBConnection.java.", true);
        }

        // load enum to choicebox
        cbYear.getItems().setAll(YearLevel.values());

        // table columns binding
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        loadData();

        // row click event
        table.setOnMouseClicked(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedId = s.getId();
                txtName.setText(s.getName());
                txtCourse.setText(s.getCourse());

                for (YearLevel y : YearLevel.values()) {
                    if (y.toString().equals(s.getYearLevel())) {
                        cbYear.setValue(y);
                        break;
                    }
                }
            }
        });
    }

    // load all records
    private void loadData() {
        list.clear();
        try {
            String query = "SELECT * FROM students ORDER BY id";
            ResultSet rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            table.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to load data.", true);
        }
    }

    // add
    @FXML
    private void addStudent() {
        if (!validateInputs()) return;

        try {
            String query = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();

            loadData();
            clearFields();
            showStatus("Student added successfully.", false);

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to add student.", true);
        }
    }

    // update
    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showStatus("⚠ Please select a student to update.", true);
            return;
        }
        if (!validateInputs()) return;

        try {
            String query = "UPDATE students SET name=?, course=?, year_level=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
            showStatus("Student updated successfully.", false);

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to update student.", true);
        }
    }

    // delete
    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showStatus("⚠ Please select a student to delete.", true);
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this student?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            String query = "DELETE FROM students WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
            showStatus("Student deleted successfully.", false);

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to delete student.", true);
        }
    }

    // clear
    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
        showStatus("", false);
    }

    // input validation
    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty()) {
            showStatus("⚠ Name cannot be empty.", true);
            txtName.requestFocus();
            return false;
        }
        if (txtCourse.getText().trim().isEmpty()) {
            showStatus("⚠ Course cannot be empty.", true);
            txtCourse.requestFocus();
            return false;
        }
        if (cbYear.getValue() == null) {
            showStatus("⚠ Please select a year level.", true);
            return false;
        }
        return true;
    }

    // status helper
    private void showStatus(String message, boolean isError) {
        lblStatus.setText(message);
        lblStatus.setStyle(isError
                ? "-fx-text-fill: #e74c3c;"
                : "-fx-text-fill: #27ae60;");
    }
}
