package comp1206.sushi.server.forms;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.ParseException;

public class PostcodeForm extends EntryForm
{
    public PostcodeForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
        generateForm();
        setVisible(true);
    }

    @Override
    public void generateForm()
    {
        JFormattedTextField textPostcode = createPostcodeFormattedTextField();
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textPostcode.getText()));
        resizeWindow();
    }

    private void submitEntry(String postcode)
    {
        if (!validateInput(postcode))
            return;

        getServer().addPostcode(postcode);
        getTableView().updateTable("Postcodes");
        this.dispose();
    }

    private JFormattedTextField createPostcodeFormattedTextField()
    {
        createLabel("Postcode");

        JFormattedTextField formattedTextField = new JFormattedTextField();

        try
        {
            MaskFormatter formatter = new MaskFormatter("UU## #UU");
            formatter.install(formattedTextField);
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        formattedTextField.setFont(ServerConfiguration.getFont());
        formattedTextField.setColumns(22);
        formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);

        setGridBagConstraints(formattedTextField,1, getNoRows(), 1, GridBagConstraints.EAST);
        addToContentPane(formattedTextField);

        return formattedTextField;
    }

    private boolean validateInput(String postcode)
    {
        if (postcode.equals(""))
        {
            throwMissingFieldsInputError();
            return false;
        }

        for (Postcode postcodeObject : getServer().getPostcodes())
        {
            if (postcodeObject.getName().trim().equals(postcode.trim()))
            {
                throwDuplicateUniqueFieldError("Postcode", postcode);
                return false;
            }
        }

        return true;
    }
}

