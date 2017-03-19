package name.eipi.loopdaw.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import name.eipi.loopdaw.main.LoopDAWApp;

/**
 * Created by avd1 on 26/02/2017.
 */

public class LoopDAWLogger {

    private final static LoopDAWLogger INSTANCE;
    private String logFilePath;

    private LoopDAWLogger() {
        //no impl.
    }

    static {
        INSTANCE = new LoopDAWLogger();
    }

    public static LoopDAWLogger getInstance() {
        return INSTANCE;
    }

    public void initialize(LoopDAWApp loopDAWApp) throws IOException {
        INSTANCE.logFilePath = loopDAWApp.getExternalCacheDir().getAbsolutePath()
                + File.separator + "logs"
//                + File.separator + "LoopDAWLog_" + loopDAWApp.getCreDtm() + ".log"; TODO  - per instance log files.
                + File.separator + "LoopDAWApp.log";
        new File(logFilePath).getParentFile().mkdirs();

    }

    public void msg(String msg) {
        try {
            FileWriter writer = new FileWriter(new File(logFilePath), true);
            writer.write(Calendar.getInstance().getTime().toString() + "\t" + msg);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reset() {
        try {
            FileWriter writer = new FileWriter(new File(logFilePath), false);
            writer.write("");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getLogFilePath() {
        return logFilePath;
    }
}
