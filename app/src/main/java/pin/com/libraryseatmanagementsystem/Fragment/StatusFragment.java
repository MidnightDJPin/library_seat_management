package pin.com.libraryseatmanagementsystem.Fragment;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pin.com.libraryseatmanagementsystem.Bean.Order;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Interface.OnFragmentInteractionListener;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatusFragment extends BaseFragment {
    private View view;
    private TextView hideText, seatID, startTime, endTime, timer;
    private ConstraintLayout usingLayout;
    private Button button;

    private Reader reader;
    private Order currentOrder;

    private boolean isCurrentOrder = false;
    private int pauseTime = 1800;
    private boolean start = false;
    private boolean pausing = false;
    private boolean overTime = false;

    private NetworkConnection connection = NetworkConnection.getInstance();

    private LocationManager locationManager;
    private LocationListener locationListener;

    private OnFragmentInteractionListener mListener;

    Handler handler = new Handler();
    Runnable timerThread = new Runnable() {
        @Override
        public void run() {
            if (pauseTime > 0 && pausing) {
                pauseTime--;
                timer.setText(pauseTime / 60 + ":" + ((pauseTime % 60 > 10)?"":"0") + pauseTime%60);
            } else if (pauseTime == 0 && !overTime) {
                Toast.makeText(getActivity(), "离开超时", Toast.LENGTH_SHORT);
                overTime = true;
            }
            handler.postDelayed(timerThread, 1000);
        }
    };

    public StatusFragment() {
        // Required empty public constructor
    }


    public static StatusFragment newInstance(Reader reader) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putSerializable("reader", reader);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reader = (Reader)getArguments().getSerializable("reader");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status, container, false);
        hideText = view.findViewById(R.id.hide_text);
        usingLayout = view.findViewById(R.id.using_layout);
        seatID = view.findViewById(R.id.seat_status);
        startTime = view.findViewById(R.id.start_time_status);
        endTime = view.findViewById(R.id.end_time_status);
        button = view.findViewById(R.id.button_status);
        button.setOnClickListener(clickListener);
        button.setOnLongClickListener(longClickListener);
        timer = view.findViewById(R.id.timer);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (reader.getRid() > 0) {
            getCurrentOrder();
        }
    }

    public void getCurrentOrder() {
        Call<List<Order>> orderCall = connection.getOrders(reader.getRid() + "", "0");
        orderCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.code() == 200) {
                    List<Order> orders = response.body();
                    if (orders.size() > 0) {
                        Date now = new Date();
                        for (Order order : orders) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date startDate = sdf.parse(order.getStarttime());
                                Date endDate = sdf.parse(order.getEndtime());
                                if (now.getTime()>=startDate.getTime() && now.getTime() <= endDate.getTime()) {
                                    currentOrder = order;
                                    hideText.setVisibility(View.INVISIBLE);
                                    seatID.setText("座位号：" + currentOrder.getSid());
                                    startTime.setText("开始时间：" + currentOrder.getStarttime());
                                    endTime.setText("结束时间：" + currentOrder.getEndtime());
                                    timer.setText(pauseTime / 60 + ":" + ((pauseTime % 60 > 10)?"":"0") + pauseTime % 60);
                                    if (!pausing) timer.setVisibility(View.INVISIBLE);
                                    usingLayout.setVisibility(View.VISIBLE);
                                    isCurrentOrder = true;
                                    break;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!isCurrentOrder) {
                        currentOrder = null;
                        usingLayout.setVisibility(View.INVISIBLE);
                        hideText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }

    @Override
    public void refreshReader(Reader newReader) {
        reader = newReader;
        getCurrentOrder();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentOrder();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!start) {
                Toast.makeText(getActivity(), "开始使用", Toast.LENGTH_SHORT).show();
                start = true;
                button.setText("暂时离开");
                //TODO:update seat table

            } else if (!pausing) {
                Toast.makeText(getActivity(), "暂时离开", Toast.LENGTH_SHORT).show();
                button.setText("继续使用");
                pausing = !pausing;
                handler.post(timerThread);
                timer.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "继续使用", Toast.LENGTH_SHORT).show();
                button.setText("暂时离开");
                pausing = !pausing;
                handler.post(timerThread);
                timer.setVisibility(View.INVISIBLE);
            }
        }
    };
    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            handler.removeCallbacks(timerThread);
            pauseTime = 1800;
            Toast.makeText(getActivity(), "结束使用", Toast.LENGTH_SHORT).show();

            return true;
        }
    };
}
