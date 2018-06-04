package com.myrp.a16023022.p07_smsretriever;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSecond extends Fragment {

    Button btnAddText;
    TextView tvFrag2;
    EditText etFrag2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container,false);

        etFrag2 = (EditText) view.findViewById(R.id.etFrag2);
        tvFrag2 = (TextView) view.findViewById(R.id.tvFrag2);
        btnAddText = (Button) view.findViewById(R.id.btnAddTextFrag2);

        btnAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvFrag2.getText().toString() + "\n" + "New Data F2";
                tvFrag2.setText(data);
            }
        });

        return view;
    }

}
