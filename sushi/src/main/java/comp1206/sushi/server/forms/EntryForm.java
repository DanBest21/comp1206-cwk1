package comp1206.sushi.server.forms;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class EntryForm extends JFrame
{
    private ServerInterface server;
    private TableView view;
    private String formName;
    private JPanel contentPane = new JPanel(new GridLayout(0, 1));
    private int noComponents = 0;
    private static final int COMPONENT_HEIGHT = 60;

    public EntryForm(ServerInterface server, TableView view, String formName)
    {
        super("Sushi Server");
        this.setTitle(formName);
        this.server = server;
        this.view = view;
        this.formName = formName;

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(ServerConfiguration.getImg());

        setContentPane(contentPane);
        contentPane.setBackground(ServerConfiguration.getColour());

        generateForm();

        setVisible(true);
    }

    public abstract void generateForm();

    protected JTextField createTextField(String name)
    {
        JPanel panel = initialisePanel(name);

        JTextField textField = new JTextField(22);
        textField.setFont(ServerConfiguration.getFont());
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(textField);

        addToContentPane(panel);

        return textField;
    }

    protected JSpinner createSpinner(String name, int initialValue, int minValue, int maxValue, int interval)
    {
        JPanel panel = initialisePanel(name);

        SpinnerModel model = new SpinnerNumberModel(initialValue, minValue, maxValue, interval);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(ServerConfiguration.getFont());
        panel.add(spinner);

        addToContentPane(panel);

        return spinner;
    }

    protected JComboBox createComboBox(String name, String type)
    {
        JPanel panel = initialisePanel(name);

        JComboBox comboBox = new JComboBox();
        List<? extends Model> items = view.getParser().getData(type);

        Class itemClass = items.get(0).getClass();

        for (Model item : items)
        {
            comboBox.addItem(itemClass.cast(item));
        }

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
        panel.add(comboBox);

        addToContentPane(panel);

        return comboBox;
    }

    protected JButton createSubmitButton()
    {
        JPanel panel = initialisePanel();

        JButton btn = new JButton("Submit " + formName.split(" ")[1]);
        btn.setFont(ServerConfiguration.getSmallTitleFont());
        panel.add(btn);

        panel.setBackground(ServerConfiguration.getColour());

        addToContentPane(panel);

        return btn;
    }

    protected void resizeWindow()
    {
        setSize(400, noComponents * COMPONENT_HEIGHT);
    }

    protected ServerInterface getServer()
    {
        return server;
    }

    protected TableView getTableView()
    {
        return view;
    }

    private JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text + ": ");

        label.setFont(ServerConfiguration.getSmallTitleFont());
        label.setForeground(Color.WHITE);

        return label;
    }

    private void addToContentPane(JPanel panel)
    {
        panel.setBackground(ServerConfiguration.getColour());

        contentPane.add(panel);
        noComponents++;
    }

    private JPanel initialisePanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        return panel;
    }

    private JPanel initialisePanel(String name)
    {
        JPanel panel = initialisePanel();
        panel.add(createLabel(name));

        return panel;
    }
}
