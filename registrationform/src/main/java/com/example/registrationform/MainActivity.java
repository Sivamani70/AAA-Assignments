package com.example.registrationform;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.registrationform.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Fields {
    private static final String TAG = "MainActivity";
    String branch, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        final List<String> languages = new ArrayList<>();
        final SharedPreferences preferences = getSharedPreferences("com.example.registrationform", MODE_MULTI_PROCESS);
        binding.male.setChecked(true);

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

            final boolean allFilled = !name.isEmpty() && !rollNumber.isEmpty() && !eMail.isEmpty() && !phoneNumber.isEmpty() && !password.isEmpty();

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


            Log.w(TAG, "onCreate: " + Fields.NAME + " : " + name);
            Log.w(TAG, "onCreate: " + Fields.ROLL_NUMBER + " : " + rollNumber);
            Log.w(TAG, "onCreate: " + Fields.MAIL + " : " + eMail);
            Log.w(TAG, "onCreate: " + Fields.PHONE_NUMBER + " : " + phoneNumber);
            Log.w(TAG, "onCreate: " + Fields.PASSWORD + " : " + password);
            Log.w(TAG, "onCreate: " + Fields.BRANCH + " : " + branch);
            Log.w(TAG, "onCreate: " + Fields.YEAR + " : " + year);
            Log.w(TAG, "onCreate: " + Fields.GENDER + " : " + gender);

            String builder = "Name : " + name + "\n" +
                    "Roll Number : " + rollNumber + "\n" +
                    "Gender : " + gender + "\n" +
                    "Mail : " + eMail + "\n" +
                    "Phone Number : " + phoneNumber + "\n" +
                    "Password : " + password + "\n" +
                    "Branch : " + branch + "\n" +
                    "Year of Pass : " + year + "\n" +
                    "Languages : " + languages.toString();
            Toast.makeText(MainActivity.this, builder, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = preferences.edit();
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


}