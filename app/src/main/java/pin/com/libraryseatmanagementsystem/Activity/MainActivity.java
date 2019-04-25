package pin.com.libraryseatmanagementsystem.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pin.com.libraryseatmanagementsystem.Adapter.MyFragmentPagerAdapter;
import pin.com.libraryseatmanagementsystem.Bean.Seat;
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

    private List<Seat> seats;

    private NetworkConnection connection = NetworkConnection.getInstance();

    private ViewPager viewPager;
    private RadioGroup radioGroup;

    private List<Fragment> fragments;
    private MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seats = new ArrayList<>(336);

        initView();


    }

    private void initView() {
        viewPager = findViewById(R.id.fragment_vp);
        radioGroup = findViewById(R.id.radio_group);

        fragments = new ArrayList<>(3);
        fragments.add(SeatFragment.newInstance("", ""));
        fragments.add(StatusFragment.newInstance("", ""));
        fragments.add(PersonFragment.newInstance("", ""));

        FragmentManager fm = getSupportFragmentManager();
        adapter = new MyFragmentPagerAdapter(fm, fragments);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(pageChangeListener);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    public void getSeats() {
        Call<List<Seat>> seatCall = connection.getSeats();
        seatCall.enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                if (response.code() == 200) {
                    seats.clear();
                    seats.addAll(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "服务器错误哦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
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
}
