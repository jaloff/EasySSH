package jalov.easyssh.main;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import jalov.easyssh.R;
import jalov.easyssh.settings.Settings;

/**
 * Created by jalov on 2018-02-27.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    Settings settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(settings.isDarkThemeEnabled()) {
            TypedArray a = getTheme().obtainStyledAttributes(new int[]{R.attr.windowActionBar});
            boolean hasActionBar = a.getBoolean(0, true);
            if (hasActionBar) {
                setTheme(R.style.AppTheme_Dark);
            } else {
                setTheme(R.style.AppTheme_Dark_NoActionBar);
            }
        }
        super.onCreate(savedInstanceState);
    }
}
