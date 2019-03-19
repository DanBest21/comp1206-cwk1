package comp1206.sushi.server.table;

import comp1206.sushi.server.ServerInterface;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.*;

public class TableView extends JTabbedPane
{
    private static HashMap<String, JScrollPane> tabs = new HashMap<>();
    private static TableParser parser;

    private static final Color RED = new Color(170, 50, 50);
    private static final Font TITLE_FONT = new Font("Viner Hand ITC", Font.BOLD, 20);
    private static final Font FONT = new Font("Courier New", Font.PLAIN, 16);

    public TableView(String[] tabs, ServerInterface server)
    {
        parser = new TableParser(server);

        for (String tab : tabs)
        {
            JScrollPane table = generateTable(tab);
            this.tabs.put(tab, table);
            this.addTab(tab, this.tabs.get(tab));

            this.setOpaque(false);
            this.setFont(TITLE_FONT.deriveFont((float)16).deriveFont(Font.PLAIN));
        }
    }

    private JScrollPane generateTable(String tab)
    {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        table.setFont(FONT);
        table.setRowHeight(16);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setOpaque(false);
        tableHeader.setBackground(RED);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(TITLE_FONT);

        parser.loadData(tab, model);

        formatTable(table);

        table.setDefaultEditor(Object.class, null);

        JScrollPane pane = new JScrollPane(table);
        pane.setLayout(new ScrollPaneLayout());
        pane.setMinimumSize(new Dimension(800, 400));
        pane.setBackground(Color.WHITE);
        pane.getViewport().setBackground(Color.WHITE);

        return pane;
    }

    private void formatTable(JTable table)
    {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(SwingConstants.NORTH);

        for (int row = 0; row < table.getRowCount(); row++)
        {
            int rowHeight = table.getRowHeight();

            for (int column = 0; column < table.getColumnCount(); column++)
            {
                table.getColumnModel().getColumn(column).setCellRenderer(renderer);

                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            table.setRowHeight(row, rowHeight);
        }
    }

    public void removeRow()
    {
        DefaultTableModel model = getTableModel();
        JTable table = getSelectedTable();

        if (table.getSelectedRow() == -1)
            return;

        if (parser.removeRow(this.getTitleAt(this.getSelectedIndex()), table))
            model.removeRow(table.getSelectedRow());
    }

    public DefaultTableModel getTableModel()
    {
        return (DefaultTableModel)getSelectedTable().getModel();
    }

    public JTable getSelectedTable()
    {
        JScrollPane pane = (JScrollPane)this.getSelectedComponent();
        return (JTable)pane.getViewport().getView();
    }
}
