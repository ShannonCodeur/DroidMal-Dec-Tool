package javax.microedition.lcdui;

public class Command {
    public static final int BACK = 2;
    public static final int CANCEL = 3;
    public static final int EXIT = 7;
    public static final int HELP = 5;
    public static final int ITEM = 8;
    public static final int OK = 4;
    public static final int SCREEN = 1;
    public static final int STOP = 6;
    int commandType;
    String inputText;
    Command ui;

    public Command(String string, int ok2, int i) {
        this.commandType = ok2;
        this.inputText = string;
    }

    public String getInputText() {
        return this.inputText;
    }

    public int getCommandType() {
        return this.commandType;
    }

    public String getLabel() {
        return null;
    }

    public int getPriority() {
        return 0;
    }
}
