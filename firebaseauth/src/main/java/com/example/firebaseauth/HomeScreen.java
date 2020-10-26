package com.example.firebaseauth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.firebaseauth.databinding.ActivityHomeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityHomeScreenBinding binding;
    ProgressDialog progressDialog;
    private DataModel dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("HomeScreen");
//        actionBar.setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);

        auth = FirebaseAuth.getInstance();
        fetchData();
        progressDialog.show();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (auth.getCurrentUser() != null) {
            finishAffinity();
        } else {
            finish();
        }
    }


    void fetchData() {
        final String[] key = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()).split("@");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userData");
        Query query = reference.orderByChild(Fields.USERNAME).equalTo(key[0]);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder builder = new StringBuilder();
                for (DataSnapshot data : snapshot.getChildren()) {
                    DataModel model = data.getValue(DataModel.class);
                    HomeScreen.this.dataModel = model;
                    assert model != null;
                    builder.append("Name :: ").append(model.getNAME()).append("\n")
                            .append("User Name :: ").append(model.getUSERNAME()).append("\n")
                            .append("Roll Number :: ").append(model.getROLL_NUMBER()).append("\n")
                            .append("Mail :: ").append(model.getMAIL()).append("\n")
                            .append("Phone Number :: ").append(model.getPHONE_NUMBER()).append("\n")
                            .append("Password :: ").append(model.getPASSWORD()).append("\n")
                            .append("LANGUAGES :: ").append(model.getLANGUAGES()).append("\n")
                            .append("Branch :: ").append(model.getBRANCH()).append("\n")
                            .append("Year :: ").append(model.getYEAR()).append("\n")
                            .append("Gender :: ").append(model.getGENDER());
                }
                binding.data.setText(builder.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int edit = R.id.editData;
        final int logout = R.id.signOut;
        switch (item.getItemId()) {
            case edit: {
                upDateData();
                break;
            }
            case logout: {
                auth.signOut();
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    void upDateData() {
        View view = LayoutInflater.from(HomeScreen.this).inflate(R.layout.edit_data, null, false);

        final EditText userName = view.findViewById(R.id.userName);
        final EditText email = view.findViewById(R.id.email);
        final EditText rollNumber = view.findViewById(R.id.rollNumber);
        final EditText name = view.findViewById(R.id.personName);
        final EditText number = view.findViewById(R.id.phoneNumber);
        final EditText yearOfPass = view.findViewById(R.id.yearOfPass);
        final EditText eBranch = view.findViewById(R.id.branch);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setView(view);
        builder.setCancelable(false);
        userName.setText(dataModel.getUSERNAME());
        email.setText(dataModel.getMAIL());
        rollNumber.setText(dataModel.getROLL_NUMBER());
        name.setText(dataModel.getNAME());
        number.setText(dataModel.getPHONE_NUMBER());
        yearOfPass.setText(dataModel.getYEAR());
        eBranch.setText(dataModel.getBRANCH());

        userName.setEnabled(false);
        email.setEnabled(false);
        rollNumber.setEnabled(false);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("UpDate", (dialog, which) -> {
            progressDialog.show();
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put(Fields.NAME, String.valueOf(name.getText()));
            objectMap.put(Fields.PHONE_NUMBER, String.valueOf(number.getText()));
            objectMap.put(Fields.YEAR, String.valueOf(yearOfPass.getText()));
            objectMap.put(Fields.BRANCH, String.valueOf(eBranch.getText()));
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userData");
            reference.child(String.valueOf(userName.getText())).updateChildren(objectMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(HomeScreen.this, "Data Updated Sucessfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        builder.show();

    }
}