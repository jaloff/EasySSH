package jalov.easyssh.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.BaseAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jalov on 2018-02-20.
 */
@Singleton
public class Logger {
    public static final String SERVER_FIRST_MESSAGE = "Server listening on";
    private BaseAdapter adapter;
    private List<String> logs;

    @Inject
    public Logger() {
        this.logs = new ArrayList<>();
    }

    public void log(String message) {
        logs.add(0, message);
        if (adapter != null) {
            new Handler(Looper.getMainLooper()).post(() -> adapter.notifyDataSetChanged());
        }
    }

    public boolean registerSshServerInputStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String firstLine = null;

        try {
            firstLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (firstLine == null) {
            return false;
        }

        log(firstLine);

        if (!firstLine.startsWith(SERVER_FIRST_MESSAGE)) {
            return false;
        }

        new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    log(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return true;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }
}
