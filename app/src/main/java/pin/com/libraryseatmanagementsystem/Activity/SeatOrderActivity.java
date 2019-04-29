package pin.com.libraryseatmanagementsystem.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pin.com.libraryseatmanagementsystem.Adapter.MyRecyclerViewAdapter;
import pin.com.libraryseatmanagementsystem.Bean.OrderInfo;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Bean.Seat;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import pin.com.libraryseatmanagementsystem.subclass.RangeTimePickerDialog;

public class SeatOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView seatId, seatState, chooseDate, startTime, endTime;
    private Button orderButton;
    private RecyclerView orderView;
    private MyRecyclerViewAdapter adapter;

    private Seat seat;
    private Reader reader;
    private List<OrderInfo> orders = new ArrayList<>();


    private NetworkConnection connection = NetworkConnection.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_order);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        seat = (Seat)bundle.getSerializable("seat");
        reader = (Reader)bundle.getSerializable("reader");
        initView();

        adapter = new MyRecyclerViewAdapter<OrderInfo>(SeatOrderActivity.this, R.layout.order_layout2, orders) {
            @Override
            public void convert(MyViewHolder holder, OrderInfo orderInfo) {
                TextView startTime = holder.getView(R.id.time_start);
                startTime.setText("开始时间：" + orderInfo.getStarttime());
                TextView endTime = holder.getView(R.id.time_end);
                endTime.setText("结束时间：" + orderInfo.getEndtime());
                TextView account = holder.getView(R.id.reader_account);
                account.setText("预约账号：" + orderInfo.getAccount());
            }
        };
        orderView.setLayoutManager(new LinearLayoutManager(this));
        orderView.setAdapter(adapter);
    }

    void initView() {
        seatId = findViewById(R.id.seat_id);
        seatState = findViewById(R.id.seat_state);
        chooseDate = findViewById(R.id.choose_date);
        chooseDate.setOnClickListener(this);
        startTime = findViewById(R.id.choose_start_time);
        startTime.setOnClickListener(this);
        endTime = findViewById(R.id.choose_end_time);
        endTime.setOnClickListener(this);
        orderButton = findViewById(R.id.book);
        orderButton.setOnClickListener(this);
        orderView = findViewById(R.id.order_view);
    }

    //TODO:add getOrderInfo method
    void getOrderInfo() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_date:
                DatePickerDialog datePicker=new DatePickerDialog(SeatOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Toast.makeText(SeatOrderActivity.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
                    }
                }, 2019, 0, 1);
                DatePicker dp = datePicker.getDatePicker();
                Calendar calendar = Calendar.getInstance();
                dp.setMinDate(calendar.getTime().getTime());
                calendar.add(Calendar.WEEK_OF_YEAR,1);
                dp.setMaxDate(calendar.getTime().getTime());
                datePicker.show();
                break;

            case R.id.choose_start_time:
                 RangeTimePickerDialog timeStart=new  RangeTimePickerDialog(SeatOrderActivity.this, new  RangeTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        Toast.makeText(SeatOrderActivity.this, hourOfDay+"hour "+minute+"minute", Toast.LENGTH_SHORT).show();
                    }
                }, 18, 25, true);
                 timeStart.show();
                break;
            case R.id.choose_end_time:
                RangeTimePickerDialog timeEnd=new  RangeTimePickerDialog(SeatOrderActivity.this, new  RangeTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        Toast.makeText(SeatOrderActivity.this, hourOfDay+"hour "+minute+"minute", Toast.LENGTH_SHORT).show();
                    }
                }, 18, 25, true);
                timeEnd.show();
                break;
        }

    }
}
