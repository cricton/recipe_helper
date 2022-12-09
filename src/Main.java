import javax.swing.*;
import java.sql.SQLException;


public class Main {



    public static void main(String[] args) throws SQLException {



        Recipe taco = new Recipe("Tacos");
        taco.addIngredient(new Ingredient("Wraps", Ingredient.Unit.pieces), 6);



        //create database helper to connect to local DB
        DB_helper helper = new DB_helper();
        Recipe test = new Recipe("Sauce2", 12, 2);
        //helper.addRecipe(test);



        //window.setupButtons();
        //window.setupTable();
        //window.setupTextFields();
        //window.show();
        JFrame frame = new JFrame("Recipe Book");
        frame.getContentPane().add(new Window(helper).getUI());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}