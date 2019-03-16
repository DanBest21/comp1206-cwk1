package comp1206.sushi.server;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;

public class TableView extends JTabbedPane
{
    private HashMap<String, JScrollPane> tables = new HashMap<>();

    public TableView(String[] tabs)
    {
        for (String tab : tabs)
        {
            JScrollPane table = generateTable();
            tables.put(tab, table);
            this.addTab(tab, tables.get(tab));
        }
    }

    private JScrollPane generateTable()
    {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        // TODO: Remove test code.
        String[] test = {"Test", "Test"};
        model.addColumn("Column 1");
        model.addColumn("Column 2");
        model.addRow(test);

        JScrollPane pane = new JScrollPane(table);
        pane.setLayout(new ScrollPaneLayout());
        pane.setMinimumSize(new Dimension(800, 400));

        return pane;
    }
}
