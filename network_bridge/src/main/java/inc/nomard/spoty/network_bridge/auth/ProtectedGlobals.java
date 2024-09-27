package inc.nomard.spoty.network_bridge.auth;

import inc.nomard.spoty.network_bridge.dtos.Role;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.extern.java.Log;

@Log
public class ProtectedGlobals {
    public static String authToken = "";
    public static String message;
    public static Employee user;
    public static Role role;
    public static double loginSceneWidth;
    public static double loginSceneHeight;
}
