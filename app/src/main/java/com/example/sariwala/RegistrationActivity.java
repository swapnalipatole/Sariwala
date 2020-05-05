package com.example.sariwala;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sariwala.constants.Constant;
import com.example.sariwala.model.User;
import com.example.sariwala.services.RetrofitClient;
import com.example.sariwala.services.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private EditText email;
    private EditText pass,repass;
    private EditText phone;
    private EditText name;
    private Button signin;
    String emailpatt="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static ServiceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL).create(ServiceApi.class);
        email=findViewById(R.id.email_reg);
        pass=findViewById(R.id.password_reg);
        phone=findViewById(R.id.phone_reg);
        signin=findViewById(R.id.btn_signup);
        name = findViewById(R.id.name);
        repass = findViewById(R.id.repassword_reg);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sname = name.getText().toString();
                String semail = email.getText().toString();
                String sphone = phone.getText().toString();
                String spass = pass.getText().toString();
                String srepass = repass.getText().toString();

                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                if (name.getText().length()==0||phone.getText().length()!=10||!srepass.equals(spass)||email.getText().toString().trim().matches(emailpatt)==false||8>pass.getText().length()){
                    if(phone.getText().length()!=10){
                        ShowMessage("Invalid Phone Number");
                        Toast.makeText(RegistrationActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    }
                    if(email.getText().toString().trim().matches(emailpatt)==false){
                        ShowMessage("Invalid Email");
                        Toast.makeText(RegistrationActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                    if(8>pass.getText().length()){
                        ShowMessage("Password Must be of Minimum 8 characters");
                        Toast.makeText(RegistrationActivity.this, "Password Must be of Minimum 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    if(srepass!=spass)
                    {
                        ShowMessage("Password does not match");
                        Toast.makeText(RegistrationActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                    if(name.getText().length()==0)
                    {
                        ShowMessage("Enter Name");
                        Toast.makeText(RegistrationActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Call<User> userCall = serviceApi.doRegistration(sname, semail, spass, sphone);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body().getResponse().equals("inserted")){
                                Log.e("response", response.body().getResponse());
                                name.setText("");
                                email.setText("");
                                phone.setText("");
                                pass.setText("");
                                MainActivity.appPreference.showToast("Registered Successfully");
                            } else if (response.body().getResponse().equals("exists")){
                                MainActivity.appPreference.showToast("This email already exists");
                            } else if (response.body().getResponse().equals("error")){
                                MainActivity.appPreference.showToast("Oops! something went wrong.");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                        }
                    });
                }
            }
        });


    }

    private void ShowMessage(String invalid_email_and_password) {
    }
}
