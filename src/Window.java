import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;



public class Window {
    private ArrayList<WindowListener> listeners = new ArrayList<>();
    private JPanel window;
    private DB_helper helper;

    private String currentRecipe;
    private int currentRow;
    private RecipeTableModel tableModel;


    public Window(DB_helper helper){
        this.helper = helper;

        window = new JPanel();
        window.setLayout(new GridBagLayout());

        setupButtons();
        try {
            setupTable();
        }catch (SQLException e){
            e.printStackTrace();
        }

    };

    public void addActionListener(WindowListener toAdd){
        listeners.add(toAdd);
    }

    public JPanel getUI(){
        return window;
    }

    private ResultSet fetchAllData(){
        return helper.getRecipes();
    }
    private int fetchDataSize(){
        return helper.getRecipeNumber();
    }

    public void setupButtons(){

        GridBagConstraints gbc;
        JButton btnSave = new JButton("Add Recipe");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 0);
        gbc.weightx = 1;
        window.add(btnSave, gbc);

        btnSave.addActionListener(
                e -> {
                    Recipe test = new Recipe("Toast");
                    //JOptionPane.showInputDialog(new Frame(),"Make "+tableModel.getValueAt(selector, 0)+"!","Recipe choice:",JOptionPane.INFORMATION_MESSAGE);

                    //raise add data event
                    for (WindowListener hl : listeners)
                        hl.addDataEvent(test);

                    //update table
                    tableModel.addRow(new Object[]{test.getName(), test.getDifficulty(), test.getPrepTime()});

                }
        );

        JButton btnRandom = new JButton("Choose for me!");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1;
        window.add(btnRandom, gbc);

        //add delete action listener
        btnRandom.addActionListener(
                e -> {
                    Random r = new Random();
                    int selector = r.nextInt(tableModel.getRowCount());
                    JOptionPane.showMessageDialog(new Frame(),"Make "+tableModel.getValueAt(selector, 0)+"!","Recipe choice:",JOptionPane.INFORMATION_MESSAGE);

                }
        );

        JButton btnDelete = new JButton("Delete Recipe");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.weightx = 1;
        window.add(btnDelete, gbc);

        //add delete action listener
        btnDelete.addActionListener(
                e -> {

                    //raise delete add data event
                    for (WindowListener hl : listeners)
                        hl.deleteDataEvent(currentRecipe);

                    //update table
                    tableModel.removeRow(currentRow);

                }
        );
    }



    public void setupTable() throws SQLException {

        //fetch recipes from local database
        ResultSet db_data = fetchAllData();

        //generate table with table model
        this.tableModel = new RecipeTableModel();
        JTable table = new JTable(tableModel);

        //setup the table columns and add data
        String[] columnNames = {"Name",
                "Difficulty",
                "Prep Time"};
        tableModel.setColumnIdentifiers(columnNames);
        while (db_data.next()){
            tableModel.addRow(new Object[]
                    {db_data.getString("name"),
                     db_data.getString("difficulty"),
                     db_data.getString("preptime")});
        }


        //add list selection listener to be able to delete entries
        table.setCellSelectionEnabled(true);
        ListSelectionModel select= table.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(e -> {
            if(e.getValueIsAdjusting()){
                int row= table.getSelectedRows()[0];
                currentRecipe = (String) table.getValueAt(row, 0);
                currentRow = row;
            }
        });

        //add a tablemodellistener to check for updates in the table. Saves updates to the DB
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e){
                //if we have a cell update
                if(e.getType()==TableModelEvent.UPDATE){
                    int row= table.getEditingRow();
                    int column = table.getEditingColumn();

                    String type = "";
                    if(column == 1){
                        type = "difficulty";
                    }else if (column == 2){
                        type = "preptime";
                    }


                    //raise update data event
                    for (WindowListener hl : listeners)
                        hl.updateDataEvent((String) tableModel.getValueAt( row, 0), (String) tableModel.getValueAt(row, column), type);
                }
            }
        });

        //add table to scroll pane
        JScrollPane scrollPane= new JScrollPane(table);
        table.setFillsViewportHeight(true);

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 3;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.weightx = 1;
        window.add(scrollPane, gbc);

    }


}
