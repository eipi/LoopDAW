package name.eipi.loopdaw.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

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
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
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
        Project project = new Project(projectFile.getName());
        project.setBaseFilePath(baseProjectsPath + projectFile.getName() + File.separator);
        return project;
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
}
