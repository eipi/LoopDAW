package name.eipi.loopdaw.model;

import java.io.File;

import name.eipi.loopdaw.fragment.CustomWaveformFragment;

/**
 * Created by Damien on 26/02/2017.
 */

public class Track {

    private Track() {
        // no impl
    }

    private int id;

    private String filePath;

    private int startTime;
    private int endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;

    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public CustomWaveformFragment getWaveform() {
        CustomWaveformFragment customWaveformFragment = new CustomWaveformFragment();
        customWaveformFragment.link(this);
        return customWaveformFragment;
    }

    public static Track newInstance(Project project) {
        Track t = new Track();
        t.setId(project.getClips().size());
        t.setFilePath(project.getBaseFilePath() + "Track" + t.getId() + ".3gp");
        return t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (id != track.id) return false;
        return filePath != null ? filePath.equals(track.filePath) : track.filePath == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        return result;
    }
}