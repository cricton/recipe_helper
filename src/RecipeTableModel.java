import javax.swing.table.DefaultTableModel;

public class RecipeTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int col) {
        if(col==0){
            return false;
        }
        return true;
    }
}
