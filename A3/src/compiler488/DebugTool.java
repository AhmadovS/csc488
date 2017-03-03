package compiler488;

/**
 * Created by Amir on 3/3/2017.
 */
public class DebugTool {

    public static boolean DEBUG = true;

    public static void print(String s) {
        if (DEBUG) {
            System.out.println(s);
        }
    }
    
}
