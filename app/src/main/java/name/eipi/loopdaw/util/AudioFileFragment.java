package name.eipi.loopdaw.util;

import java.io.FileInputStream;

import name.eipi.loopdaw.model.Track;

/**
 * Created by eipi on 22/04/2017.
 */

public class AudioFileFragment {

    private byte[] header;

    private byte[] crc;

    private FileInputStream fileInputStream;

    private byte[] dataBuffer;

    private Track sourceTrack;


    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getCrc() {
        return crc;
    }

    public void setCrc(byte[] crc) {
        this.crc = crc;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public byte[] getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(byte[] dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    public Track getSourceTrack() {
        return sourceTrack;
    }

    public void setSourceTrack(Track sourceTrack) {
        this.sourceTrack = sourceTrack;
    }
}
