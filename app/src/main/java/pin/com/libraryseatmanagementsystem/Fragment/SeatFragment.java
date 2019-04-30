package pin.com.libraryseatmanagementsystem.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pin.com.libraryseatmanagementsystem.Activity.SeatOrderActivity;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Bean.Seat;
import pin.com.libraryseatmanagementsystem.Interface.OnFragmentInteractionListener;
import pin.com.libraryseatmanagementsystem.Net.NetworkConnection;
import pin.com.libraryseatmanagementsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeatFragment extends BaseFragment {
    private Reader reader;
    private List<Seat> seats = new ArrayList<>(336);

    private NetworkConnection connection = NetworkConnection.getInstance();

    private OnFragmentInteractionListener mListener;

    ViewGroup HV_layout;
    View fragmentView;
    private int[] seatsInt = {-1,  1,  2,  3,   7,  8,  9, 0,  13, 14, 15, 0,  19, 20, 0,  23, 24, 0, 157,158, 0, 161,162, 0, 165,166, 0, 181, 182, 0, 185, 186, 0, 189,190,191, 0, 195,196,198, 201,202,203,-1,
            4,  5,  6,  10, 11, 12, 0,  16, 17, 18, 0,  21, 22, 0,  25, 26, 0, 159,160, 0, 163,164, 0, 167,168, 0, 183, 184, 0, 187, 188, 0, 192,193,194, 0, 198,199,200, 204,205,206,-1,
            0,  0,  0,   0,  0,  0, 0,   0,  0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,   0, 0,   0,   0, 0,   0,  0,  0, 0,   0,  0,  0,   0,  0,  0,-1,
            27, 28, 29,  33, 34, 35, 0,  39, 40, 41, 0,  45, 46, 0,  49, 50, 0, 169,170, 0, 173,174, 0, 177,178, 0, 207, 208, 0, 211, 212, 0, 215,216,217, 0, 221,222,223, 227,228,229,-1,
            30, 31, 32,  36, 37, 38, 0,  42, 43, 44, 0,  47, 48, 0,  51, 52, 0, 171,172, 0, 175,176, 0, 179,180, 0, 209, 210, 0, 213, 214, 0, 218,219,220, 0, 224,225,226, 230,231,232,-1,
            0,  0,  0,   0,  0,  0, 0,   0,  0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,   0, 0,   0,   0, 0,   0,  0,  0, 0,   0,  0,  0,   0,  0,  0,-1,
            53, 54, 55,  59, 60, 61, 0,  65, 66, 67, 0,  71, 72, 0,  75, 76, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 233, 234, 0, 237, 238, 0, 241,242,243, 0, 247,248,249, 253,254,255,-1,
            56, 57, 58,  62, 63, 64, 0,  68, 69, 70, 0,  73, 74, 0,  77, 78, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 235, 236, 0, 239, 240, 0, 244,245,246, 0, 250,251,252, 256,257,258,-1,
            0,  0,  0,   0,  0,  0, 0,   0,  0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,   0, 0,   0,   0, 0,   0,  0,  0, 0,   0,  0,  0,   0,  0,  0,-1,
            79, 80, 81,  85, 86, 87, 0,  91, 92, 93, 0,  97, 98, 0, 101,102, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 259, 260, 0, 263, 264, 0, 267,268,269, 0, 273,274,275, 279,280,281,-1,
            80, 83, 84,  88, 89, 90, 0,  94, 95, 96, 0,  99,100, 0, 103,104, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 261, 262, 0, 265, 266, 0, 270,271,272, 0, 276,277,278, 282,283,284,-1,
            0,  0,  0,   0,  0,  0, 0,   0,  0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,   0, 0,   0,   0, 0,   0,  0,  0, 0,   0,  0,  0,   0,  0,  0,-1,
            105,106,107, 111,112,113, 0, 117,118,119, 0, 123,124, 0, 127,128, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 285, 286, 0, 289, 290, 0, 293,294,295, 0, 299,300,301, 305,306,307,-1,
            108,109,110, 114,115,116, 0, 120,121,122, 0, 125,126, 0, 129,130, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 287, 288, 0, 291, 292, 0, 296,297,298, 0, 302,303,304, 308,309,310,-1,
            0,  0,  0,   0,  0,  0, 0,   0,  0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0,   0,   0, 0,   0,   0, 0,   0,  0,  0, 0,   0,  0,  0,   0,  0,  0,-1,
            131,132,133, 137,138,139, 0, 143,144,145, 0, 149,150, 0, 153,154, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 311, 312, 0, 315, 316, 0, 319,320,321, 0, 325,326,327, 331,332,333,-1,
            134,135,136, 140,141,142, 0, 146,147,148, 0, 151,152, 0, 155,156, 0,   0,  0, 0,   0,  0, 0,   0,  0, 0, 313, 314, 0, 317, 318, 0, 322,323,324, 0, 328,329,330, 334,335,336,-1
    };
    private int[] seatIndex = new int[336];
    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;

    int STATUS_DISABLE = 0;
    int STATUS_AVAILABLE = 1;
    int STATUS_USING = 2;

    public SeatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reader Parameter 1.
     * @return A new instance of fragment SeatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeatFragment newInstance(Reader reader) {
        SeatFragment fragment = new SeatFragment();
        Bundle args = new Bundle();
        args.putSerializable("reader", reader);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reader = (Reader) getArguments().getSerializable("reader");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentView = inflater.inflate(R.layout.fragment_seat, container, false);
        HV_layout = fragmentView.findViewById(R.id.layoutSeat);

        LinearLayout layoutSeat = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        HV_layout.addView(layoutSeat);

        LinearLayout layout = null;

        for (int i = 0; i < seatsInt.length; i++) {
            if (seatsInt[i] == -1) {
                layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seatsInt[i] > 0) {
                TextView view = new TextView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(seatsInt[i]);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_available);
                view.setText(seatsInt[i] + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                //xx号座位在seatViewLis中的索引存放在seatIndex中 获取TextView的方法：seatViewList.get(seatIndex[rid - 1])
                seatIndex[seatsInt[i] - 1] = seatViewList.size() - 1;

                view.setOnClickListener(onSeatClickListener);
            } else if (seatsInt[i] == 0) {
                TextView view = new TextView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
            HV_layout.setVisibility(View.INVISIBLE);
        }

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (reader.getRid() > 0)
            setSeat();
    }

    private void setSeat() {
        Call<List<Seat>> seatCall = connection.getSeats();
        seatCall.enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                if (response.code() == 200) {
                    seats.clear();
                    seats.addAll(response.body());
                    for (int i = 0; i < seats.size(); i++) {
                        int sid = seats.get(i).getSid();
                        int sstate = seats.get(i).getSstate();
                        TextView tv = seatViewList.get(seatIndex[sid - 1]);
                        if (sstate == 1) {
                            tv.setBackgroundResource(R.drawable.ic_seats_available);
                            tv.setTextColor(Color.BLACK);
                            tv.setTag(STATUS_AVAILABLE);
                        } else {
                            tv.setTextColor(Color.WHITE);
                            if (sstate == 0) {
                                tv.setBackgroundResource(R.drawable.ic_seats_disable);
                                tv.setTag(STATUS_DISABLE);
                            } else {
                                tv.setBackgroundResource(R.drawable.ic_seats_using);
                                tv.setTag(STATUS_USING);
                            }
                        }
                    }
                    HV_layout.setVisibility(View.VISIBLE);
                    ConstraintLayout loading = fragmentView.findViewById(R.id.loading_layout);
                    loading.setVisibility(View.INVISIBLE);
                    LinearLayout seatType = fragmentView.findViewById(R.id.seat_type);
                    seatType.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {

            }
        });
    }

    @Override
    public void refreshReader(Reader newReader) {
        reader = newReader;
        setSeat();
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
        setSeat();
    }

    public View.OnClickListener onSeatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (reader.getRid() == 0) {
                Toast.makeText(getActivity(), "请先登陆后再进行相应操作", Toast.LENGTH_SHORT).show();
            } else if (seats.get(vId - 1).getSstate() == STATUS_DISABLE) {
                Toast.makeText(getActivity(), "该座位不可用", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), SeatOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("reader", reader);
                bundle.putSerializable("seat", seats.get(vId - 1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };


}
