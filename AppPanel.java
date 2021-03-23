package mvc;

import tools.Utilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class AppPanel extends JPanel implements ActionListener 
{
	protected Model model;
    protected View view;
    protected JPanel controlPanel;
    protected AppFactory factory;
    private JFrame frame;
    public static int FRAME_WIDTH = 500;
    public static int FRAME_HEIGHT = 300;

    public AppPanel(AppFactory factory) 
    {
    	this.factory = factory;
    	model = factory.makeModel();
    	controlPanel = new JPanel();
    	view = factory.makeView(model);
    	setLayout((new GridLayout(1, 2)));
    	add(controlPanel);
    	add(view);

    	controlPanel.setBackground(Color.PINK);
    	view.setBackground(Color.GRAY);

    	frame = new JFrame();
    	Container cp = frame.getContentPane();
    	cp.add(this);
    	frame.setJMenuBar(createMenuBar());
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setTitle(factory.getTitle());
    	frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

	protected JMenuBar createMenuBar() 
	{
	    JMenuBar result = new JMenuBar();
	    // add file, edit, and help menus
	    JMenu fileMenu =
	         Utilities.makeMenu("File", new String[] {"New",  "Save", "SaveAs", "Open", "Quit"}, this);
	    result.add(fileMenu);
	
	    JMenu editMenu =
	           Utilities.makeMenu("Edit", factory.getEditCommands(), this);
	    result.add(editMenu);
	
	    JMenu helpMenu =
	           Utilities.makeMenu("Help", new String[] {"About", "Help"}, this);
	    result.add(helpMenu);
	
	    return result;
	}

	public void display() { frame.setVisible(true); }

	public void actionPerformed(ActionEvent ae) 
	{
		try 
		{
			String cmmd = ae.getActionCommand();
			if (cmmd == "Save") {
				//	String fName = Utilities.ask("File Name?");
				String fName = Utilities.getFileName(null, false);
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fName));
				os.writeObject(model);
				os.close();
				model.setUnsavedChanges(false);
			} else if (cmmd == "SaveAs") {
				String fName = Utilities.getFileName(null, false);
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fName));
				os.writeObject(model);
				os.close();
				model.setUnsavedChanges(false);
			} else if (cmmd == "Open") {
				if (!model.getUnsavedChanges())
				{
					String fName = Utilities.getFileName(null, true);
					ObjectInputStream is = new ObjectInputStream(new FileInputStream(fName));
					model = (Model)is.readObject();
					view.setModel(model);
					is.close();
				} else {
					boolean save = Utilities.confirm("There are unsaved changes. Do you want to save?");
					if (save)
					{
						String fName = Utilities.getFileName(null, true);
						ObjectInputStream is = new ObjectInputStream(new FileInputStream(fName));
						model = (Model)is.readObject();
						view.setModel(model);
					} else {
						String fName = Utilities.getFileName(null, false);
						ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fName));
						os.writeObject(model);
						os.close();
						model.setUnsavedChanges(false);
					}
				}
			} else if (cmmd == "New") {
				if (!model.getUnsavedChanges())
				{
					model = factory.makeModel();
					view.setModel(model);
				} else {
					boolean save = Utilities.confirm("There are unsaved changes. Do you want to save?");
					if (save)
					{
						model = factory.makeModel();
						view.setModel(model);
					} else {
						String fName = Utilities.getFileName(null, false);
						ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fName));
						os.writeObject(model);
						os.close();
						model.setUnsavedChanges(false);
					}
				}
			} else if (cmmd == "Quit") {
				if (!model.getUnsavedChanges())
				{
					System.exit(1);
				} else {
					boolean save = Utilities.confirm("There are unsaved changes. Do you want to save?");
					if (save)
					{
						System.exit(1);
					} else {
						String fName = Utilities.getFileName(null, false);
						ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fName));
						os.writeObject(model);
						os.close();
						model.setUnsavedChanges(false);
					}
				}
				
			} else if (cmmd == "About") {
				Utilities.inform(factory.about());
			} else if (cmmd == "Help") {
				Utilities.inform(factory.getHelp());
			} else {
				Command c = factory.makeEditCommand(model, cmmd);
				c.execute();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	protected void handleException(Exception e) 
	{
		Utilities.error(e);
	}
}
