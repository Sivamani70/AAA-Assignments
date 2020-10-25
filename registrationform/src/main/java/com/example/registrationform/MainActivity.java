package com.example.registrationform;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.registrationform.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Fields {
    private static final String TAG = "MainActivity";
    String branch, year;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        final List<String> languages = new ArrayList<>();
        final SharedPreferences preferences = getSharedPreferences(Fields.PREFS_KEY, MODE_MULTI_PROCESS);
        binding.male.setChecked(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        ArrayAdapter<CharSequence> branchData = ArrayAdapter.createFromResource(
                MainActivity.this,
                R.array.branch_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        ArrayAdapter<CharSequence> yearsData = ArrayAdapter.createFromResource(
                MainActivity.this,
                R.array.years,
                android.R.layout.simple_spinner_dropdown_item
        );

        binding.branch.setAdapter(branchData);
        binding.years.setAdapter(yearsData);
        binding.branch.setOnItemSelectedListener(this);
        binding.years.setOnItemSelectedListener(this);


        binding.submit.setOnClickListener(v -> {
            languages.clear();
            final String name = String.valueOf(binding.name.getText());
            final String rollNumber = String.valueOf(binding.rollNumber.getText());
            final String eMail = String.valueOf(binding.email.getText());
            final String phoneNumber = String.valueOf(binding.phoneNumber.getText());
            final String password = String.valueOf(binding.password.getText());
            final String branch = this.branch;
            final String year = this.year;
            final int checkedButton = binding.genderGroup.getCheckedRadioButtonId();
            final RadioButton radioButton = findViewById(checkedButton);
            final String gender = radioButton.getText().toString();


            if (binding.telugu.isChecked()) languages.add("Telugu");
            if (binding.english.isChecked()) languages.add("English");
            if (binding.hindi.isChecked()) languages.add("Hindi");

            final boolean allFilled = !name.isEmpty() && !rollNumber.isEmpty() && !eMail.isEmpty() && eMail.contains("@") && !phoneNumber.isEmpty() && !password.isEmpty();

            if (!(allFilled)) {
                Snackbar.make(v, "Fill All the details", Snackbar.LENGTH_SHORT).show();
                binding.name.setError("Enter Name");
                binding.rollNumber.setError("Enter Roll Number");
                binding.email.setError("Enter E-Mail");
                binding.phoneNumber.setError("Enter Phone Number");
                binding.password.setError("Enter Password");
                return;
            }

            if (binding.branch.getSelectedItemId() == 0 || binding.years.getSelectedItemId() == 0 || languages.isEmpty()) {
                Snackbar.make(v, "Select The year and Branch and Languages ", Snackbar.LENGTH_SHORT).show();
                return;
            }


            final SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Fields.NAME, name);
            editor.putString(Fields.ROLL_NUMBER, rollNumber);
            editor.putString(Fields.MAIL, eMail);
            editor.putString(Fields.PHONE_NUMBER, phoneNumber);
            editor.putString(Fields.GENDER, gender);
            editor.putString(Fields.PASSWORD, password);
            editor.putString(Fields.BRANCH, branch);
            editor.putString(Fields.YEAR, year);
            editor.putString(Fields.LANGUAGES, languages.toString());
            editor.apply();
            clearFields(binding);
            message(v);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Register with Firebase")
                    .setCancelable(false)
                    .setMessage("Would you like to register with Firebase with E-mail & Password")
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("yes", (dialog, which) -> {
                        progressDialog.show();
                        final FirebaseAuth auth = FirebaseAuth.getInstance();

                        auth.createUserWithEmailAndPassword(eMail, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(v, "User Registration Successful", Snackbar.LENGTH_SHORT).show();
                                        uploadData(preferences);
                                    } else {
                                        Snackbar.make(v, "User Registration Failed", Snackbar.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    })
                    .show();

        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == findViewById(R.id.branch)) {
            branch = String.valueOf(parent.getItemAtPosition(position));
            Log.e(TAG, "onItemSelected: " + branch);
            return;
        }
        if (parent == findViewById(R.id.years)) {
            year = String.valueOf(parent.getItemAtPosition(position));
            Log.e(TAG, "onItemSelected: " + year);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    void clearFields(ActivityMainBinding binding) {
        binding.name.setText("");
        binding.rollNumber.setText("");
        binding.email.setText("");
        binding.phoneNumber.setText("");
        binding.password.setText("");
        binding.branch.setSelection(0, true);
        binding.years.setSelection(0, true);
        binding.telugu.setChecked(false);
        binding.english.setChecked(false);
        binding.hindi.setChecked(false);
    }

    void uploadData(SharedPreferences prefs) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("userData");
        Map<String, Object> data = new HashMap<>();

        String[] keys = prefs.getString(MAIL, "").split("@");
        final String userName = keys[0];


        data.put(Fields.USERNAME, userName);
        data.put(Fields.NAME, prefs.getString(NAME, ""));
        data.put(Fields.ROLL_NUMBER, prefs.getString(ROLL_NUMBER, ""));
        data.put(Fields.MAIL, prefs.getString(MAIL, ""));
        data.put(Fields.PHONE_NUMBER, prefs.getString(PHONE_NUMBER, ""));
        data.put(Fields.PASSWORD, prefs.getString(PASSWORD, ""));
        data.put(Fields.BRANCH, prefs.getString(BRANCH, ""));
        data.put(Fields.GENDER, prefs.getString(GENDER, ""));
        data.put(Fields.YEAR, prefs.getString(YEAR, ""));
        data.put(Fields.LANGUAGES, prefs.getString(LANGUAGES, ""));

        database.child(userName).setValue(data)
                .addOnSuccessListener(aVoid -> Log.w(TAG, "uploadData: Success"))
                .addOnFailureListener(Throwable::printStackTrace);
        progressDialog.dismiss();
    }


}