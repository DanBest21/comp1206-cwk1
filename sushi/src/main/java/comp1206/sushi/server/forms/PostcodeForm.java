package comp1206.sushi.server.forms;

import comp1206.sushi.server.ServerInterface;

public class PostcodeForm extends EntryForm
{
    public PostcodeForm(ServerInterface server, String formName)
    {
        super(server, formName);
    }

    @Override
    public void generateForm()
    {
        createTextField("Postcode");
        createSubmitButton();
        resizeWindow();
    }
}

