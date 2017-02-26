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

    private Integer startTime;
    private Integer endTime;

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
        t.setFilePath(project.getBaseFilePath() + File.separator + "Track" + t.getId() + ".3gp");
        t.setId(project.getClips().size());
        return t;
    }

}
