package util;

import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Log {
    private String text;
    private HashMap<String, String> actions = new HashMap<>();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Log() {
        text = "";
        actions.put("1", "Create");
        actions.put("2", "Read");
        actions.put("3", "Write");
        actions.put("4", "Delete");
    }

    public void newLogEntry(CurrentClient cc, String action, String record, Boolean allowed) {
        String result;

        action = actions.getOrDefault(action, "FAIL");
        if (action != "FAIL") {
            if (allowed) {
                result = "Allowed";
            } else {
                result = "Access Denied or no such record";
            }
            LocalDateTime time = LocalDateTime.now();
            String s = "Time: " + dtf.format(time) + ", Client: " + cc.getName() + ", Action: " + action + ", Record: " + record + ", Result: " + result;
            System.out.println(s);
            addEventToLog(s);
        }
    }

    private void addEventToLog(String s) {
        text = text + s + "\n";
    }
    public String getText(){
        return text;
    }
}
