package name.eipi.loopdaw.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by avd1 on 06/02/2017.
 */

public class NavigationUtils {

    public static void goToActivity(Context currentActivity,
                                    Class<? extends Activity> activity, Bundle bundle) {

        LoopDAWLogger logger = LoopDAWLogger.getInstance();


        try {
            Intent newActivity = new Intent(currentActivity, activity);
            if (bundle != null) {
                newActivity.putExtras(bundle);
            }
            currentActivity.startActivity(newActivity);
        } catch (Throwable t) {
            logger.msg(t.getMessage());
            logger.msg(t.toString());
        }

    }

}
