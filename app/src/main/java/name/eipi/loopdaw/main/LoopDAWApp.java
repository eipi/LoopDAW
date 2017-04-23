package name.eipi.loopdaw.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
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
            initialize();
        } catch (IOException  ex) {
            ex.printStackTrace();
        }

    }

    public void initialize() throws IOException {
        String projectsPath = getExternalCacheDir().getAbsolutePath() + File.separator + "projects" + File.separator;
        File projectsDir = new File(projectsPath);
        if (projectsDir != null && projectsDir.exists()) {
            File[] projects = projectsDir.listFiles();
            for (File project : projects) {
                Project loadedProject = Project.create(projectsPath, project);
                projectList.add(loadedProject);
                for (File file : project.listFiles()) {
                    loadedProject.getClips().add(Track.reInstance(loadedProject, file.getName()));
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Project project : projectList) {
            String objectString = new Gson().toJson(project);
            System.out.println(objectString);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
