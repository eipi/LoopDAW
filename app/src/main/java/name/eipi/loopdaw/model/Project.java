package name.eipi.loopdaw.model;

import java.util.Collection;

import name.eipi.loopdaw.fragment.CustomWaveformFragment;

/**
 * Created by avd1 on 07/02/2017.
 */

public class Project {

    private String name;
    private int id;
    private boolean favourite;
    private boolean published;
    private Collection<CustomWaveformFragment> clips;

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
    }

    public Collection<CustomWaveformFragment> getClips() {
        return clips;
    }

    public void setClips(Collection<CustomWaveformFragment> clips) {
        this.clips = clips;
    }

}
