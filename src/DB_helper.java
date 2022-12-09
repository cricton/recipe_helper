import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Properties;

public class DB_helper implements WindowListener{
    private String db_string = "jdbc:sqlserver://localhost;";
    private Properties db_properties;
    private Connection db_connection;

    public DB_helper(String db_string){
        this.db_string = db_string;

        try {

            db_connection = DriverManager.getConnection(db_string);
            if (db_connection!=null){
                System.out.println("Connected.");
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DB_helper(){
        //trustServerCertificate needed to ignore missing certificate
        db_properties = new Properties();
        db_properties.put("user","SA");
        db_properties.put("password","pass10_QWweadsa");
        db_properties.put("trustServerCertificate","true");
    }

    public boolean connect(){
        try {

            db_connection = DriverManager.getConnection(db_string, db_properties);
            if (db_connection!=null){
                System.out.println("Connected.");
                return true;
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean addRecipe(Recipe recipe){

        try {

            String queryString = "SELECT COUNT(*) FROM recipe_db.dbo.recipes WHERE name = '" + recipe.getName() +"';";
            PreparedStatement stmt = db_connection.prepareStatement(queryString);
            ResultSet result = stmt.executeQuery();
            //if entry exists don't add anything
            while(result.next()){
                if(result.getInt("")==1){
                    return false;
                }
            }

            //add recipe if it does not exist yet
            queryString = "INSERT INTO recipe_db.dbo.recipes VALUES ('" + recipe.getName() + "'," + recipe.getDifficulty() + "," + recipe.getPrepTime() +")";
            stmt = db_connection.prepareStatement(queryString);
            stmt.execute();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    };

    public ResultSet getRecipes(){

        try {
            String queryString = "SELECT * FROM recipe_db.dbo.recipes";
            //System.out.println(queryString);
            PreparedStatement stmt = db_connection.prepareStatement(queryString);
            return stmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    };

    public int getRecipeNumber(){

        try {
            String queryString = "SELECT COUNT(*) FROM recipe_db.dbo.recipes;";
            //System.out.println(queryString);
            PreparedStatement stmt = db_connection.prepareStatement(queryString);
            ResultSet result = stmt.executeQuery();
            while (result.next()){
                return result.getInt("");

            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return 0;
    };
    public boolean removeRecipe(String name){
        if(name==null){
            return false;
        }

        try {
            String queryString = "DELETE FROM recipe_db.dbo.recipes WHERE name = '" + name + "'";
            PreparedStatement stmt = db_connection.prepareStatement(queryString);
            stmt.execute();
            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateRecipe(String name, String value, String type){
        if(name==null){
            return false;
        }

        try {
            String queryString = "UPDATE recipe_db.dbo.recipes SET "+type + " = '"+ value +"' WHERE name = '" + name + "'";
            PreparedStatement stmt = db_connection.prepareStatement(queryString);
            stmt.execute();
            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void addDataEvent(Recipe e) {
        addRecipe(e);
    }

    @Override
    public void deleteDataEvent(String name) {
        removeRecipe(name);
    }

    @Override
    public void updateDataEvent(String name, String value, String type) {
        updateRecipe(name, value, type);
    }

    @Override
    public void fetchDataEvent() {

    }
}
