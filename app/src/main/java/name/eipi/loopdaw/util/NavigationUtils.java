package name.eipi.loopdaw.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by avd1 on 06/02/2017.
 */

public class NavigationUtils {

    public static void goToActivity(Activity currentActivity,
                                    Class<? extends Activity> activity, Bundle bundle) {
        Intent newActivity = new Intent(currentActivity, activity);
        if (bundle != null) {
            newActivity.putExtras(bundle);
        }
        currentActivity.startActivity(newActivity);
    }

}
