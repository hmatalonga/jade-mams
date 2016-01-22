package jadeproject;


/**
 * Created by user on 15-01-2016.
 */
public class Konsole {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void welcome(String name) {
        System.out.println(ANSI_GREEN + "Hello! " + name + " is in the room..." + ANSI_RESET);
    }

    public static void calendar(Calendar cal) {
        System.out.println("Calendar:\n" +  ANSI_BLUE + cal + ANSI_RESET);
    }

    public static void waitingForAction(String name) {
        System.out.println(ANSI_PURPLE + name + ": is waiting..." + ANSI_RESET);
    }

    public static void lookingForMeeting(String name, int meeting) {
        System.out.println(ANSI_GREEN + name + ": is looking for meeting on day " + meeting + ANSI_RESET);
    }

    public static void meetingRequestDone(String name) {
        System.out.println(ANSI_GREEN + name + ": request for meeting is done!" + ANSI_RESET);
    }

    public static void finishMeetingDetails(String name) {
        System.out.println(ANSI_GREEN + name + ": calendar updated!" + ANSI_RESET);
    }

    public static void receiveMeetingRequest(String name) {
        System.out.println(ANSI_CYAN + name + ": I have a meeting request..." + ANSI_RESET);
    }

    public static void handleMeeting(String name) {
        System.out.println(ANSI_CYAN + name + ": I have a meeting to handle!" + ANSI_RESET);
    }

    public static void askingTimeSlotPreference(String name, int timeslot) {
        System.out.println(ANSI_GREEN + name + ": is asking for preference of timeslot(" + timeslot +")" + ANSI_RESET);
    }

    public static void givingTimeSlotPreference(String name, int day, int timeslot, double preference) {
        System.out.println(ANSI_GREEN + name + ": day " + day + " - timeslot(" + timeslot + ") = "
                + preference + ANSI_RESET);
    }

    public static void settingMeetingDetails(String name, int day, int timeslot, double preference) {
        System.out.println(ANSI_YELLOW + name + ": Meeting day " + day + " - timeslot(" + timeslot + ") = "
                + preference + ANSI_RESET);
    }

    public static void noMeeting(String name) {
        System.out.println(ANSI_YELLOW + name + ": No participants available for this meeting..." + ANSI_RESET);
    }

    public static void terminalSplit(int num) {
        System.out.print(ANSI_RED);
        for (int i = 0; i < num; i++)
            System.out.print("-");
        System.out.println(ANSI_RESET);
    }
}
