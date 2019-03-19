package comp1206.sushi.server.forms;

import comp1206.sushi.common.Model;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.ServerWindow;

import javax.swing.*;
import java.awt.*;

public abstract class EntryForm extends JFrame
{
    private ServerInterface server;
    private String formName;

    public EntryForm(ServerInterface server, String formName)
    {
        super("Sushi Server");
        this.setTitle(server.getRestaurantName() + " Server - " + formName);
        this.server = server;
        this.formName = formName;

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(ServerWindow.getImg());

        generateForm();

        setVisible(true);
    }

    public abstract void generateForm();

    protected JPanel createTextField(String name)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel(name + ":"));
        panel.add(new JTextField());

        return panel;
    }

    protected JPanel createSpinner(String name)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel(name + ":"));
        panel.add(new JSpinner());

        return panel;
    }

    protected JPanel createComboBox(String name, Class<? extends Model> type)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel(name + ":"));
        panel.add(new JComboBox<>());

        return panel;
    }

    protected JPanel createRecipeComponent()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // TODO: Implement this.

        return panel;
    }
}
