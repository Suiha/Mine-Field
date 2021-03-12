package mvc;

import java.awt.*;
import java.beans.*;
import javax.swing.JPanel;

public class View extends JPanel implements PropertyChangeListener
{
	protected Model model;
	
	public View(Model m)
	{
		    this.model = m;
	}
	
	public void setModel(Model m)
	{
		    this.model.removePropertyChangeListener(this);
        this.model = m;
        this.model.initSupport();
        this.model.addPropertyChangeListener(this);
        repaint();
	}
	
	public void paintComponent(Graphics gc)
	{
		    super.paintComponent(gc);
	}
	
	@Override
    public void propertyChange(PropertyChangeEvent arg0) {
    	  repaint();
    }
}
