package com.example.firebaseauth;

import android.app.ProgressDialog;
import android.os.Bundle;
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

import java.util.Objects;

public class HomeScreen extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityHomeScreenBinding binding;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("HomeScreen");
//        actionBar.setDisplayHomeAsUpEnabled(true);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading data...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);

        auth = FirebaseAuth.getInstance();
        fetchData();
        dialog.show();

        binding.logout.setOnClickListener(v -> {
            auth.signOut();
            finish();
        });
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
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder builder = new StringBuilder();
                for (DataSnapshot data : snapshot.getChildren()) {
                    DataModel model = data.getValue(DataModel.class);
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
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}