package com.univ.it.javafx;

import com.univ.it.db.Column;
import com.univ.it.db.DataBase;
import com.univ.it.db.Row;
import com.univ.it.db.Table;
import com.univ.it.dbtype.Attribute;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MainWindow extends Application {
    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    private VBox verticalLayout;
    private DataBase currentDB;
    private TabPane tabPane;

    private final String tempTable = "C:\\Temp\\tb.db";
    private final String tempDB = "C:\\Temp\\bd.db";

    private final ObservableList<String> availableOptions =
            FXCollections.observableArrayList(
                    "Char",
                    "Date",
                    "DateInvl",
                    "Integer",
                    "Real"
            );

    private void initUI(Stage stage) {
        currentDB = new DataBase("C:\\Temp\\bd.db", "New database", new ArrayList<>());

        StackPane root = new StackPane();
        verticalLayout = new VBox();

        Scene scene = new Scene(root, 600, 480);

        initializeMenuBar();
        initializeTableTab();

        root.getChildren().add(verticalLayout);

        stage.setTitle("Database Logic");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuHelp = new Menu("Help");
        Menu menuTable = new Menu("Table");

        MenuItem newDbMenuItem = new MenuItem("New DB");
        newDbMenuItem.setOnAction(t -> createDb());
        MenuItem openDbMenuItem = new MenuItem("Open DB");
        openDbMenuItem.setOnAction(t -> openDb());
        MenuItem saveDbMenuItem = new MenuItem("Save DB");
        saveDbMenuItem.setOnAction(t -> saveDb());
        menuFile.getItems().addAll(newDbMenuItem, openDbMenuItem, saveDbMenuItem);

        MenuItem newTableMenuItem = new MenuItem("New Table");
        newTableMenuItem.setOnAction(t -> createTable());
//        MenuItem dropTableMenuItem = new MenuItem("Drop Table");
//        dropTableMenuItem.setOnAction(t -> dropTable());
        MenuItem addNewRowTableMenuItem = new MenuItem("Add New Row");
        addNewRowTableMenuItem.setOnAction(t -> addNewRowTable());
//        MenuItem calculateDifferenceMenuItem = new MenuItem("Calculate Difference");
//        calculateDifferenceMenuItem.setOnAction(t -> calculateDifference());
        menuTable.getItems().addAll(
                newTableMenuItem,
//                dropTableMenuItem,
                addNewRowTableMenuItem
//                calculateDifferenceMenuItem
        );

        MenuItem helpMenuItem = new MenuItem("About");
        helpMenuItem.setOnAction(t -> showHelpWindow());
        menuHelp.getItems().addAll(helpMenuItem);

        menuBar.getMenus().addAll(menuFile, menuTable, menuHelp);
        verticalLayout.getChildren().add(menuBar);
    }

    private void createDb() {
        Label secondLabel = new Label("Name of data base");
        TextField dbNameTextField = new TextField();
        Button createButton = new Button("Create");

        HBox secondaryLayout = new HBox();
        secondaryLayout.getChildren().addAll(secondLabel, dbNameTextField, createButton);
        Scene secondScene = new Scene(secondaryLayout);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("New Database");
        newWindow.setScene(secondScene);

        createButton.setOnAction(e -> {
            if (!dbNameTextField.getText().equals("")) {
                System.out.println(dbNameTextField.getText());
                try {
                    currentDB = new DataBase(tempDB, dbNameTextField.getText(), new ArrayList<>());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                newWindow.close();
                closeAllTabs();
                showDataBase();
            } else {
                showErrorMessage("Empty database name");
            }
        });

        newWindow.show();
    }

    @SuppressWarnings("unchecked")
    private void createTable() {
        Label secondLabel = new Label("Name of table");
        TextField tableNameTextField = new TextField();

        HBox horizontalLayout = new HBox();
        horizontalLayout.getChildren().addAll(secondLabel, tableNameTextField);

        VBox _verticalLayout = new VBox();

        Button addNewColumnButton = new Button("Add new column");

        Button createNewTableButton = new Button("Create New Table");

        HBox buttonsLayout = new HBox();
        buttonsLayout.getChildren().addAll(addNewColumnButton, createNewTableButton);

        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(horizontalLayout, _verticalLayout, buttonsLayout);

        // New window (Stage)
        Scene secondScene = new Scene(mainLayout);
        Stage newWindow = new Stage();
        newWindow.setTitle("New Table");
        newWindow.setScene(secondScene);

        HBox columnCreationLayout = new HBox();
        ArrayList<ComboBox> comboBoxes = new ArrayList<>();
        ComboBox comboBox = new ComboBox(availableOptions);
        comboBoxes.add(comboBox);
        columnCreationLayout.getChildren().addAll(new Label("Column"), comboBox);
        _verticalLayout.getChildren().add(columnCreationLayout);
        addNewColumnButton.setOnAction(e -> {
            HBox _columnCreationLayout = new HBox();
            ComboBox _comboBox = new ComboBox(availableOptions);
            comboBoxes.add(_comboBox);
            _columnCreationLayout.getChildren().addAll(new Label("Column"), new ComboBox(availableOptions));
            _verticalLayout.getChildren().add(_columnCreationLayout);
        });

        createNewTableButton.setOnAction(e -> {
            boolean allTypesChosen = true;
            ArrayList<Column> columns = new ArrayList<>();
            for (ComboBox _comboBox : comboBoxes) {
                if (_comboBox.getValue() == null) {
                    allTypesChosen = false;
                    break;
                } else {
                    try {
                        columns.add(new Column("ColumnName", "com.univ.it.dbtype.Attribute" + _comboBox.getValue().toString()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (!allTypesChosen) {
                showErrorMessage("Not all Types are chosen");
                return;
            }
            if (!tableNameTextField.getText().equals("")) {
                String tableName = tableNameTextField.getText();
                String tableFilePath = currentDB.getPathForTables() + File.separator + tableName + ".db";
                Table newTable = new Table(tableFilePath, tableName, columns);
                currentDB.getTables().add(newTable);
                newWindow.close();
                showTable(newTable);
            } else {
                showErrorMessage("Empty table name");
            }
        });

        newWindow.show();
    }

    private void openDb() {
        Stage newWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database File");
        File file = fileChooser.showOpenDialog(newWindow);
        if (file != null) {
            try {
                currentDB = new DataBase(file.getAbsolutePath());
                closeAllTabs();
                showDataBase();
            } catch (Exception e) {
                showErrorMessage(e.toString());
            }
        } else {
            showErrorMessage("Choose file");
        }
    }

    private void saveDb() {
        Stage newWindow = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Database File");
        File selectedDirectory = directoryChooser.showDialog(newWindow);
        if (selectedDirectory != null) {
            try {
                currentDB.saveToFile(selectedDirectory.getAbsolutePath());
            } catch (Exception e) {
                showErrorMessage(e.toString());
            }
        } else {
            showErrorMessage("Choose file");
        }
    }

    private void showDataBase() {
        HashMap<String, Table> tables = new HashMap<>();
        for (Table table : currentDB.getTables()) {
            tables.put(table.getName(), table);
        }

        for (HashMap.Entry<String, Table> entry : tables.entrySet()) {
            String tableName = entry.getKey();
            Table table = entry.getValue();
            Tab tab = new Tab();
            tab.setText(tableName);
            TableView tableView = new TableView();
            tab.setContent(tableView);
            tabPane.getTabs().add(tab);
            showTable(table, tableView);
        }
    }

    @SuppressWarnings("unchecked")
    private void showTable(Table table, TableView tableView) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < table.getColumns().size(); i++) {
            TableColumn col = new TableColumn(table.getColumns().get(i).getName());
            final int j = i;
            col.setCellValueFactory(
                    (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                            new SimpleStringProperty(param.getValue().get(j).toString())
            );
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    (EventHandler<TableColumn.CellEditEvent<ObservableList, String>>) t -> {
                        String newValue = t.getNewValue();
                        try {
                            table.getRows().get(t.getTablePosition().getRow()).set(j,
                                    (Attribute) table.getColumns().get(j).getStringConstructor().newInstance(newValue));
                        } catch (Exception e) {
                            showErrorMessage(e.toString());
                        }
                    }
            );
            tableView.getColumns().add(col);
        }

        for (int i = 0; i < table.getRows().size(); i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int j = 0; j < table.getRows().get(i).size(); j++) {
                row.add(table.getRows().get(i).get(j).toString());
            }
            data.add(row);
        }
        tableView.setEditable(true);
        tableView.setItems(data);
    }

//    private void dropTable() {
//        Tab tab = tabPane.getSelectionModel().getSelectedItem();
//        String tabName = tab.getText();
//        if (!currentDB.dropTable(tabName)) {
//            showErrorMessage("Error occurred");
//        }
//        closeAllTabs();
//        showDataBase();
//    }

    private void addNewRowTable() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String tableName = tab.getText();
        HashMap<String, Table> tables = new HashMap<>();
        for (Table table : currentDB.getTables()) {
            tables.put(table.getName(), table);
        }
        Table table = tables.get(tableName);

        VBox mainLayout = new VBox();
        ArrayList<TextField> textFields = new ArrayList<>();
        for (int i = 0; i < table.getColumns().size(); ++i) {
            HBox horizontalBoxLayout = new HBox();
            Column col = table.getColumns().get(i);
            Label label = new Label(col.getName());
            TextField valueTextField = new TextField();
            textFields.add(valueTextField);
            horizontalBoxLayout.getChildren().addAll(label, valueTextField);
            mainLayout.getChildren().add(horizontalBoxLayout);
        }

        Button addButton = new Button("Add new row");
        mainLayout.getChildren().add(addButton);

        Scene secondScene = new Scene(mainLayout);

        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        addButton.setOnAction(e -> {
            boolean allValuesFilled = true;
            ArrayList<Attribute> attributes = new ArrayList<>(textFields.size());
            for (int i = 0; i < textFields.size(); ++i) {
                TextField textField = textFields.get(i);
                if (textField.getText().equals("")) {
                    allValuesFilled = false;
                } else {
                    Column col = table.getColumns().get(i);
                    try {
                        attributes.add((Attribute) col.getStringConstructor().newInstance(textField.getText()));
                    } catch (Exception ex) {
                        showErrorMessage(ex.toString());
                    }
                }
            }
            if (!allValuesFilled) {
                showErrorMessage("Not all values filled");
            }
            try {
                table.getRows().add(new Row(attributes));
            } catch (Exception ex) {
                showErrorMessage(ex.toString());
            }
            newWindow.close();
        closeAllTabs();
        showDataBase();
        });

        newWindow.show();
    }

//    @SuppressWarnings("unchecked")
//    private void calculateDifference() {
//        Collection<String> allTableNames = currentDB.getTables().keySet();
//
//        Label firstTableLabel = new Label("First Table Name");
//        ComboBox firstTableComboBox = new ComboBox();
//        for (String tableName : allTableNames) {
//            firstTableComboBox.getItems().add(tableName);
//        }
//
//        Label secondTableLabel = new Label("First Table Name");
//        ComboBox secondTableComboBox = new ComboBox();
//        for (String tableName : allTableNames) {
//            secondTableComboBox.getItems().add(tableName);
//        }
//
//        HBox horizontalLayout1 = new HBox();
//        horizontalLayout1.getChildren().addAll(firstTableLabel, firstTableComboBox);
//
//        HBox horizontalLayout2 = new HBox();
//        horizontalLayout2.getChildren().addAll(secondTableLabel, secondTableComboBox);
//
//        Button calculateDifference = new Button("Calculate Difference");
//
//        VBox mainLayout = new VBox();
//        mainLayout.getChildren().addAll(horizontalLayout1, horizontalLayout2, calculateDifference);
//
//        Scene secondScene = new Scene(mainLayout);
//
//        Stage newWindow = new Stage();
//        newWindow.setTitle("About");
//        newWindow.setScene(secondScene);
//
//        calculateDifference.setOnAction(e -> {
//            if (firstTableComboBox.getValue() == null || secondTableComboBox.getValue() == null) {
//                showErrorMessage("Choose table");
//                return;
//            }
//            String firstTableName = firstTableComboBox.getValue().toString();
//            String secondTableName = secondTableComboBox.getValue().toString();
//            currentDB.addTable(Table.differenceBetween(currentDB.getTable(firstTableName),
//                    currentDB.getTable(secondTableName)));
//            newWindow.close();
//            closeAllTabs();
//            showDataBase();
//        });
//
//        newWindow.show();
//    }

    private void showTable(Table table) {
        Tab tab = new Tab();
        tab.setText(table.getName());
        TableView tableView = new TableView();
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
        showTable(table, tableView);
    }

    private static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showHelpWindow() {
        Label secondLabel = new Label("Author: Student");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        newWindow.show();
    }

    private void initializeTableTab() {
        tabPane = new TabPane();
        verticalLayout.getChildren().add(tabPane);
    }

    private void closeAllTabs() {
        tabPane.getTabs().clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
