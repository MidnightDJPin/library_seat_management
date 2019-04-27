package pin.com.libraryseatmanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    private Reader reader;

    private NetworkConnection connection = NetworkConnection.getInstance();

    private EditText name;
    private EditText phone;
    private EditText email;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        reader = (Reader) bundle.getSerializable("reader");

        initView();

    }

    void initView() {
        name = findViewById(R.id.edit_name);
        name.setText(reader.getRname()==null?"":reader.getRname());
        name.addTextChangedListener(watcher);
        phone = findViewById(R.id.edit_phone);
        phone.setText(reader.getPhone()==null?"":reader.getPhone());
        phone.addTextChangedListener(watcher);
        email = findViewById(R.id.edit_email);
        email.setText(reader.getEmail()==null?"":reader.getEmail());
        email.addTextChangedListener(watcher);
        save = findViewById(R.id.save);
        save.setOnClickListener(listener);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            save.setVisibility(View.VISIBLE);
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            reader.setRname(name.getText().toString());
            reader.setPhone(phone.getText().toString());
            reader.setEmail(email.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(reader);
            Call<Reader> updateCall = connection.updateReader(json);
            updateCall.enqueue(new Callback<Reader>() {
                @Override
                public void onResponse(Call<Reader> call, Response<Reader> response) {
                    if (response.code()==200) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("reader", reader);
                        intent.putExtras(bundle);
                        setResult(514, intent);
                        Toast.makeText(UpdateActivity.this, "修改已保存", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Reader> call, Throwable t) {
                    Toast.makeText(UpdateActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
