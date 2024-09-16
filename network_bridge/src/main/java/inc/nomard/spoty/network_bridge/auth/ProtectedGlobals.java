package inc.nomard.spoty.network_bridge.auth;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import lombok.extern.java.*;

@Log
public class ProtectedGlobals {
    public static String authToken = "";
    public static boolean trial;
    public static boolean canTry;
    public static boolean newTenancy;
    public static boolean activeTenancy;
    public static String message;
    public static Employee user;
    public static Role role;
    public static double loginSceneWidth;
    public static double loginSceneHeight;
}
