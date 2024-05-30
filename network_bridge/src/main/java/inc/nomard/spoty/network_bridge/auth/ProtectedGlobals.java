package inc.nomard.spoty.network_bridge.auth;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.User;
import lombok.extern.java.Log;

@Log
public class ProtectedGlobals {
    public static String authToken = "";
    public static boolean trial;
    public static boolean canTry;
    public static boolean newTenancy;
    public static boolean activeTenancy;
    public static String message;
    public static User user;
}
