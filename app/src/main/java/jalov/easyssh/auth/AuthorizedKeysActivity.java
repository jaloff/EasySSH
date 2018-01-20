package jalov.easyssh.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.R;

public class AuthorizedKeysActivity extends AppCompatActivity {
    @Inject
    AuthorizedKeysManager authorizedKeysManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_keys);

        ListView lw = findViewById(R.id.lw_authorized_keys);
        lw.setAdapter(new AuthorizedKeysAdapter(this, authorizedKeysManager.getKeys()));
    }
}
