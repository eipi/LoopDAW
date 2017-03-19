package name.eipi.loopdaw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.util.LoopDAWLogger;

public class LogViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        StringBuffer sb = new StringBuffer();
        String logFilePath = LoopDAWLogger.getInstance().getLogFilePath();
        File logFile = new File(logFilePath);
        if (logFile.exists()) {
            FileReader fr = null;
            BufferedReader reader = null;
            try {
                fr = new FileReader(logFile);
                reader = new BufferedReader(fr);
                String bufferLine;
                while ((bufferLine = reader.readLine())!=null) {
                    sb.append(bufferLine).append("\r\n\n");
                }
            } catch (IOException fEx) {
                sb.append(fEx.getMessage());
                // shouldn't get here.
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {

                    }
                }
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException ex) {

                    }
                }
            }
        }
        TextView textView = (TextView) findViewById(R.id.logViewer);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(sb.toString());
    }
}
