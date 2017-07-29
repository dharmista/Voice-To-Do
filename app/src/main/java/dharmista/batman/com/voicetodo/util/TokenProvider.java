package dharmista.batman.com.voicetodo.util;

import java.util.UUID;

/**
 * Created by Dharmista on 7/20/2017.
 */

public class TokenProvider {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
