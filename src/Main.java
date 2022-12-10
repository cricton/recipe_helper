import TypeDefs.Ingredient;
import TypeDefs.Recipe;

import javax.swing.*;
import java.sql.SQLException;


public class Main {



    public static void main(String[] args) throws SQLException {



        Recipe taco = new Recipe("Tacos");
        taco.addIngredient(new Ingredient("Wraps", Ingredient.Unit.pieces), 6);



        //create database helper to connect to local DB
        DB_helper helper = new DB_helper();
        Window window = new Window();



        //setup event listeners
        Connector conn = new Connector();
        conn.addComponent(window);
        conn.addComponent(helper);
        conn.setupEventListener();

        //connect to DB and fetch all recipes
        helper.connect();
        helper.getRecipes();

        Recipe test = new Recipe("Shoe", 12, 2);
        helper.addRecipe(test);

        JFrame frame = new JFrame("Recipe Book");
        frame.getContentPane().add(window.getUI());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}