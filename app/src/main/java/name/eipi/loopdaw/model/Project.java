package name.eipi.loopdaw.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

import name.eipi.loopdaw.fragment.CardContentFragment;
import name.eipi.loopdaw.fragment.FavsCardContentFragment;
import name.eipi.loopdaw.util.LoopDAWLogger;

/**
 * Created by avd1 on 07/02/2017.
 */

public class Project implements Serializable, Comparable<Project> {

    private transient String baseFilePath;
    private String id;
    private String name;

    private String description;

    private boolean favourite;
    private boolean published;

    private ArrayList<Track> clips;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
        FavsCardContentFragment.adapter.notifyDataSetChanged();
        CardContentFragment.adapter.notifyDataSetChanged();
        save();
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
        FavsCardContentFragment.refreshFavs();
        CardContentFragment.adapter.notifyDataSetChanged();
        save();
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Project(final String name) {
        this.name = name;
        clips = new ArrayList<>();
    }

    public static Project create(String baseProjectsPath, File projectFile) {
        File file = new File(baseProjectsPath + projectFile.getName() + File.separator + "project.info");
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while (buffer.ready()) {
                sb.append(buffer.readLine());
            }
            buffer.close();
            reader.close();

            Project project = new Gson().fromJson(sb.toString(), Project.class);
                    //new Project(projectFile.getName());
            project.setBaseFilePath(baseProjectsPath + projectFile.getName() + File.separator);
            return project;
        } catch (Exception ex) {
            LoopDAWLogger.getInstance().msg(ex.getMessage());
            Project project = new Project(projectFile.getName());
            project.setBaseFilePath(baseProjectsPath + projectFile.getName() + File.separator);
            return project;
        }
    }

    public ArrayList<Track> getClips() {
        return clips;
    }

    public void setClips(ArrayList<Track> clips) {
        this.clips = clips;
    }

    public String getBaseFilePath() {
        return baseFilePath;
    }

    public void setBaseFilePath(String baseFilePath) {
        this.baseFilePath = baseFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return name.equals(project.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Project project) {
        if (this.isFavourite() == project.isFavourite()) {
            return this.getName().compareTo(project.getName());
        } else if (this.isFavourite()) {
            return 1;
        } else {
            return -1;
        }

    }

    public void save() {
        String objectString = new Gson().toJson(this);
        File file = new File(this.getBaseFilePath() + "project.info");
        file.getParentFile().mkdirs();
        try {
            FileWriter fos = new FileWriter(file);
            fos.write(objectString);
            fos.close();
        } catch (Exception ex) {
            LoopDAWLogger.getInstance().msg(ex.getMessage());
            LoopDAWLogger.getInstance().msg(objectString);

        }
    }
}
