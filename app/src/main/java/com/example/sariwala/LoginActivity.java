package com.example.sariwala;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sariwala.constants.Constant;
import com.example.sariwala.model.User;
import com.example.sariwala.services.MyInterface;
import com.example.sariwala.services.RetrofitClient;
import com.example.sariwala.services.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnLogin;
    String emailpatt="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static ServiceApi serviceApi;
    private MyInterface loginFromActivityListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL).create(ServiceApi.class);
        email=findViewById(R.id.email_reg);
        pass=findViewById(R.id.password_reg);

        btnLogin=findViewById(R.id.btn_login);


       btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString()==null && pass.getText().toString()==null) {
                    ShowMessage("Enter Email and Password");
                    Toast.makeText(LoginActivity.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().trim().matches(emailpatt)==false&&8>pass.getText().length()){
                    ShowMessage("Invalid Email and Password");
                    Toast.makeText(LoginActivity.this, "Invalid Email and Password", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().trim().matches(emailpatt)==false){
                    ShowMessage("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else if (8>pass.getText().length()) {
                    ShowMessage("Password Must be of Minimum 8 characters");
                    Toast.makeText(LoginActivity.this, "Password Must be of Minimum 8 characters", Toast.LENGTH_SHORT).show();
                }



                else
                {
                    String Email = email.getText().toString();
                    String Password = pass.getText().toString();

                    Call<User> userCall = serviceApi.doLogin(Email, Password);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body().getResponse().equals("data")){
                                MainActivity.appPreference.setLoginStatus(true); // set login status in sharedPreference
                                loginFromActivityListener.login(
                                        response.body().getEmail());

                            }
                            else if (response.body().getResponse().equals("login_failed")){
                                MainActivity.appPreference.showToast("Error. Login Failed");
                                email.setText("");
                                pass.setText("");
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

    private void ShowMessage(String invalid_input) {
    }
}
