package au.com.vacc.timesheets.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.com.vacc.timesheets.MembersActivity;
import au.com.vacc.timesheets.R;
import au.com.vacc.timesheets.utils.Utils;

/**
 * Created by Juhachi on 11/10/2017.
 */

public class AboutFragment extends Fragment {
    private Activity act;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        act = getActivity();
        ((TextView) rootView.findViewById(R.id.tv_version)).setText(Utils.getAppVersionName(act));
        TextView tvTimestamps = (TextView) rootView.findViewById(R.id.tv_timestamps);
        String timeStamp = "<u>Time Stamps</u>";
        tvTimestamps.setText(Html.fromHtml(timeStamp));
        tvTimestamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MembersActivity) act).showTimeStamp();
            }
        });

        rootView.findViewById(R.id.iv_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.onBackPressed();
            }
        });
        return rootView;
    }
}