package name.eipi.loopdaw.model;

import java.io.File;
import java.io.Serializable;

import name.eipi.loopdaw.fragment.CustomWaveformFragment;
import name.eipi.loopdaw.fragment.TrackFragment;

/**
 * Created by Damien on 26/02/2017.
 */

public class Track implements Serializable {

    private Track() {
        // no impl
    }

    private int id;

    private String name;
    private String description;

    private transient String filePath;

    private int startTime;
    private int endTime;

    private boolean mute;

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
//        TrackFragment.getInstance().notifyDataChanged();
    }

    private int numLoops = Integer.MAX_VALUE;

    public int getNumLoops() {
        return numLoops;
    }

    public void setNumLoops(int numLoops) {
        this.numLoops = numLoops;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
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
        //t.setId(System.currentTimeMillis());
        t.setFilePath(project.getBaseFilePath() + t.getId() + ".aac");
        return t;
    }

    public static void reInstance(Project project, String trackName) {
        trackName = trackName.replaceFirst("Track", "");
        String[] split = trackName.split("\\.");
        String id = split[0];
        Integer trackId = Integer.parseInt(id);
        boolean found = false;
        for (Track track : project.getClips()) {
            if (track.getId() == trackId) {
                track.setFilePath(project.getBaseFilePath() + trackName);
                found = true;
            }
        }
        if (!found) {
            Track t = new Track();
            t.setId(trackId);
            t.setFilePath(project.getBaseFilePath() + trackName);
            project.getClips().add(t);
        }
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
