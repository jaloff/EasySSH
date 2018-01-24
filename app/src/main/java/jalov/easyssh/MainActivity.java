package jalov.easyssh;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Optional;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.auth.AuthorizedKeysActivity;
import jalov.easyssh.server.SshServer;
import jalov.easyssh.settings.Settings;
import jalov.easyssh.settings.SettingsActivity;
import jalov.easyssh.settings.StatusChangeListener;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getName();
    private FloatingActionButton fab;
    private StatusChangeListener serverStatusListener = this::updateServerStatus;
    @Inject
    Settings settings;
    @Inject
    SshServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> toggleSSH());

        if(settings.runOnAppStart()) {
            server.start();
        }

        server.addOnStatusChangeListener(serverStatusListener);
        updateIPAdress();
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
        if(server.isRunning()) {
            server.stop();
        } else {
            server.start();
        }
    }

    public void updateServerStatus(boolean status) {
        TextView tv = findViewById(R.id.tv_status);
        String text = "Stopped";
        if(status) {
            text = "Running";
            fab.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            fab.setImageResource(android.R.drawable.ic_media_play);
        }
        tv.setText(text);
    }

    public void updateIPAdress() {
        TextView tv = findViewById(R.id.tv_ip_address);
        Optional<String> ipAddress = NetworkUtils.getIPAdress();
        String text = ipAddress.isPresent() ? "root@" + ipAddress.get() + ":" + settings.getPort()
                : getResources().getString(R.string.no_ip_address);
        tv.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.removeOnStatusChangeListener(serverStatusListener);
    }
}
