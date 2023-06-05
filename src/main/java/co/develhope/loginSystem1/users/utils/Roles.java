package co.develhope.loginSystem1.users.utils;

import co.develhope.loginSystem1.users.entities.User;

public class Roles {

    public final static String OWNER = "OWNER";
    public final static String SUPER_ADMIN = "SUPER_ADMIN";
    public final static String ADMIN = "ADMIN";
    public final static String REGISTERED = "REGISTERED";



    public static boolean hasRole(User user, String roleInput){
        return user.getRoles().stream().filter(role -> role.getName().equals(roleInput)).count() != 0;
    }
}
