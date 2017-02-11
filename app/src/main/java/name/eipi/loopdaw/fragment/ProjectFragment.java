package name.eipi.loopdaw.fragment;

import android.app.ListFragment;

/**
 * Created by avd1 on 07/02/2017.
 */

public class ProjectFragment extends ListFragment {

    private static final ProjectFragment INSTANCE;

    static {
        INSTANCE = new ProjectFragment();
    }

    public ProjectFragment getInstance() {
        return INSTANCE;
    }



}
