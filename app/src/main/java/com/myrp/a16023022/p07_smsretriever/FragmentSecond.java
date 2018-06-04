package com.myrp.a16023022.p07_smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
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

    Button btnRetrieve2;
    TextView tvFrag2, tvDisplay2;
    EditText etFrag2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container,false);

        etFrag2 = (EditText) view.findViewById(R.id.etFrag2);
        tvDisplay2 = (TextView) view.findViewById(R.id.tvDisplay2);
        tvFrag2 = (TextView) view.findViewById(R.id.tvFrag2);
        btnRetrieve2 = (Button) view.findViewById(R.id.btnAddTextFrag2);

        btnRetrieve2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regexStr = "^[a-zA-Z ]+$";
                String data2 = etFrag2.getText().toString();
                if(data2.trim().matches(regexStr))
                {
                    int permissionCheck = PermissionChecker.checkSelfPermission
                            (getActivity(), Manifest.permission.READ_SMS);

                    if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_SMS}, 0);
                        // stops the action from proceeding further as permission not
                        //  granted yet
                        return;
                    }

                    Uri uri = Uri.parse("content://sms");
                    // The columns we want
                    //  date is when the message took place
                    //  address is the number of the other party
                    //  body is the message content
                    //  type 1 is received, type 2 sent
                    String[] reqCols = new String[]{"date", "address", "body", "type"};

                    // Get Content Resolver object from which to
                    //  query the content provider
                    ContentResolver cr = getActivity().getContentResolver() ;
                    // The filter String
                    String filter="body LIKE ? AND body LIKE ?";
                    // The matches for the ?
                    String[] filterArgs = {"%" + data2 + "%"};
                    // Fetch SMS Message from Built-in Content Provider
                    Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                    String smsBody = "";
                    if (cursor.moveToFirst()) {
                        do {
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat
                                    .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            if (type.equalsIgnoreCase("1")) {
                                type = "Inbox:";
                            } else {
                                type = "Sent:";
                            }
                            smsBody += type + " " + address + "\n at " + date
                                    + "\n\"" + body + "\"\n\n";
                        } while (cursor.moveToNext());
                    }
                    tvDisplay2.setText(smsBody);
                }
                else{
                    tvDisplay2.setText("Please enter only words");
                }
            }
        });

        return view;
    }

}
