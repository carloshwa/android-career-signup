package com.doubleencore.careersignup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    private static DatabaseHelper sDatabaseHelper;

    private EditText mFirstNameEdit;
    private EditText mLastNameEdit;
    private EditText mEmailEdit;
    private AutoCompleteTextView mSchoolEdit;
    private Spinner mGradeSpinner;

    private ArrayAdapter<CharSequence> mSchoolAdapter;
    private SpinnerAdapter mGradeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        setContentView(R.layout.activity_main);

        mFirstNameEdit = (EditText) findViewById(R.id.first_name_edit);
        mLastNameEdit = (EditText) findViewById(R.id.last_name_edit);
        mEmailEdit = (EditText) findViewById(R.id.email_edit);
        mSchoolEdit = (AutoCompleteTextView) findViewById(R.id.school_edit);
        mGradeSpinner = (Spinner) findViewById(R.id.grade_spinner);

        mFirstNameEdit.setNextFocusDownId(R.id.last_name_edit);
        mLastNameEdit.setNextFocusDownId(R.id.email_edit);

        mSchoolAdapter = ArrayAdapter.createFromResource(this, R.array.school_array, android.R.layout.simple_dropdown_item_1line);
        mSchoolEdit.setAdapter(mSchoolAdapter);

        mSchoolEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, String.format("%d %d", actionId, event == null ? -1 : event.getAction()));
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mGradeSpinner.performClick();
                    return true;
                }

                return false;
            }
        });

        mGradeAdapter = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        ((ArrayAdapter<CharSequence>) mGradeAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGradeSpinner.setAdapter(mGradeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dumpUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void dumpUsers() {
        try {
            for (User user : sDatabaseHelper.getUserDao()) {
                Log.d(TAG, String.format("%s %s,%s,%s,%s",
                        user.getFirstName(), user.getLastName(), user.getEmail(), user.getSchool(), user.getGrade()));
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void clearFields() {
        mFirstNameEdit.setText(null);
        mLastNameEdit.setText(null);
        mEmailEdit.setText(null);
        mSchoolEdit.setText(null);
        mGradeSpinner.setSelection(0, false);
        mFirstNameEdit.requestFocus();
    }

    private User saveFields() {
        String firstName = mFirstNameEdit.getText().toString();
        String lastName = mLastNameEdit.getText().toString();
        String email = mEmailEdit.getText().toString();
        String school = mSchoolEdit.getText().toString();
        String grade = (String) mGradeSpinner.getSelectedItem();

        return User.create(sDatabaseHelper, firstName, lastName, email, school, grade);
    }

    private boolean isMissingFields() {
        return TextUtils.isEmpty(mFirstNameEdit.getText()) ||
                TextUtils.isEmpty(mLastNameEdit.getText()) ||
                TextUtils.isEmpty(mEmailEdit.getText());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                clearFields();
                return true;
            case R.id.action_save:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mFirstNameEdit.getWindowToken(), 0);

                if (isMissingFields()) {
                    Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT).show();
                } else {
                    User user = saveFields();
                    if (user != null) {
                        Toast.makeText(this,
                                getString(R.string.thank_you, user.getFirstName(), user.getLastName()),
                                Toast.LENGTH_LONG).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
