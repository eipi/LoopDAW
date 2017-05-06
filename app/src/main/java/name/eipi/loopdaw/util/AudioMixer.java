package name.eipi.loopdaw.util;

import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.Frame;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;

/**
 * Created by eipi on 17/04/2017.
 */

public class AudioMixer {

    final Project project;

    public AudioMixer(Project projectIn) {
        this.project = projectIn;
    }


    public void renderProject() {
        try {

            Collection<Byte[]> byteArrayList = new ArrayList<>();
            for (Track track : project.getClips()) {
                //MediaExtractor extractor = new MediaExtractor();
                FileInputStream inputStream = new FileInputStream(track.getFilePath());
//                byte[] data = new byte[1024];
//                while (inputStream.read(data) > 0) {
//                    for (byte b : data) {
//                        System.out.print(b + " ");
//                    }
//                }

                MP4Container container = new MP4Container(inputStream);
                Collection<net.sourceforge.jaad.mp4.api.Track> tracks = container.getMovie().getTracks();
                net.sourceforge.jaad.mp4.api.Track track1 = container.getMovie().getTracks().iterator().next();
                byte[] decoderSpecificinfo = track1.getDecoderSpecificInfo();
                Decoder dec = new Decoder(decoderSpecificinfo);
                SampleBuffer buf = new SampleBuffer();
                while (track1.hasMoreFrames()) {
                    Frame aacFrame = track1.readNextFrame();
                    dec.decodeFrame(aacFrame.getData(), buf);
                    byte[] audio = buf.getData();
                    for (byte b : audio) {
                        System.out.print(b);
                    }
                    System.out.println();
                }
                //the aacFrame array contains the AAC frame to decode
                byte[] audio = buf.getData(); //this array contains the raw PCM audio data
/*
                byte[] header = new byte[7];
                inputStream.read(header);
                for (byte b : header) {
                    System.out.println(b);
                }

                // TODO - Check CRC bit
                boolean crcBit = header[2] % 2 == 0;
                if (crcBit) {
                    byte[] crc = new byte[2];
                    inputStream.read(crc);
                    for (byte b : crc) {
                        System.out.println(b);
                    }
                }

                byte[] data = new byte[1024];
                int i = 0;
                while (inputStream.read(data) > 0) {
                    for (byte b : data) {
                        i++;
                        System.out.print(b + " ");
                        if (i % 60 == 0) {
                            i = 0;
                            System.out.println("");
                        }
                    }
                }
                */

//                FileDescriptor fileDescriptor = inputStream.getFD();
//                extractor.setDataSource(fileDescriptor);
//                int numTracks = extractor.getTrackCount();
//                for (int i = 0; i < numTracks; ++i) {
//                    MediaFormat format = extractor.getTrackFormat(i);
//                    String mime = format.getString(MediaFormat.KEY_MIME);
//                    if (true) {
//                        extractor.selectTrack(i);
//                    }
//                }
//                ByteBuffer inputBuffer = ByteBuffer.allocate(4096);
//                while (extractor.readSampleData(inputBuffer, 4096) >=0){
//                    int trackIndex = extractor.getSampleTrackIndex();
//                    long presentationTimeUs = extractor.getSampleTime();
//                    // ...
//                    extractor.advance();
//                }
//
//                extractor.release();
//                extractor = null;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    private Long mixSamples(Long... samples) {
        long total = 0;
        for (Long l : samples) {
            total += l / samples.length;
        }
        return total;
    }


}
