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


    private ConstraintLayout personInfoLayout;
    private TextView name;
    private TextView account;
    private TextView checkOrder;
    private Button banButton;
    private Button lrButton;




    private Reader reader;


    private OnFragmentInteractionListener mListener;

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reader Parameter 1.

     * @return A new instance of fragment PersonFragment.
     */

    public static PersonFragment newInstance(Reader reader) {
        PersonFragment fragment = new PersonFragment();
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
        View thisView = inflater.inflate(R.layout.fragment_person, container, false);
        personInfoLayout = thisView.findViewById(R.id.person_info_layout);
        personInfoLayout.setOnClickListener(personInfo);
        name = thisView.findViewById(R.id.name);
        //name.setText("姓名：" + ((reader.getRname() == null)?"":reader.getRname()));
        account = thisView.findViewById(R.id.account);
        //account.setText("账号：" + reader.getAccount());
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



    //TODO:implement the click events
    private View.OnClickListener personInfo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (reader.getRid() >0) {
                mListener.updateReader();
            } else {
                Toast.makeText(getActivity(), "请登陆后再完成相应操作", Toast.LENGTH_SHORT).show();
            }
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
