/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author mo3tamed
 */
public class Search {

    TextField monthT, monSum;
    TextField dayT;

    Label sumM;
    ObservableList<Item> items = FXCollections.observableArrayList();

    public Search() {
        Stage window = new Stage();

        TableView<Item> t;

        //table
        TableColumn<Item, String> mon = new TableColumn<>("Month");
        mon.setMinWidth(100);
        mon.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<Item, String> day = new TableColumn<>("Day");
        day.setMinWidth(100);
        day.setCellValueFactory(new PropertyValueFactory<>("day"));

        TableColumn<Item, String> type = new TableColumn<>("Type");
        type.setMinWidth(200);
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Item, String> cost = new TableColumn<>("Cost");
        cost.setMinWidth(100);
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        //controls 
        monthT = new TextField();
        monSum = new TextField();
        monthT.setPromptText("month to search ");
        monSum.setPromptText("month to get sum of all cost  ");
        //sumM=new Lable (" oh you spent 0   , you should save your money  ^^^^ ") ; 
        sumM = new Label("your spent is  :: ");
        Button s = new Button(":: search  :: ");
        Button s2 = new Button(" :: sum of month :: ");

        //actions 
        s.setOnAction(e -> {
            //as it will sent to sql as string 
            String x = monthT.getText();

            show(x);

        });
        s2.setOnAction(e -> {
            String x = monSum.getText();

            sum(x);

        });

        t = new TableView<>();
        t.setItems(items);
        t.getColumns().addAll(mon, day, type, cost);

        //main layout 
        VBox root = new VBox();
        //select month 
        HBox h = new HBox();
        h.setSpacing(10);
        h.setPadding(new Insets(10));
        h.getChildren().addAll(monthT, s);

        
        //sum of month
        HBox h2 = new HBox();
        h2.setSpacing(10);
        h2.setAlignment(Pos.CENTER);
        h2.setPadding(new Insets(10));
        h2.getChildren().addAll(monSum, sumM, s2);

        root.getChildren().addAll(h, h2, t);

        Scene sce = new Scene(root, 800, 150);

        window.setScene(sce);
        window.setTitle("search in Log ");
        window.showAndWait();

    }

    public void show(String month) {
        items.clear();
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:Items.db");
            Statement st = c.createStatement();

            //select a row of specific month 
            ResultSet rs = st.executeQuery("select * from item where month=" + month + "");

            while (rs.next()) {
                //add item
                items.add(
                        new Item(rs.getInt("month"), rs.getInt("day"), rs.getString("type"), rs.getDouble("cost"))
                );

            }

            c.close();
            st.close();

        } catch (SQLException s) {
            System.out.println(s.toString());
        }

    }

    public void sum(String m) {

        try {
            Statement st;
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:Items.db")) {
                st = c.createStatement();
                //select a row of specific month
                ResultSet rs = st.executeQuery("select sum(cost) from item where month=" + m + "");

                sumM.setText("oh you spent " + rs.getString(1) + ", you should save your money  ^^^^");

            }
            st.close();

        } catch (SQLException s) {
            System.out.println(s.toString());
        }

    }

}