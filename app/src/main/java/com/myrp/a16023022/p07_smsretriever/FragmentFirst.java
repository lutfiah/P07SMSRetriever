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
public class FragmentFirst extends Fragment {


    Button btnRetrieve1;
    TextView tvFrag1, tvDisplay1;
    EditText etFrag1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        etFrag1 = (EditText) view.findViewById(R.id.etFrag1);
        tvFrag1 = (TextView) view.findViewById(R.id.tvFrag1);
        tvDisplay1 = (TextView) view.findViewById(R.id.tvDisplay1);
        btnRetrieve1 = (Button) view.findViewById(R.id.btnAddTextFrag1);

        btnRetrieve1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regexStr = "^[0-9]*$";
                String data = etFrag1.getText().toString();
                if(data.trim().matches(regexStr))
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
                    String[] filterArgs = {"%" + data + "%"};
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
                    tvDisplay1.setText(smsBody);
                }
                else{
                    tvDisplay1.setText("Please enter only numbers");
                }
            }
        });

        return view;
    }

}
