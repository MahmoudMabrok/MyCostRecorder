/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package table;

// 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author mo3tamed
 */
public class Delete {
    TextField monthT;
    TextField dayT ;

    Label l_del  ,l_del2 , status  ;
    Button btDel , cancel  ; 

    
    public  Delete ()
    {
    
       Stage window2  = new Stage ();
       
       
       
       monthT = new TextField();
       monthT.setPromptText("month");
       
       dayT = new TextField();
       dayT.setPromptText("day");
       l_del = new Label (" :: enter  month-day to remove 0 to all day-month ");
       l_del2 = new Label ("0-0 to remove all data :: ");
           Label l  =new Label ("مع تحيـــــات المعتمد على الله     الله اكبر و سبحان الله والحمد لله ") ; 
     
       status = new Label(".... ");
              
       btDel = new Button ("!!! delete !!! ");
       cancel = new Button ("cancel  ");
       
       btDel.setOnAction(e-> {
       //as it w String x = monthT.getText()ill sent to sql as string 
            String x = monthT.getText();
            String y = dayT.getText();
           delete( x , y );
          
       });
       cancel.setOnAction(e->{
    
           window2.close();
    });
       
       
       
       //main layout 
        VBox root = new VBox();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #00ffcc 24%, #33cc33 97%)" );
         
        
       
        root.setAlignment(Pos.CENTER);
        root.setSpacing(17);
        root.setPadding(new Insets(30));
        root.getChildren().addAll( l_del,l_del2 , monthT,dayT,btDel , cancel , status,l );
        
        
        Scene s = new Scene(root , 350 , 400 );
        window2.setScene(s);
        window2.centerOnScreen();
        window2.setTitle("Delete Operation ");
        window2.showAndWait();
       
    
    
    }
    
    public void delete (String month, String day)
    {
        
            //default is delete for all days of a month 
        String sql ="" ;
        
        
        //delete for all records 
        if ( month.equals("0") && day.equals("0"))
            sql = "delete from item  ";
       //delete for days  from all months 
        else if (month.equals("0"))
        sql = "delete from item where day=" + day + "" ;
        // delete for all days of a month 
        else if (day.equals("0") ) 
            sql = "delete from item where month=" + month + " " ; 
        //delete for a day to a month 
        else 
            sql = "delete from item where month=" + month + " and day ="+day+"";
        
        try {
        Connection c = DriverManager.getConnection("jdbc:sqlite:db/Items.db");
        Statement st = c.createStatement();
        st.executeUpdate(sql);
        status.setText(" successfuly  deleted   ");
        st.close();
        c.close();
            
            
        }catch (SQLException s )    
        {
            System.out.println(s.toString());
        }
        
    
    
    }
    
}
