package mvc;

import tools.*;

public abstract class Model extends Bean
{
	private String fileName;
	private boolean unsavedChanges;
	
	public Model()
	{
		fileName = null;
		unsavedChanges = false;
	}
	
	public void setFileName(String name) { fileName = name; }
	public String getFileName() { return fileName; }
	
	public void setUnsavedChanges(boolean b) { unsavedChanges = b; }
	public boolean getUnsavedChanges() { return unsavedChanges; }
	
	public void changed()
	{
		boolean old = unsavedChanges;
		this.setUnsavedChanges(true);
		firePropertyChange("unsavedChanges", old, unsavedChanges);
	}
}
