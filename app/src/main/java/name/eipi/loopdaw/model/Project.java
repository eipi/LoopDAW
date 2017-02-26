package name.eipi.loopdaw.model;

import java.util.ArrayList;

/**
 * Created by avd1 on 07/02/2017.
 */

public class Project {

    private String baseFilePath;
    private String name;
    private int id;
    private boolean favourite;
    private boolean published;
    private ArrayList<Track> clips;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
