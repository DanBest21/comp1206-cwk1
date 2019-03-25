package comp1206.sushi.server.table;

import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.ServerWindow;
import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.map.MapView;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.*;

public class TableView extends JTabbedPane
{
    private HashMap<String, Container> tabs = new HashMap<>();
    private static TableParser parser;
    private static ServerWindow window;

    // TableView class - Daniel Best, 2019
    public TableView(String[] tabs, ServerInterface server, ServerWindow window)
    {
        this.window = window;

        parser = new TableParser(server);
        setBackground(Color.WHITE);

        for (String tab : tabs)
        {
            if (tab == "Map")
            {
                MapView mapView = new MapView(server);
                this.tabs.put(tab, mapView);
            }
            else
            {
                JScrollPane table = generateTable(tab);
                this.tabs.put(tab, table);
            }

            addTab(tab, this.tabs.get(tab));

            setOpaque(false);
            setFont(ServerConfiguration.getSmallTitleFont());
        }

        addChangeListener(e -> {
            if (this.tabs.get("Map") == getSelectedComponent())
                window.setupForMapView();
            else
                window.setResizable(true);
        });
    }

    private JScrollPane generateTable(String tab)
    {
        window.setResizable(true);

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

    // *******************************************************************************************************
    // * Title: Modified updateRowHeights() function
    // * Author: camickr
    // * Date: 2009
    // * Availability: https://stackoverflow.com/questions/1783607/auto-adjust-the-height-of-rows-in-a-jtable
    // ******************************************************************************************************/
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

    public JTable getSelectedTable()
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

    public void updateTables()
    {
        for (String tab : tabs.keySet())
        {
            updateTable(tab);
        }
    }
}
