package jalov.easyssh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import jalov.easyssh.auth.AuthorizedKeysActivity;
import jalov.easyssh.settings.SettingsActivity;

import static jalov.easyssh.RootManager.su;

public class MainActivity extends AppCompatActivity {
    public static final String START_SSH = "start-ssh";
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    public static final String TAG = "MainActivity";
    private Optional<ProcessInfo> sshdProcessInfo;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> toggleSSH());

        updateSshdProcessInfo();

        boolean runOnStart = sharedPreferences.getBoolean(getResources().getString(R.string.run_on_app_start_key), false);
        if(runOnStart && !sshdProcessInfo.isPresent()) {
            toggleSSH();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.action_authorized_keys) {
            intent = new Intent(MainActivity.this, AuthorizedKeysActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleSSH() {
        if(sshdProcessInfo.isPresent()) {
            su("kill -9 " + sshdProcessInfo.get().getPID());
        } else {
            su(START_SSH);
        }

        updateSshdProcessInfo();
    }

    public void updateSshdProcessInfo() {
        TextView tv = findViewById(R.id.tv_status);
        sshdProcessInfo = getSshdProcessesInfo();
        String status = "Stopped";
        if(sshdProcessInfo.isPresent()) {
            status = "Running";
            fab.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            fab.setImageResource(android.R.drawable.ic_media_play);
        }
        tv.setText(status);
    }

    public Optional<ProcessInfo> getSshdProcessesInfo() {
        Optional<ProcessInfo> processInfo = Optional.empty();
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(su.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(su.getInputStream()));

            dos.writeBytes("ps | grep " + SSHD_APP_NAME + "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            su.waitFor();

            if(reader.ready()) {
                String line = reader.readLine();
                String[] cols = line.split("\\s+");
                processInfo = Optional.of(new ProcessInfo(cols[0], cols[1], cols[8]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return processInfo;
    }
}
