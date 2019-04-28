package pin.com.libraryseatmanagementsystem.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanActivity extends AppCompatActivity {

    private RadioGroup radioGroup1;
    private EditText account;
    private RadioGroup radioGroup2;
    private Button button;
    private int day = 0;
    private boolean op = true;

    private NetworkConnection connection = NetworkConnection.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);

        initView();

    }

    void initView() {
        radioGroup1 = findViewById(R.id.radio_group1);
        radioGroup1.setOnCheckedChangeListener(group1);
        radioGroup2 = findViewById(R.id.radio_group2);
        radioGroup2.setOnCheckedChangeListener(group2);
        account = findViewById(R.id.op_account);
        button = findViewById(R.id.ok_button);
        button.setOnClickListener(send);
    }

    private RadioGroup.OnCheckedChangeListener group1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            op = !op;
            if (checkedId == R.id.lift)
                radioGroup2.setVisibility(View.INVISIBLE);
            else
                radioGroup2.setVisibility(View.VISIBLE);
        }
    };




    private RadioGroup.OnCheckedChangeListener group2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.week)
                day = 0;
            else if (checkedId == R.id.month)
                day = 1;
            else
                day = 2;
        }
    };

    private View.OnClickListener send = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Call<Reader> banCall = connection.banReader(account.getText().toString(), day, op);
            banCall.enqueue(new Callback<Reader>() {
                @Override
                public void onResponse(Call<Reader> call, Response<Reader> response) {
                    switch (response.code()) {
                        case 200: {
                            Toast.makeText(BanActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 403: {
                            Toast.makeText(BanActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 404: {
                            Toast.makeText(BanActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 409: {
                            Toast.makeText(BanActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        default:
                            Toast.makeText(BanActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<Reader> call, Throwable t) {
                    Log.getStackTraceString(t);
                    Toast.makeText(BanActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
