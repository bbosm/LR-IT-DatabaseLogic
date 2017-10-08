package com.univ.it.javafx;

import com.univ.it.db.Column;
import com.univ.it.db.Table;
import com.univ.it.dbtype.Attribute;
import com.univ.it.dbtype.AttributeDate;
import com.univ.it.dbtype.AttributeInteger;
import com.univ.it.dbtype.DBTypeId;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.text.ParseException;
import java.util.ArrayList;

public class Controller {
    public TableView<String[]> tableView;
    public Table table;

    public void sayHelloWorld(ActionEvent actionEvent) throws
            NoSuchMethodException,
            ClassNotFoundException,
            ParseException {
        tableView = new TableView<>();
        ObservableList<String[]> data = FXCollections.observableArrayList();

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Date", "com.univ.it.dbtype.AttributeDate"));
        columns.add(new Column("Date","com.univ.it.dbtype.AttributeInteger"));

        Table table = new Table(new DBTypeId(0), "testTable", columns);

        ArrayList<Attribute> values = new ArrayList<>();
        values.add(new AttributeDate("2017.10.06"));
        values.add(new AttributeInteger(6));
        table.addRow(values);

        values = new ArrayList<>();
        values.add(new AttributeDate("2017.10.07"));
        values.add(new AttributeInteger(8));
        table.addRow(values);

        tableView = new TableView<>();
        for (int i = 0; i < table.getColumns().size(); i++) {
            TableColumn tc = new TableColumn(table.getColumns().get(i).getTypeName());
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            tableView.getColumns().add(tc);
        }
        tableView.setItems(data);
    }
}
