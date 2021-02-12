package util;

public class Log {
    private String text;

    public Log() {
        text = "";
    }

    public void addEventToLog(String s) {
        text = text + s + "\n";
    }
}
