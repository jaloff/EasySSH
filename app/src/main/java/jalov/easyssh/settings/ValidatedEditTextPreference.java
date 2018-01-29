package jalov.easyssh.settings;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-25.
 */

public class ValidatedEditTextPreference extends EditTextPreference {
    private List<Validator<String>> validators = new ArrayList<>();
    private OnPreferenceChangeListener listener;

    public ValidatedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValidatedEditTextPreference(Context context) {
        super(context);
    }

    private void init() {
        this.setDialogLayoutResource(R.layout.preference_dialog_edittext);
        validators = new ArrayList<>();
        super.setOnPreferenceChangeListener(this::onPreferenceChange);
    }

    @Override
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        listener = onPreferenceChangeListener;
    }

    private boolean onPreferenceChange(Preference preference, Object newValue) {
        if(listener.onPreferenceChange(preference, newValue)) {
            setSummary(newValue.toString());
            return true;
        }
        return false;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        setSummary(getText());
    }

    public void addValidator(Validator<String> validator) {
        validators.add(validator);
    }

    public List<Validator<String>> getValidators() {
        return validators;
    }
}
