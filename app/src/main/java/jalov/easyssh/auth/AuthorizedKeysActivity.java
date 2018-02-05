package jalov.easyssh.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.R;

public class AuthorizedKeysActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    static final int SELECT_FILE_RC = 10;
    private AuthorizedKeysAdapter authorizedKeysAdapter;
    @Inject
    AuthorizedKeysManager authorizedKeysManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_keys);

        ListView lw = findViewById(R.id.lw_authorized_keys);
        authorizedKeysAdapter = new AuthorizedKeysAdapter(this, authorizedKeysManager);
        lw.setAdapter(authorizedKeysAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.authorized_keys_menu, menu);

        MenuItem item = menu.findItem(R.id.add_authorized_key);
        item.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, SELECT_FILE_RC);
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == SELECT_FILE_RC && resultCode == RESULT_OK) {
            try {
                Uri uri = resultData.getData();
                if (uri != null) {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    authorizedKeysManager.addAuthorizedKeysFromInputStream(inputStream);
                    authorizedKeysAdapter.notifyDataSetChanged();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
