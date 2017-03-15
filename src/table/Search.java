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
public class Search {

    TextField monthT, monSum;
    TextField dayT , dayS;

    Label sumM ,toS,toSum ;
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
        dayT = new TextField();
        dayS = new TextField();

        
        
        toS = new Label("enter (month-day) to search ::  0 to all days  ::  ");
        toSum = new Label("enter (month-day) to sum of cost ::  0 to all days  ::  "); 
        sumM = new Label("your spent is  :: ");
        
            Label l  =new Label ("مع تحيـــــات المعتمد على الله     الله اكبر و سبحان الله والحمد لله ") ; 
     
        Button s = new Button(":: search  :: ");
        Button s2 = new Button(" :: sum of costs :: ");

        //actions 
        s.setOnAction((ActionEvent e) -> {
            //as it w String x = monthT.getText()ill sent to sql as string 
            String x = monthT.getText();
            String y = dayT.getText();


            show(x , y );

        });
        s2.setOnAction(e -> {
            String xx = monSum.getText();
             String yy = dayS.getText();
            sum(xx ,yy );

        });

        t = new TableView<>();
        t.setItems(items);
        t.getColumns().addAll(mon, day, type, cost);

        //main layout 
        VBox root = new VBox();
        //select month 
        HBox h = new HBox();
        h.setSpacing(10);
        h.setAlignment(Pos.CENTER);
        h.setPadding(new Insets(10));
        h.getChildren().addAll( toS,monthT,dayT, s);

        
        //sum of month
        HBox h2 = new HBox();
        h2.setSpacing(10);
        h2.setAlignment(Pos.CENTER);
        h2.setPadding(new Insets(10));
        h2.getChildren().addAll( toSum, monSum, dayS, sumM, s2);

        root.getChildren().addAll(h, h2, t , l );
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffccff 0%, #ff9900 100%)" );
        

        Scene sce = new Scene(root, 1000, 300);

        window.setScene(sce);
        window.setTitle("search in Log ");
        window.showAndWait();

    }

    public void show(String month, String day ) {
        items.clear();
        
        
        //default is search for all days of a month 
        String sql ="select * from item where month=" + month + "" ;
        
        
        //search for all records 
        if ( month.equals("0") && day.equals("0"))
            sql = "select * from item  ";
       //search for days  from all months 
        else if (month.equals("0"))
        sql = "select * from item where day=" + day + "" ;
        // search for all days of a month 
        else if (day.equals("0") ) 
            sql = "select * from item where month=" + month + " " ; 
        //search for a day to a month 
        else 
            sql = "select * from item where month=" + month + " and day ="+day+"";
        
        
        
        
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:db/Items.db");
            Statement st = c.createStatement();

            //select a row of specific month 
            ResultSet rs = st.executeQuery(sql);

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

    public void sum(String month, String day ) {
        
         //default is cost for all days of a month 
        String sql  ;
       // "select sum(cost) from item where month=" + month + "" 
        
        //cost for all records 
        if ( month.equals("0") && day.equals("0"))
            sql = " select sum(cost) from item  where month > 0 ";
       //cost for days  from all months 
        else if (month.equals("0"))
        sql = "select sum(cost) from item where day =" + day + "" ;
        // cost for all days of a month 
        else if (day.equals("0") ) 
            sql = "select sum(cost) from item where month=" + month + ""; 
        //cost  for a day to a month 
        else 
            sql = "select sum(cost) from item where month =" + month + " and day= "+day +"";

        try {
            Statement st;
            Connection c = DriverManager.getConnection("jdbc:sqlite:db/Items.db"); 
                st = c.createStatement();
                
                ResultSet rs = st.executeQuery(sql);

                sumM.setText("oh you spent " + rs.getString(1) + ", you should save your money  ^^^^");

            
            st.close();
            c.close();

        } catch (SQLException s) {
            System.out.println(s.toString());
        }

    }

}
