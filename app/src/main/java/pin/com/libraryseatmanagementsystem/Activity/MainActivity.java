package pin.com.libraryseatmanagementsystem.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pin.com.libraryseatmanagementsystem.Adapter.MyFragmentPagerAdapter;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Bean.Seat;
import pin.com.libraryseatmanagementsystem.Fragment.BaseFragment;
import pin.com.libraryseatmanagementsystem.Fragment.PersonFragment;
import pin.com.libraryseatmanagementsystem.Fragment.SeatFragment;
import pin.com.libraryseatmanagementsystem.Fragment.StatusFragment;
import pin.com.libraryseatmanagementsystem.Interface.OnFragmentInteractionListener;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private Reader reader;

    private NetworkConnection connection = NetworkConnection.getInstance();

    private SharedPreferences preferences;

    private ViewPager viewPager;
    private RadioGroup radioGroup;

    private List<BaseFragment> fragments;
    private MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAccount();
        initView();


    }

    private void initAccount() {
        preferences = getSharedPreferences("LoginAccount", MODE_PRIVATE);
        String account = preferences.getString("account", "");
        String password = preferences.getString("password", "");
        reader = new Reader();
        reader.setAccount(account);
        reader.setPassword(password);
    }

    private void initView() {
        viewPager = findViewById(R.id.fragment_vp);
        radioGroup = findViewById(R.id.radio_group);

        fragments = new ArrayList<>(3);
        fragments.add(SeatFragment.newInstance(reader, ""));
        fragments.add(StatusFragment.newInstance("", ""));
        fragments.add(PersonFragment.newInstance(reader, ""));

        FragmentManager fm = getSupportFragmentManager();
        adapter = new MyFragmentPagerAdapter(fm, fragments);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(pageChangeListener);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    @Override
    public void login() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, 114);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 114 && resultCode == 514) {
            Bundle bundle = data.getExtras();
            reader = (Reader) bundle.getSerializable("reader");
            fragments.get(0).refreshReader(reader);
            fragments.get(1).refreshReader(reader);
            fragments.get(2).refreshReader(reader);
        }
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.seat_button:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.state_button:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.person_button:
                    viewPager.setCurrentItem(2);
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(pageChangeListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
