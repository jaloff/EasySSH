package jalov.easyssh.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-26.
 */

public class ValidatedPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private EditText editText;
    private TextView errorTextView;
    private int whichButtonClicked;

    public static ValidatedPreferenceDialogFragmentCompat newInstance(String key) {
        final ValidatedPreferenceDialogFragmentCompat fragment = new ValidatedPreferenceDialogFragmentCompat();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        editText = view.findViewById(android.R.id.edit);
        errorTextView = view.findViewById(R.id.tv_error_message);

        if (editText == null) {
            throw new IllegalStateException("Dialog must contain EditText with id 'edit'");
        }

        DialogPreference preference = getPreference();
        String text = null;
        if (preference instanceof ValidatedEditTextPreference) {
            text = ((ValidatedEditTextPreference) preference).getText();
        }

        // Set initial value
        if (text != null) {
            editText.setText(text);
            editText.setSelection(editText.length());
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        // Ignore function argument and use custom flag
        if (whichButtonClicked == DialogInterface.BUTTON_POSITIVE) {
            DialogPreference preference = getPreference();
            if (preference instanceof ValidatedEditTextPreference) {
                ValidatedEditTextPreference validatedPreference = (ValidatedEditTextPreference) preference;
                String text = editText.getText().toString();
                // Call user listener
                if (validatedPreference.callChangeListener(text)) {
                    // Save preference
                    validatedPreference.setText(text);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Show keyboard
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // Setup button identifier
        whichButtonClicked = DialogInterface.BUTTON_NEGATIVE;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        DialogPreference preference = getPreference();
        // Bind validation action to button
        if (dialog != null && preference instanceof ValidatedEditTextPreference) {
            ValidatedEditTextPreference validatedPreference = (ValidatedEditTextPreference) preference;
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                // Actual validation
                for (Validator<String> validator : validatedPreference.getValidators()) {
                    if (!validator.isValid(editText.getText().toString())) {
                        errorTextView.setText(validator.getErrorMessage());
                        return;
                    }
                }
                whichButtonClicked = DialogInterface.BUTTON_POSITIVE;
                dialog.dismiss();
            });
        }
    }
}
