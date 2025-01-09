package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 *
 * @author vjm
 */
public class Main {

    /**
     * Processes user's commands on a phone directory.
     *
     * @param fn The file containing the phone directory.
     * @param ui The UserInterface object to use
     *           to talk to the user.
     * @param pd The PhoneDirectory object to use
     *           to process the phone directory.
     */
    public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
        pd.loadData(fn);
        boolean changed = false;

        String[] commands = {"Add/Change Entry", "Look Up Entry", "Remove Entry", "Save Directory", "Exit"};

        String name, number, oldNumber;

        while (true) {
            int c = ui.getCommand(commands);
            switch (c) {
                case -1:
                    ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
                    break;
                case 0:
                    name = ui.getInfo("Enter the name ");
                    if(name == null){break;}
                    while (name.isEmpty()){
                        name = ui.getInfo("Re-enter the name ");
                        if(name == null){break;}
                    }
                    number = ui.getInfo("Enter the number ");
                    if(number == null){break;}
                    oldNumber = pd.addOrChangeEntry(name, number);
                    if (oldNumber == null)
                        ui.sendMessage(name + " was added to the directory with number " + number);
                    else if (oldNumber.isEmpty())
                        ui.sendMessage(name + "'s number " + number + " was added to the directory.");
                    else
                        ui.sendMessage(name + " has changed number from " + oldNumber + " to " + number);
                    changed = true;
                    break;
                case 1:
                    name = ui.getInfo("Enter the name ");
                    if(name == null){break;}
                    while (name.isEmpty()){
                        name = ui.getInfo("Re-enter the name ");
                        if(name == null){break;}
                    }
                    number = pd.lookupEntry(name);
                    if (number == null)
                        ui.sendMessage(name + " is not listed in the directory.");
                    else if (number.isEmpty()) {
                        ui.sendMessage(name + " has no number ");
                    }
                    else
                        ui.sendMessage(name + " has number " + number);
                    break;
                case 2:
                    name = ui.getInfo("Enter the name ");
                    if (name == null){break;}
                    while (name.isEmpty()){
                        name = ui.getInfo("Re-enter the name ");
                        if (name == null){break;}
                    }
                    oldNumber = pd.removeEntry(name);
                    ui.sendMessage(name + "with number" + oldNumber + " has been removed.");
                    changed = true;
                    break;
                case 3:
                    pd.save();
                    ui.sendMessage("Directory saved.");
                    changed = false;
                    break;
                case 4:
                    if (changed) {
                        ui.sendMessage("Do you want to exit without saving?");
                        String[] saveChanges = {"Yes", "No"};
                        int save = ui.getCommand(saveChanges);
                        if (save == 0) {
                            ui.sendMessage("Directory not saved.");
                            return;
                        }

                    }
                    return;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fn = "csc220.txt";
        // PhoneDirectory pd = new ArrayBasedPD();
        PhoneDirectory pd = new SortedPD();
        //UserInterface ui = new ConsoleUI();

        UserInterface ui = new GUI("Phone Directory");
        //UserInterface ui = new TestUI("Phone Directory");
        processCommands(fn, ui, pd);
    }
}
