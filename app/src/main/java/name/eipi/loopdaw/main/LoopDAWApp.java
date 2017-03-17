package name.eipi.loopdaw.main;

import android.app.Application;

import java.io.IOException;
import java.util.ArrayList;

import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.util.LoopDAWLogger;

/**
 * Created by Damien on 25/02/2017.
 */

public class LoopDAWApp extends Application {

    private static final long creDtm;

    static {
        creDtm = System.currentTimeMillis();
    }

    public ArrayList<Project> projectList = new ArrayList<Project>();

    public long getCreDtm(){
        return creDtm;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            LoopDAWLogger.getInstance().initialize(this);
        } catch (IOException  ex) {
            ex.printStackTrace();
        }

    }
}
