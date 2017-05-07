package name.eipi.loopdaw.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    if (file.getName().endsWith(".aac")) {
                        Track.reInstance(loadedProject, file.getName());
                    }
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        for (Project project : projectList) {
//            String objectString = new Gson().toJson(project);
//            File file = new File(project.getBaseFilePath() + "project.info");
//            file.getParentFile().mkdirs();
//            try {
//                FileWriter fos = new FileWriter(file);
//                fos.write(objectString);
//                fos.close();
//            } catch (Exception ex) {
//                LoopDAWLogger.getInstance().msg(ex.getMessage());
//                LoopDAWLogger.getInstance().msg(objectString);
//
//            }
//        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public List<Project> getFavourites() {
        List<Project> favs = new ArrayList<>();
        for (Project p : projectList) {
            if (p.isFavourite()) {
                favs.add(p);
            }
        }
        return favs;
    }

    public List<Project> refreshFavs(final List<Project> favsIn) {
        for (Project p : projectList) {
            if (p.isFavourite()) {
                if (!favsIn.contains(p)) {
                    favsIn.add(p);
                }
            } else if (favsIn.contains(p)) {
                favsIn.remove(p);
            }
        }
        return favsIn;
    }

    public List<Project> getAllProjects() {
        List<Project> all = new ArrayList<>();
        all.addAll(projectList);
        return all;
    }

    public void deleteProject(final Project project) {
        File folder = new File(project.getBaseFilePath());
        File[] files = folder.listFiles();
        for (File file : files) {
            file.delete();
        }
        folder.delete();
    }

}
