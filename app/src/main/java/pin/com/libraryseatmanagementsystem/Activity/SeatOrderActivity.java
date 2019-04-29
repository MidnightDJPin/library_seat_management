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

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pin.com.libraryseatmanagementsystem.Adapter.MyRecyclerViewAdapter;
import pin.com.libraryseatmanagementsystem.Bean.Order;
import pin.com.libraryseatmanagementsystem.Bean.OrderInfo;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Bean.Seat;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import pin.com.libraryseatmanagementsystem.subclass.RangeTimePickerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeatOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView seatId, seatState, chooseDate, startTime, endTime;
    private Button orderButton;
    private RecyclerView orderView;
    private MyRecyclerViewAdapter adapter;

    private Seat seat;
    private Reader reader;
    private List<OrderInfo> orders = new ArrayList<>();

    boolean startSet = false;
    boolean endSet = false;


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
                if (reader.isAdmin())
                    account.setVisibility(View.VISIBLE);
            }
        };
        orderView.setLayoutManager(new LinearLayoutManager(this));
        orderView.setAdapter(adapter);

        getOrderInfo();
    }

    void initView() {
        seatId = findViewById(R.id.seat_id);
        seatId.setText("座位号：" + seat.getSid());
        seatState = findViewById(R.id.seat_state);
        seatState.setText("状态：" + seat.getSstate());
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


    void getOrderInfo() {
        Call<List<OrderInfo>> orderCall = connection.getSeatOrders("0", seat.getSid()+"");
        orderCall.enqueue(new Callback<List<OrderInfo>>() {
            @Override
            public void onResponse(Call<List<OrderInfo>> call, Response<List<OrderInfo>> response) {
                if (response.code()==200) {
                    orders.clear();
                    orders.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else if (response.code() == 403) {
                    Toast.makeText(SeatOrderActivity.this, "存在时间冲突", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SeatOrderActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderInfo>> call, Throwable t) {
                Toast.makeText(SeatOrderActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void sendOrder() {
        Order order = new Order();
        order.setRid(reader.getRid());
        order.setSid(seat.getSid());
        order.setStarttime(chooseDate.getText().toString() + " " + startTime.getText().toString());
        order.setEndtime(chooseDate.getText().toString() + " " + endTime.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(order);
        Call<Order> orderCall = connection.sendOrder(json);
        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.code() == 201) {
                    Toast.makeText(SeatOrderActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                    getOrderInfo();
                } else {
                    Toast.makeText(SeatOrderActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(SeatOrderActivity.this, "网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Calendar now = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.choose_date:
                DatePickerDialog datePicker=new DatePickerDialog(SeatOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Toast.makeText(SeatOrderActivity.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        chooseDate.setText(sdf.format(date));
                    }
                }, 2019, 0, 1);
                DatePicker dp = datePicker.getDatePicker();
                Calendar calendar = Calendar.getInstance();
                dp.setMinDate(calendar.getTime().getTime());
                calendar.add(Calendar.DAY_OF_MONTH,6);
                dp.setMaxDate(calendar.getTime().getTime());
                datePicker.show();
                break;

            case R.id.choose_start_time:
                 RangeTimePickerDialog timeStart=new  RangeTimePickerDialog(SeatOrderActivity.this, new  RangeTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(SeatOrderActivity.this, hourOfDay+"hour "+minute+"minute", Toast.LENGTH_SHORT).show();
                        Date date = new Date();
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String s = sdf.format(date);
                        if (endSet && s.compareTo(endTime.getText().toString()) > 0) {
                            Toast.makeText(SeatOrderActivity.this, "开始时间不得晚于结束时间", Toast.LENGTH_SHORT).show();
                            startSet = false;
                        } else {
                            startTime.setText(s);
                            startSet = true;
                        }

                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
                 timeStart.show();
                break;
            case R.id.choose_end_time:
                RangeTimePickerDialog timeEnd=new  RangeTimePickerDialog(SeatOrderActivity.this, new  RangeTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(SeatOrderActivity.this, hourOfDay+"hour "+minute+"minute", Toast.LENGTH_SHORT).show();
                        Date date = new Date();
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String s = sdf.format(date);
                        if (startSet && s.compareTo(startTime.getText().toString()) < 0) {
                            Toast.makeText(SeatOrderActivity.this, "开始时间不得晚于结束时间", Toast.LENGTH_SHORT).show();
                            endSet = false;
                        } else {
                            endTime.setText(s);
                            endSet = true;
                        }

                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
                timeEnd.show();
                break;
            case R.id.book:
                if (reader.isRstate()) {
                    if (startTime.getText().toString().compareTo("22:15") >= 0) {
                        Toast.makeText(SeatOrderActivity.this, "22:15后无需进行预约", Toast.LENGTH_SHORT).show();
                    } else {
                        sendOrder();
                    }
                } else {
                    Toast.makeText(SeatOrderActivity.this, "封禁中，无法预约座位", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
