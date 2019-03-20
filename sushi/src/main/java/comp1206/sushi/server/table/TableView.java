package comp1206.sushi.server.table;

import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.configuration.ServerConfiguration;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.*;

public class TableView extends JTabbedPane
{
    private static HashMap<String, JScrollPane> tabs = new HashMap<>();
    private static TableParser parser;

    public TableView(String[] tabs, ServerInterface server)
    {
        parser = new TableParser(server);
        setBackground(Color.WHITE);

        for (String tab : tabs)
        {
            JScrollPane table = generateTable(tab);
            TableView.tabs.put(tab, table);
            this.addTab(tab, TableView.tabs.get(tab));

            this.setOpaque(false);
            this.setFont(ServerConfiguration.getSmallTitleFont());
        }
    }

    private JScrollPane generateTable(String tab)
    {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        table.setFont(ServerConfiguration.getFont());
        table.setRowHeight(16);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setOpaque(false);
        tableHeader.setBackground(ServerConfiguration.getColour());
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(ServerConfiguration.getTitleFont());

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

    private DefaultTableModel getTableModel()
    {
        return (DefaultTableModel)getSelectedTable().getModel();
    }

    private JTable getSelectedTable()
    {
        JScrollPane pane = (JScrollPane)this.getSelectedComponent();
        return (JTable)pane.getViewport().getView();
    }

    public TableParser getParser()
    {
        return parser;
    }

    public void updateTable(String tab)
    {
        tabs.put(tab, generateTable(tab));

        int index = this.indexOfTab(tab);

        this.setComponentAt(index, tabs.get(tab));
    }
}
