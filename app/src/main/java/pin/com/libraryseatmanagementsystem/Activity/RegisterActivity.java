package pin.com.libraryseatmanagementsystem.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import pin.com.libraryseatmanagementsystem.Util.MD5;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerAccount;
    private EditText registerPassword;
    private EditText registerConfirmPassword;
    private Button registerButton;
    private TextView toLogin;
    private NetworkConnection network = NetworkConnection.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //绑定控件
        registerAccount = findViewById(R.id.register_account);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.register_confirm_password);
        registerButton = findViewById(R.id.register_button);
        toLogin = findViewById(R.id.register_text2);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountString = registerAccount.getText().toString();
                String passwordString = registerPassword.getText().toString();
                String confirmString = registerConfirmPassword.getText().toString();
                if (accountString.equals("") || passwordString.equals("")||confirmString.equals(""))
                    Toast.makeText(RegisterActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                else if (!passwordString.equals(confirmString))
                    Toast.makeText(RegisterActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                else {
                    Reader reader = new Reader();
                    reader.setAccount(accountString);
                    reader.setPassword(MD5.MD5Util(passwordString));
                    Gson gson = new Gson();
                    String json = gson.toJson(reader);
                    Call<Reader> registerCall = network.register(json);
                    registerCall.enqueue(new Callback<Reader>() {
                        @Override
                        public void onResponse(Call<Reader> call, Response<Reader> response) {
                            switch (response.code()) {
                                case 201:
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    registerPassword.setText("");
                                    registerConfirmPassword.setText("");
                                    break;
                                case 403:
                                    Toast.makeText(RegisterActivity.this, "该账号已被使用", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Reader> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
