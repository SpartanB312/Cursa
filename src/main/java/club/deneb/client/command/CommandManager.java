package club.deneb.client.command;

import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.ClassFinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by B_312 on 01/15/2021
 */
public class CommandManager {

    public static CommandManager INSTANCE;

    public List<Command> commands = new ArrayList<>();

    public String cmdPrefix = ".";

    public CommandManager() {
        INSTANCE = this;
        loadCommands();
    }

    private void loadCommands(){
        Set<Class> classList = ClassFinder.findClasses(Command.class.getPackage().getName(), Command.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(aClass -> {
            try {
                Command command = (Command) aClass.newInstance();
                commands.add(command);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Command " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    public void runCommands(String s) {
        String readString = s.trim().substring(cmdPrefix.length()).trim();
        boolean commandResolved = false;
        boolean hasArgs = readString.trim().contains(" ");
        String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
        String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

        for(Command command: commands) {
            if(command.getCommand().trim().equalsIgnoreCase(commandName.trim().toLowerCase())) {
                command.onCall(readString, args);
                commandResolved = true;
                break;
            }
        }
        if(!commandResolved){
            ChatUtil.sendNoSpamErrorMessage("Unknown command. try 'help' for a list of commands.");
        }
    }

    public void setCmdPrefix(String s){
        cmdPrefix = s;
    }

    public String getCmdPrefix(){
        return cmdPrefix;
    }
}
