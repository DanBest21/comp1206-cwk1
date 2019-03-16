package comp1206.sushi.server;

import java.awt.*;

// TODO: Figure out if this class is salvagable.
public class GridBagConstraintsBuilder
{
    private GridBagConstraints constraints = new GridBagConstraints();

    public GridBagConstraintsBuilder setPosition(int x, int y)
    {
        constraints.gridx = x;
        constraints.gridy = y;
        return this;
    }

    public GridBagConstraintsBuilder setCellLength(int length)
    {
        constraints.gridwidth = length;
        return this;
    }

    public GridBagConstraintsBuilder setBorder(int top, int left, int bottom, int right)
    {
        constraints.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GridBagConstraints build()
    {
        GridBagConstraints constraints = this.constraints;
        this.constraints = new GridBagConstraints();
        return constraints;
    }
}