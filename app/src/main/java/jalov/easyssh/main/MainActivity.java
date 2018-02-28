package jalov.easyssh.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Optional;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.R;
import jalov.easyssh.auth.AuthorizedKeysActivity;
import jalov.easyssh.server.SshServer;
import jalov.easyssh.settings.SettingsActivity;
import jalov.easyssh.utils.Logger;
import jalov.easyssh.utils.NetworkUtils;

public class MainActivity extends BaseActivity {
    final String TAG = this.getClass().getName();
    private FloatingActionButton fab;
    private SshServer.StatusChangeListener serverStatusListener;
    @Inject
    SshServer server;
    @Inject
    Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> toggleSSH());

        if (settings.runOnAppStart()) {
            server.start();
        }

        // Logging view
        ListView lw = findViewById(R.id.lv_logs);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logger.getLogs());
        logger.setAdapter(adapter);
        lw.setAdapter(adapter);

        serverStatusListener = this::updateServerStatus;
        server.addOnStatusChangeListener(serverStatusListener);
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
        if (server.isRunning()) {
            server.stop();
        } else {
            server.start();
        }
    }

    public void updateServerStatus(boolean status) {
        TextView statusTv = findViewById(R.id.tv_status);
        String statusText = "Stopped";
        if (status) {
            statusText = "Running";
            fab.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            fab.setImageResource(android.R.drawable.ic_media_play);
        }
        statusTv.setText(statusText);
        // Update IP Address
        TextView ipTv = findViewById(R.id.tv_ip_address);
        TextView portTv = findViewById(R.id.tv_port);
        String ipText = "";
        String portText = "";
        if (status) {
            Optional<String> ipAddress = NetworkUtils.getIPAddress();
            ipText = ipAddress.isPresent() ? ipAddress.get() : getResources().getString(R.string.no_ip_address);
            portText = settings.getPort();
        }
        ipTv.setText(ipText);
        portTv.setText(portText);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.removeOnStatusChangeListener(serverStatusListener);
    }
}
