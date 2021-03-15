package mvc;

public class AppFactory {
  public Model makeModel();
	public View makeView(Model model);
	public String[] getEditCommands();
	public Command makeEditCommand(Model model, String type);
	public String getTitle();
	public String about();
	public String[] getHelp();
}
