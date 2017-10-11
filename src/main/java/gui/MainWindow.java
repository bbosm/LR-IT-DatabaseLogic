package gui;

import db.*;
import dbtype.Attribute;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow extends Application {

    private ArrayList<Column> currColumns = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    private VBox verticalLayout;
    private DataBase currentDB;
    private Table currTable;
    private TabPane tabPane;

    // TODO: remove this
    private final String tempTable = "C:\\Temp\\tb.db";
    private final String tempDB = "/home/romashka/mydb/1.db";

    private final ObservableList<String> availableOptions =
            FXCollections.observableArrayList(

                    "Integer",
                    "Real",
                    "Char",
                    "Enum",
                            "Date",
                            "DateInvl"
            );

    private void initUI(Stage stage) {
        currentDB = new DataBase(tempDB, new HashMap<String, Table>());

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
        Menu menuTable = new Menu("Table");

        MenuItem newDbMenuItem = new MenuItem("New DB");
        newDbMenuItem.setOnAction(t -> createDb());
        MenuItem openDbMenuItem = new MenuItem("Open DB");
        openDbMenuItem.setOnAction(t -> openDb());
        MenuItem saveDbMenuItem = new MenuItem("Save DB");
        saveDbMenuItem.setOnAction(t -> saveDb());
        menuFile.getItems().addAll(newDbMenuItem, openDbMenuItem, saveDbMenuItem);

        MenuItem newTableMenuItem = new MenuItem("Create Table");
        newTableMenuItem.setOnAction(t -> createTable());
        MenuItem addNewRowTableMenuItem = new MenuItem("Add New Row");
        addNewRowTableMenuItem.setOnAction(t -> addNewRowTable());
        MenuItem searchMenuItem = new MenuItem("Search");
        searchMenuItem.setOnAction(t -> search());
        menuTable.getItems().addAll(
                newTableMenuItem,
                addNewRowTableMenuItem,
                searchMenuItem
        );

        menuBar.getMenus().addAll(menuFile, menuTable);
        verticalLayout.getChildren().add(menuBar);
    }

    private void search()
    {
        String tableName = tabPane.getSelectionModel().getSelectedItem().getText();
        currTable = currentDB.getTables().get(tableName);

        HBox columnLayout = new HBox();
        ArrayList<TextField> textFields = new ArrayList<>();

        for (int i = 0; i < currTable.getColumns().size(); ++i)
        {
            VBox tmpLayout = new VBox();
            String name = currTable.getColumns().get(i).getClassName();
            textFields.add(new TextField());
            tmpLayout.getChildren().addAll(new Label(name), textFields.get(i));
            columnLayout.getChildren().add(tmpLayout);
        }

        Button tmpButton = new Button("Search");

        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(columnLayout, tmpButton);

        Stage tmpWindow = new Stage();
        tmpWindow.setTitle("Search Window");
        tmpWindow.setScene(new Scene(mainLayout));
        tmpWindow.show();

        tmpButton.setOnAction(e -> {
            ArrayList<String> fieldsSearch = new ArrayList<>(currTable.getColumns().size());
            for (int j = 0; j < currTable.getColumns().size(); j++) {
                fieldsSearch.add(textFields.get(j).getText());
            }
            Table searchTable = currTable.search(fieldsSearch);

            tmpWindow.close();
            showTable(searchTable);
        });
    }

    private void createDb() {
        Label secondLabel = new Label("filePath to data base");
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
                    currentDB = new DataBase(dbNameTextField.getText(), new HashMap<String, Table>());
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

    private boolean addColumnToTable(Object className, String columnName)
    {
        if (className == null) {
            showErrorMessage("Choose the type of the last column");
            return false;
        }
        if (columnName.equals("")) {
            showErrorMessage("Choose the name of the last column");
            return false;
        }
        if (className.toString().equals("Enum")) {

            VBox tmpLayout = new VBox();
            Button createEnumButton = new Button("Create Enum");
            TextField enumValues = new TextField();
            tmpLayout.getChildren().addAll(new Label("Enter space-separated values"), enumValues, createEnumButton);
            Stage tmpWindow = new Stage();
            tmpWindow.setTitle("Enum Values");
            tmpWindow.setScene(new Scene(tmpLayout));
            tmpWindow.show();

            createEnumButton.setOnAction(r -> {
                try {
                    String[] enumVals = enumValues.getText().split("\\s+");
                    ArrayList<String> vals = new ArrayList<>();

                    for (int i = 0; i < enumVals.length; ++i) {
                        vals.add(enumVals[i]);
                    }

                    currColumns.add(new ColumnEnum(columnName,"dbtype.AttributeEnum", vals));
                    tmpWindow.close();
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
        }
        else
        {
            try {
                currColumns.add(new Column(columnName, "dbtype.Attribute" + className.toString()));
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void createTable() {
        currColumns.clear();

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
        ArrayList<TextField> textFields = new ArrayList<>();
        TextField columnNameTextField = new TextField();
        textFields.add(columnNameTextField);
        columnCreationLayout.getChildren().addAll(new Label("Column"), comboBox, columnNameTextField);
        _verticalLayout.getChildren().add(columnCreationLayout);


        addNewColumnButton.setOnAction(e -> {
            int curr = comboBoxes.size() - 1;
            boolean isAdded = addColumnToTable(comboBoxes.get(curr).getValue(), textFields.get(curr).getText());

            if (isAdded) {
                HBox _columnCreationLayout = new HBox();
                ComboBox _comboBox = new ComboBox(availableOptions);
                comboBoxes.add(_comboBox);
                TextField _columnNameTextField = new TextField();
                textFields.add(_columnNameTextField);
                _columnCreationLayout.getChildren().addAll(new Label("Column"), _comboBox, _columnNameTextField);
                _verticalLayout.getChildren().add(_columnCreationLayout);
            }
        });

        createNewTableButton.setOnAction(e -> {
            int curr = comboBoxes.size() - 1;
            boolean isAdded = addColumnToTable(comboBoxes.get(curr).getValue(), textFields.get(curr).getText());

            if (isAdded) {
                if (!tableNameTextField.getText().equals("")) {
                    String tableName = tableNameTextField.getText();
                    String tableFilePath = currentDB.getPathForTables() + File.separator + tableName + ".db";
                    Table newTable = new Table(tableFilePath, tableName, currColumns);
                    currentDB.getTables().put(tableName, newTable);
                    newWindow.close();
                    currTable = newTable;
                    showTable(newTable);
                } else {
                    showErrorMessage("Empty table name");
                }
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
        if (currentDB.getPathToFile().equals(tempDB)) {
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
        else
            try {
                currentDB.saveToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void showDataBase() {
        HashMap<String, Table> tables = new HashMap<>();
        for (Table table : currentDB.getTables().values()) {
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
                            table.setField(t.getTablePosition().getRow(), j, newValue);
                        } catch (Exception e) {
                            showErrorMessage(e.toString());
                        }
                    }
            );
            tableView.getColumns().add(col);
        }

        for (int i = 0; i < table.getRows().size(); i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int j = 0; j < table.getRows().get(i).getValues().size(); j++) {
                row.add(table.getRows().get(i).get(j).toString());
            }
            data.add(row);
        }
        tableView.setEditable(true);
        tableView.setItems(data);
    }

    private void addNewRowTable() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String tableName = tab.getText();
        HashMap<String, Table> tables = new HashMap<>();
        for (Table table : currentDB.getTables().values()) {
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
