package comp1206.sushi.server.forms;

import comp1206.sushi.common.Model;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.configuration.ServerConfiguration;

import javax.swing.*;
import java.awt.*;

public abstract class EntryForm extends JFrame
{
    private ServerInterface server;
    private String formName;
    private JPanel contentPanel = new JPanel();
    private int noComponents = 0;
    private static final int COMPONENT_HEIGHT = 70;

    public EntryForm(ServerInterface server, String formName)
    {
        super("Sushi Server");
        this.setTitle(server.getRestaurantName() + " Server - " + formName);
        this.server = server;
        this.formName = formName;

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(ServerConfiguration.getImg());

        setContentPane(contentPanel);
        contentPanel.setBackground(ServerConfiguration.getColour());

        generateForm();

        setVisible(true);
    }

    public abstract void generateForm();

    protected JPanel createTextField(String name)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = formatLabel(new JLabel(name + ":"));
        panel.add(label);

        JTextField textField = new JTextField(25);
        textField.setFont(ServerConfiguration.getFont());
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(textField);

        panel.setBackground(ServerConfiguration.getColour());

        contentPanel.add(panel);
        noComponents++;

        return panel;
    }

    protected JPanel createSpinner(String name)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel(name + ":"));
        panel.add(new JSpinner());

        contentPanel.add(panel);
        noComponents++;

        return panel;
    }

    protected JPanel createComboBox(String name, Class<? extends Model> type)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel(name + ":"));
        panel.add(new JComboBox<>());

        contentPanel.add(panel);
        noComponents++;

        return panel;
    }

    protected JPanel createRecipeComponent()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // TODO: Implement this.

        return panel;
    }

    protected JButton createSubmitButton()
    {
        JButton btn = new JButton("Submit " + formName.split(" ")[1]);
        btn.setFont(ServerConfiguration.getSmallTitleFont());

        contentPanel.add(btn);

        noComponents++;

        return btn;
    }

    protected void resizeWindow()
    {
        setSize(400, noComponents * COMPONENT_HEIGHT);
    }

    private JLabel formatLabel(JLabel label)
    {
        label.setFont(ServerConfiguration.getSmallTitleFont());
        label.setForeground(Color.WHITE);

        return label;
    }
}
