package contentclasses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 16.03.15.
 */
public class Event {
    private int categoryId;
    private int eventid;
    private long startTime;
    String eventName;
    String command1, command2;
    String eventStates;
    int maxbet, minbet;
    int totalMax, totalMin;
    ArrayList<String> eventForas = new ArrayList<>();
    ArrayList<String> eventTotals = new ArrayList<>();
    ArrayList<String> foras1command = new ArrayList<>();
    ArrayList<String> foras2command = new ArrayList<>();
    ArrayList<String> totals1command = new ArrayList<>();
    ArrayList<String> totals2command = new ArrayList<>();
    String href;
    HashMap<String, Integer> totals = new HashMap<>();

    public Event() {

    }

    public Event(int id, String url) {
        this.eventid = id;
        this.href = url;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCommand1() {
        return command1;
    }

    public String getCommand2() {
        return command2;
    }

    public ArrayList<String> addForas1command() {
        return foras1command;
    }

    public void addForas1command(String fora) {
        this.foras1command.add(fora);
    }

    public ArrayList<String> getForas2command() {
        return foras2command;
    }

    public void addForas2command(String fora) {
        this.foras2command.add(fora);
    }

    public ArrayList<String> getTotals1command() {
        return totals1command;
    }

    public void addTotals1command(String total) {
        this.totals1command.add(total);
    }

    public ArrayList<String> getTotals2command() {
        return totals2command;
    }

    public void addTotals2command(String total) {
        this.totals2command.add(total);
    }

    public String getEventStates() {
        return eventStates;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getEventid() {
        return eventid;
    }

    public String getHref() {
        return href;
    }

    public void setCommand1(String command1) {
        this.command1 = command1;
    }

    public void setCommand2(String command2) {
        this.command2 = command2;
    }

    public void setEventName(String eventEventName) {
        this.eventName = eventEventName;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public void setEventStates(String eventStates) {
        this.eventStates = eventStates;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setMaxbet(int maxbet) {
        this.maxbet = maxbet;
    }

    public void setMinbet(int minbet) {
        this.minbet = minbet;
    }

    public void setTotalMax(int totalMax) {
        this.totalMax = totalMax;
    }

    public void setTotalMin(int totalMin) {
        this.totalMin = totalMin;
    }

    public void setTotals(HashMap<String, Integer> totals) {
        this.totals = totals;
    }

    public ArrayList<String> getEventForas() {
        return eventForas;
    }

    public void addEventForas(String eventFora) {
        this.eventForas.add(eventFora);
    }

    public ArrayList<String> getEventTotals() {
        return eventTotals;
    }

    public void addEventTotals(String eventTotal) {
        this.eventTotals.add(eventTotal);
    }


    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", command1='" + command1 + '\'' +
                ", command2='" + command2 + '\'' +
                ", eventStates='" + eventStates + '\'' +
                ", eventForas=" + eventForas +
                ", eventTotals=" + eventTotals +
                ", foras1command=" + foras1command +
                ", foras2command=" + foras2command +
                ", totals1command=" + totals1command +
                ", totals2command=" + totals2command +
                '}';
    }
}
