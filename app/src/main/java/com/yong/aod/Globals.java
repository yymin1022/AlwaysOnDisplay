package com.yong.aod;
import java.util.*;
import java.util.concurrent.*;

public class Globals {
    public static boolean isShown;
    public static boolean sensorIsScreenOff;
    public static boolean inCall = false;
    public static boolean noLock;
    public static boolean killedByDelay;
    public static boolean isServiceRunning;
    public static boolean waitingForApp = false;
    public static Map<String, NotificationListener.NotificationHolder> notifications = new ConcurrentHashMap<>();
    public static ArrayList<String> ownedItems;

    public static String newNotification() {
        if (Globals.notifications != null)
            for (Map.Entry<String, NotificationListener.NotificationHolder> entry : Globals.notifications.entrySet()) {
                if (entry != null)
                    if (entry.getValue() != null)
                        if (entry.getValue().getNew())
                            if (entry.getKey() != null)
                                return entry.getKey();
            }
        return null;
    }
}
