package pin.com.libraryseatmanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pin.com.libraryseatmanagementsystem.Adapter.MyRecyclerViewAdapter;
import pin.com.libraryseatmanagementsystem.Bean.Order;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderActivity extends AppCompatActivity {

    private Reader reader;
    private List<Order> openOrders = new ArrayList<>();
    private List<Order> closeOrders = new ArrayList<>();

    private RecyclerView openView;
    private MyRecyclerViewAdapter openViewAdapter;
    private RecyclerView closeView;
    private MyRecyclerViewAdapter closeViewAdapter;


    private NetworkConnection connection = NetworkConnection.getInstance();
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        reader = (Reader)bundle.getSerializable("reader");

        openView = findViewById(R.id.open_view);
        closeView = findViewById(R.id.close_view);

        setAdapter();
        openView.setLayoutManager(new LinearLayoutManager(this));
        openView.setAdapter(openViewAdapter);
        closeView.setLayoutManager(new LinearLayoutManager(this));
        closeView.setAdapter(closeViewAdapter);

        getMyOrder();
    }

    public void setAdapter() {
      openViewAdapter = new MyRecyclerViewAdapter<Order>(MyOrderActivity.this, R.layout.order_layout1, openOrders) {
          @Override
          public void convert(MyViewHolder holder, final Order order) {
              TextView seatNumber = holder.getView(R.id.seat_number);
              seatNumber.setText("座位号：" + order.getSid());
              TextView startTime = holder.getView(R.id.start_time);
              startTime.setText("开始时间：" + order.getStarttime().substring(0, 16));
              TextView endTime = holder.getView(R.id.end_time);
              endTime.setText("结束时间：" + order.getEndtime().substring(0, 16));
              Button cancelButton = holder.getView(R.id.cancel_button);
              cancelButton.setVisibility(View.VISIBLE);
              cancelButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Call<Order> orderCall = connection.deleteOrder(order.getOid() + "");
                      orderCall.enqueue(new Callback<Order>() {
                          @Override
                          public void onResponse(Call<Order> call, Response<Order> response) {
                              if (response.code()==200) {
                                  Toast.makeText(MyOrderActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                  openOrders.remove(order);
                                  openViewAdapter.notifyDataSetChanged();
                              } else {
                                  Toast.makeText(MyOrderActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                              }
                          }

                          @Override
                          public void onFailure(Call<Order> call, Throwable t) {

                          }
                      });
                  }
              });
          }
      };

      closeViewAdapter = new MyRecyclerViewAdapter<Order>(MyOrderActivity.this, R.layout.order_layout1, closeOrders) {
          @Override
          public void convert(MyViewHolder holder, Order order) {
              TextView seatNumber = holder.getView(R.id.seat_number);
              seatNumber.setText("座位号：" + order.getSid());
              TextView startTime = holder.getView(R.id.start_time);
              startTime.setText("开始时间：" + order.getStarttime().substring(0, 16));
              TextView endTime = holder.getView(R.id.end_time);
              endTime.setText("结束时间：" + order.getEndtime().substring(0, 16));
          }
      };
    }

    public void getMyOrder() {
        String sid = "0";
        Call<List<Order>> orderCall = connection.getOrders(reader.getRid() +"", sid);
        orderCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.code() == 200) {
                    openOrders.clear();
                    closeOrders.clear();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                    for (Order order:response.body()) {
                        if (order.getStarttime().substring(0,16).compareTo(df.format(new Date()).substring(0,16)) >= 0) {
                            openOrders.add(order);
                        } else {
                            closeOrders.add(order);
                        }
                    }
                    Collections.reverse(openOrders);
                    openViewAdapter.notifyDataSetChanged();
                    closeViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }
}
