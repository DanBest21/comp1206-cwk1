package comp1206.sushi.server.forms;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public abstract class EntryForm extends JDialog
{
    private ServerInterface server;
    private TableView view;
    private String formName;
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraints = new GridBagConstraints();
    private JPanel contentPane = new JPanel(layout);
    private int noRows = 0;
    private static final int COMPONENT_HEIGHT = 60;
    private static final int DIALOG_WIDTH = 500;

    public EntryForm(ServerInterface server, TableView view, String formName)
    {
        this.setTitle(formName);
        this.server = server;
        this.view = view;
        this.formName = formName;

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setIconImage(ServerConfiguration.getImg());

        constraints.insets = new Insets(5, 5, 5, 5);

        setContentPane(contentPane);
        contentPane.setBackground(ServerConfiguration.getColour());
    }

    public abstract void generateForm();

    protected JTextField createTextField(String name)
    {
        createLabel(name);

        JTextField textField = new JTextField(22);
        textField.setFont(ServerConfiguration.getFont());
        textField.setHorizontalAlignment(SwingConstants.RIGHT);

        setGridBagConstraints(textField,1, noRows, 1, GridBagConstraints.EAST);
        addToContentPane(textField);

        return textField;
    }

    protected JSpinner createSpinner(String name, int initialValue, int minValue, int maxValue, int interval)
    {
        createLabel(name);

        SpinnerModel model = new SpinnerNumberModel(initialValue, minValue, maxValue, interval);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(ServerConfiguration.getFont());

        setGridBagConstraints(spinner,1, noRows, 1, GridBagConstraints.EAST);
        addToContentPane(spinner);

        return spinner;
    }

    protected JComboBox createComboBox(String name, String type)
    {
        createLabel(name);

        JComboBox comboBox = new JComboBox();
        List<? extends Model> items = view.getParser().getData(type);

        Class itemClass = items.get(0).getClass();

        for (Model item : items)
        {
            comboBox.addItem(itemClass.cast(item));
        }

        // *******************************************************************************************************
        // * Title: Combo Box with Custom Renderer
        // * Author: Rob Camick
        // * Date: 2013
        // * Availability: https://tips4java.wordpress.com/2013/11/17/combo-box-with-custom-renderer/
        // ******************************************************************************************************/
        comboBox.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Model)
                {
                    Model model = (Model)value;
                    setText(model.getName());
                }

                return this;
            }
        });

        comboBox.setFont(ServerConfiguration.getFont());

        setGridBagConstraints(comboBox, 1, noRows, 1, GridBagConstraints.EAST);
        addToContentPane(comboBox);

        return comboBox;
    }

    protected JTextArea createTextArea(String name, int lines)
    {
        createLabel(name);

        JTextArea textArea = new JTextArea(lines, 22);

        textArea.setLineWrap(true);
        textArea.setFont(ServerConfiguration.getFont());

        setGridBagConstraints(textArea,1, noRows, 1, GridBagConstraints.EAST);
        addToContentPane(textArea);
        noRows = noRows + (lines - 1);

        return textArea;
    }

    protected JFormattedTextField createCurrencyField(String name)
    {
        createLabel(name);

        // **********************************************************************************************************************************
        // * Title: Adapted currency formatter
        // * Author: Reimeus
        // * Date: 2013
        // * Availability: https://stackoverflow.com/questions/17246676/a-jformattedtextfield-to-accept-a-currency-format-without-decimals
        // *********************************************************************************************************************************/
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.UK);
        format.setCurrency(Currency.getInstance("GBP"));

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.00);
        formatter.setMaximum(1000.00);
        formatter.setAllowsInvalid(false);

        JFormattedTextField formattedTextField = new JFormattedTextField(formatter);
        formattedTextField.setColumns(22);
        formattedTextField.setFont(ServerConfiguration.getFont());
        formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        formattedTextField.setValue(0.00);

        setGridBagConstraints(formattedTextField,1, noRows, 1, GridBagConstraints.EAST);
        addToContentPane(formattedTextField);

        return formattedTextField;
    }

    protected JButton createSubmitButton()
    {
        JButton btn = new JButton("Submit " + formName.split(" ")[1]);
        btn.setFont(ServerConfiguration.getSmallTitleFont());

        setGridBagConstraints(btn, 0, noRows, 2, GridBagConstraints.CENTER);
        addToContentPane(btn);

        return btn;
    }

    protected void resizeWindow()
    {
        setSize(DIALOG_WIDTH, noRows * COMPONENT_HEIGHT);
    }

    protected void resizeWindow(double percentage)
    {
        setSize(DIALOG_WIDTH, (int)(noRows * COMPONENT_HEIGHT * percentage));
    }

    protected ServerInterface getServer()
    {
        return server;
    }

    protected TableView getTableView()
    {
        return view;
    }

    protected JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text + ": ");

        label.setFont(ServerConfiguration.getSmallTitleFont());
        label.setForeground(Color.WHITE);

        setGridBagConstraints(label,0, noRows, 1, GridBagConstraints.NORTHWEST);
        addToContentPane(label);

        return label;
    }

    private void addToContentPane(JLabel label)
    {
        contentPane.add(label);
    }

    protected void addToContentPane(Component component)
    {
        contentPane.add(component);
        noRows++;
    }

    protected void setGridBagConstraints(Component component, int x, int y, int width, int alignment)
    {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.anchor = alignment;

        layout.setConstraints(component, constraints);
    }

    public int getNoRows()
    {
        return noRows;
    }

    protected void setNoRows(int noRows)
    {
        this.noRows = noRows;
    }

    protected void throwMissingFieldsInputError()
    {
        displayErrorMessage("All fields must be populated before a record can be inserted.");
    }

    protected void throwDuplicateUniqueFieldError(String field, String value)
    {
        displayErrorMessage("The " + field + " field should be unique - the value of \"" + value + "\" is already present in an existing record.");
    }

    protected void displayErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(new JDialog(), message + "\r\nPlease correct this issue and then try again.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
