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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

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
public class Table extends Application {

    TableView<Item> t;

    ObservableList<Item> items = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        
        //showDatafromDB();
        Label l = new Label("مع تحيـــــات المعتمد على الله     الله اكبر و سبحان الله والحمد لله ");
        Button btnAdd = new Button("   Add   ");
        Button btn1 = new Button("  Search  ");
        Button btn2 = new Button("  Delete  ");
        Button btnShow = new Button("  Show Data  ");
        Button btnCreate = new Button("  Craete DB  ");

        TextField monthT = new TextField();
        monthT.setPromptText("Month");
        TextField dayT = new TextField();
        dayT.setPromptText("day");
        TextField typeT = new TextField();
        typeT.setPromptText("type");
        TextField costT = new TextField();
        costT.setPromptText("cost");

        //Actions 
        btnAdd.setOnAction((ActionEvent e) -> {
            try {
                addItem(monthT.getText(), dayT.getText(), typeT.getText(), costT.getText());
            } catch (SQLException ex) {
                Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            }
            //update data on table after insert(add)  operation 
            showDatafromDB();
        });

        btn1.setOnAction(e -> {

            Search search = new Search();

        });
        btn2.setOnAction(e -> {

            Delete delete = new Delete();
            //update data on table after delete operation 
            showDatafromDB();
        });

        btnCreate.setOnAction(e -> {

            createDB();

        });

        btnShow.setOnAction(e -> {
            showDatafromDB();
        });

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

        t = new TableView<>();
        t.setItems(items);
        t.getColumns().addAll(mon, day, type, cost);

        VBox root = new VBox();

        //style
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #33ccff 0%, #ff99cc 100%)");

        //main controls
        HBox h = new HBox();
        h.setSpacing(10);
        h.setAlignment(Pos.CENTER);
        h.setPadding(new Insets(10));
        h.getChildren().addAll(btnCreate, btnShow, btn1, btn2);

        //Text field to add  data 
        HBox hT = new HBox();
        hT.getChildren().addAll(monthT, dayT, typeT, costT, btnAdd);
        hT.setSpacing(10);
        hT.setAlignment(Pos.CENTER);
        hT.setPadding(new Insets(10));
        root.getChildren().addAll(h, hT, t, l);

        Scene scene = new Scene(root, 800, 250);

        primaryStage.setTitle(" MyCostRecorder ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showDatafromDB() {

        items.clear();

        // Read From the DataBase and add it to the TableView
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db/Items.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM item ");

            // loop through the result set
            while (rs.next()) {

                items.add(
                        new Item(rs.getInt("month"), rs.getInt("day"), rs.getString("type"), rs.getDouble("cost"))
                );

            }

            conn.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addItem(String monthT, String dayT, String typeT, String costT) throws SQLException {

        try {
            //getData
            int month = Integer.parseInt(monthT);
            int day = Integer.parseInt(dayT);
            double cost = Double.parseDouble(costT);

            //connectionand to  Add 
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db/Items.db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO item VALUES(" + month + "," + day + ",'"
                    + typeT + "'," + cost + ")");

            stmt.close();
            conn.close();

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

    }

    public void createDB() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db/Items.db");
            Statement st = conn.createStatement();

            
            //create a table if it not exist 
            st.executeUpdate("create table if not exist  item ( month int  , day int  , type varchar(60) , cost  double  )");

            conn.close();
            st.close();

        } catch (SQLException ex) {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
