package pin.com.libraryseatmanagementsystem.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pin.com.libraryseatmanagementsystem.Activity.LoginActivity;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Interface.OnFragmentCallbackListener;
import pin.com.libraryseatmanagementsystem.Interface.OnFragmentInteractionListener;
import pin.com.libraryseatmanagementsystem.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends BaseFragment implements OnFragmentCallbackListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ConstraintLayout personInfoLayout;
    private TextView name;
    private TextView account;
    private TextView checkOrder;
    private Button banButton;
    private Button lrButton;


    // TODO: Rename and change types of parameters

    private Reader reader;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reader Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(Reader reader, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putSerializable("reader", reader);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reader = (Reader) getArguments().getSerializable("reader");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_person, container, false);
        personInfoLayout = thisView.findViewById(R.id.person_info_layout);
        personInfoLayout.setOnClickListener(personInfo);
        name = thisView.findViewById(R.id.name);
        name.setText("姓名：" + ((reader.getRname() == null)?"":reader.getRname()));
        account = thisView.findViewById(R.id.account);
        account.setText("账号：" + reader.getAccount());
        checkOrder = thisView.findViewById(R.id.check_order);
        banButton = thisView.findViewById(R.id.banButton);
        banButton.setVisibility((reader.isAdmin())?View.VISIBLE:View.INVISIBLE);
        banButton.setOnClickListener(ban);
        lrButton = thisView.findViewById(R.id.lr_button);
        Resources resources = getResources();
        if (reader.getRid() == 0) {
            lrButton.setBackground(resources.getDrawable(R.drawable.button_shape3));
            lrButton.setText("登陆");
        } else {
            lrButton.setBackground(resources.getDrawable(R.drawable.button_shape4));
            lrButton.setText("退出账号");
        }
        lrButton.setOnClickListener(lr);
        return thisView;
    }

    @Override
    public void refreshReader(Reader newReader) {
        reader = newReader;
        name.setText("姓名：" + ((reader.getRname() == null)?"":reader.getRname()));
        account.setText("账号：" + reader.getAccount());
        banButton.setVisibility((reader.isAdmin())?View.VISIBLE:View.INVISIBLE);
        Resources resources = getResources();
        if (reader.getRid() == 0) {
            lrButton.setBackground(resources.getDrawable(R.drawable.button_shape3));
            lrButton.setText("登陆");
        } else {
            lrButton.setBackground(resources.getDrawable(R.drawable.button_shape4));
            lrButton.setText("退出账号");
        }
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



    private View.OnClickListener personInfo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "个人信息", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener ban = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "封禁管理", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener lr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.login();
        }
    };
}
