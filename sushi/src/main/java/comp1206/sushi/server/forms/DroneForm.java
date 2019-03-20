package comp1206.sushi.server.forms;

import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class DroneForm extends EntryForm
{
    public DroneForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
    }

    @Override
    public void generateForm()
    {
        JSpinner spinnerSpeed = createSpinner("Drone Speed", 1, 1, 100, 1);
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry((Integer)spinnerSpeed.getValue()));
        resizeWindow();
    }

    private void submitEntry(Number speed)
    {
        getServer().addDrone(speed);
        getTableView().updateTable("Drones");
        this.dispose();
    }
}
