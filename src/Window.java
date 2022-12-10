import TypeDefs.Recipe;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;



public class Window implements DBEventListener {
    private ArrayList<WindowEventListener> listeners = new ArrayList<>();
    private JPanel window;


    private String currentRecipe;
    private int currentRow;
    private RecipeTableModel tableModel;

    private JPanel recipeDialog;

    public Window(){

        window = new JPanel();
        window.setLayout(new GridBagLayout());

        setupButtons();
        setupTable();

    };

    public void addActionListener(WindowEventListener toAdd){
        listeners.add(toAdd);
    }

    public JPanel getUI(){
        return window;
    }


    public JFrame setupRecipeDialog(){

        JFrame recipeFrame = new JFrame("Add a recipe!");

        recipeDialog = new JPanel();
        recipeDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc;

        JLabel nameText = new JLabel("Recipe name:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(nameText, gbc);

        JTextField nameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(nameField, gbc);

        JLabel difficultyText = new JLabel("Difficulty:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(difficultyText, gbc);

        String[] difficulties = { "Easy", "Moderate", "Hard"};
        JComboBox<String> difficultyField = new JComboBox<>(difficulties);

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(difficultyField, gbc);


        JLabel prepTimeText = new JLabel("Prep time:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(prepTimeText, gbc);

        JTextField prepTimeField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(prepTimeField, gbc);

        JButton btnSave = new JButton("Save");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(btnSave, gbc);
        btnSave.addActionListener(e -> {
            Recipe newRecipe = new Recipe(nameField.getText());
            newRecipe.setDifficulty(difficultyField.getSelectedIndex());
            newRecipe.setPrepTime(Integer.parseInt(prepTimeField.getText()));

            //raise add data event
            for (WindowEventListener listener : listeners)
                listener.addDataEvent(newRecipe);

            //update table
            boolean valueExists = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (newRecipe.getName().equals((String)tableModel.getValueAt(i, 0))){
                    valueExists = true;
                }
            }

            if (!valueExists) tableModel.addRow(new Object[]{newRecipe.getName(), newRecipe.getDifficulty(), newRecipe.getPrepTime()});

            recipeFrame.dispose();
        });

        JButton btnCancel = new JButton("Cancel");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        recipeDialog.add(btnCancel, gbc);
        btnCancel.addActionListener(e -> {
            recipeFrame.dispose();
        });

        recipeFrame.getContentPane().add(recipeDialog);
        recipeFrame.setLocationRelativeTo(null);
        recipeFrame.pack();

        return(recipeFrame);
    }

    public void showRecipeDialog(){
        JFrame recipeFrame = setupRecipeDialog();



        recipeFrame.setVisible(true);
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

                    showRecipeDialog();

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
                    JOptionPane.showMessageDialog(new Frame(),"Make "+tableModel.getValueAt(selector, 0)+"!","TypeDefs.Recipe choice:",JOptionPane.INFORMATION_MESSAGE);

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
                    for (WindowEventListener listener : listeners)
                        listener.deleteDataEvent(currentRecipe);

                    //update table
                    if(currentRecipe!=null){
                        tableModel.removeRow(currentRow);
                    }


                }
        );
    }

    public void addTableData(ResultSet set) throws SQLException {
        while (set.next()){
            tableModel.addRow(new Object[]
                    {set.getString("name"),
                    set.getString("difficulty"),
                    set.getString("preptime")});
        }
    }

    public void setupTable() {

        //generate table with table model
        this.tableModel = new RecipeTableModel();
        JTable table = new JTable(tableModel);

        //setup the table columns and add data
        String[] columnNames = {"Name",
                "Difficulty",
                "Prep Time"};
        tableModel.setColumnIdentifiers(columnNames);



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
                    for (WindowEventListener listener : listeners)
                        listener.updateDataEvent((String) tableModel.getValueAt( row, 0), (String) tableModel.getValueAt(row, column), type);
                }
            }
        });

        //add table to scroll pane
        JScrollPane scrollPane= new JScrollPane(table);
        table.setFillsViewportHeight(true);

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 3;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.weightx = 1;
        window.add(scrollPane, gbc);

    }


    @Override
    public void dataFetchedEvent(ResultSet e) {
        try {
            addTableData(e);
        }catch (SQLException f){
            f.printStackTrace();
        }

    }
}
