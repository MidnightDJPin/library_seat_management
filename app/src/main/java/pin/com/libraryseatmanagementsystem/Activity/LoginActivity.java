package pin.com.libraryseatmanagementsystem.Activity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private EditText loginAccount;
    private EditText loginPassword;
    private Button loginButton;
    private TextView toRegister;
    private NetworkConnection network = NetworkConnection.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //绑定控件
        loginAccount = findViewById(R.id.login_account);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        toRegister = findViewById(R.id.login_text2);

        //登录事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountString = loginAccount.getText().toString();
                String passwordString = loginPassword.getText().toString();
                if (accountString.equals("")||passwordString.equals(""))
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                else {
                    String password = MD5.MD5Util(passwordString);
                    Reader reader = new Reader();
                    reader.setAccount(accountString);
                    reader.setPassword(password);
                    Gson gson = new Gson();
                    String json = gson.toJson(reader);
                    /*Call<Reader> loginCall = network.login(json);
                    loginCall.enqueue(new Callback<Reader>() {
                        @Override
                        public void onResponse(Call<Reader> call, Response<Reader> response) {
                            switch (response.code()) {
                                case 200: {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Reader loginReader = response.body();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("reader", loginReader);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                }
                                case 401: {
                                    Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                default: {
                                    Toast.makeText(LoginActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Reader> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });

        //切换至注册页面
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}
