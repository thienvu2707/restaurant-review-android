package com.android.thienvu.restaurantreview;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.thienvu.restaurantreview.Common.Common;
import com.android.thienvu.restaurantreview.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    EditText editPhone, editName, editPassword ;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Initial Database from FireBase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please waiting....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if already user phone
                            if (dataSnapshot.child(editPhone.getText().toString()).exists() || dataSnapshot.child(editName.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number or User Name already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(editName.getText().toString(), editPassword.getText().toString());
                                table_user.child(editPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else
                {
                    Toast.makeText(SignUp.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

}
