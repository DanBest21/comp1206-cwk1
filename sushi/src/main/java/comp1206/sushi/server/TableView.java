package comp1206.sushi.server;

import comp1206.sushi.common.*;
import comp1206.sushi.mock.MockServer;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TableView extends JTabbedPane
{
    private HashMap<String, JScrollPane> tabs = new HashMap<>();
    private ServerInterface server;
    private HashMap<String, List<? extends Model>> loadedData = new HashMap<>();

    private Color red = new Color(170, 50, 50);
    private Font titleFont = new Font("Viner Hand ITC", Font.BOLD, 20);
    private Font font = new Font("Courier New", Font.PLAIN, 16);

    public TableView(String[] tabs)
    {
        // TODO: Replace with actual server when created.
        server = new MockServer();

        for (String tab : tabs)
        {
            JScrollPane table = generateTable(tab);
            this.tabs.put(tab, table);
            this.addTab(tab, this.tabs.get(tab));

            this.setOpaque(false);
            this.setFont(titleFont.deriveFont((float)16).deriveFont(Font.PLAIN));
        }
    }

    private JScrollPane generateTable(String tab)
    {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        table.setFont(font);
        table.setRowHeight(16);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setOpaque(false);
        tableHeader.setBackground(red);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(titleFont);

        loadData(tab, model);

        formatTable(table);

        table.setDefaultEditor(Object.class, null);

        JScrollPane pane = new JScrollPane(table);
        pane.setLayout(new ScrollPaneLayout());
        pane.setMinimumSize(new Dimension(800, 400));
        pane.setBackground(Color.WHITE);
        pane.getViewport().setBackground(Color.WHITE);

        return pane;
    }

    private void loadData(String type, DefaultTableModel model)
    {
        List<? extends Model> data = new ArrayList<>();

        switch (type)
        {
            case "Orders":
                data = server.getOrders();
                break;

            case "Dishes":
                data = server.getDishes();
                break;

            case "Ingredients":
                data = server.getIngredients();
                break;

            case "Suppliers":
                data = server.getSuppliers();
                break;

            case "Staff":
                data = server.getStaff();
                break;

            case "Users":
                data = server.getUsers();
                break;

            case "Drones":
                data = server.getDrones();
                break;

            case "Postcodes":
                data = server.getPostcodes();
                break;

            case "Configuration":
                // TODO: Implement Configuration case.
                break;
        }

        if (data.size() <= 0)
        {
            return;
        }

        TableData tableData = new TableData();
        tableData.loadData(data, model);

        loadedData.put(type, data);
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

        String tab = this.getTitleAt(this.getSelectedIndex());
        List<? extends Model> objects = loadedData.get(tab);

        switch (tab)
        {
            case "Postcodes":
                try
                {
                    server.removePostcode((Postcode)objects.get(getSelectedTable().getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    System.err.println(ex);
                }
                break;

            case "Drones":
                try
                {
                    server.removeDrone((Drone)objects.get(getSelectedTable().getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    System.err.println(ex);
                }
                break;

            case "Staff":
                try
                {
                    server.removeStaff((Staff)objects.get(getSelectedTable().getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    System.err.println(ex);
                }
                break;
        }

        model.removeRow(getSelectedTable().getSelectedRow());
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
