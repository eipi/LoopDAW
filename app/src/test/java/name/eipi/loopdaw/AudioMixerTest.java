package name.eipi.loopdaw;

import org.junit.Test;


import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.AudioMixer;

/**
 * Created by eipi on 17/04/2017.
 */

public class AudioMixerTest {

    @Test
    public void testAudioMixer() throws Exception {
        Project project1 = new Project("AudioMixerTestProject1");
        Track track1 = Track.newInstance(project1);
        track1.setFilePath("C:/devel/LoopDAW/app/src/test/resources/Track1.aac");
        Track track2 = Track.newInstance(project1);
        track2.setFilePath("C:/devel/LoopDAW/app/src/test/resources/Track2.aac");
        project1.getClips().add(track1);
        project1.getClips().add(track2);

        AudioMixer audioMixer = new AudioMixer(project1);
        audioMixer.renderProject();

    }

}
