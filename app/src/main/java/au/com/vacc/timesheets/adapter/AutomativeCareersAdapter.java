package au.com.vacc.timesheets.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import au.com.vacc.timesheets.R;
import au.com.vacc.timesheets.model.AutomativeCareer;

public class AutomativeCareersAdapter extends ArrayAdapter<AutomativeCareer> {
    private Context mContext;
    private ArrayList<AutomativeCareer> mList;
    private LayoutInflater layoutInflater;

    public AutomativeCareersAdapter(Context context, int resourceId, ArrayList<AutomativeCareer> list) {
        super(context, resourceId, list);
        mContext = context;
        mList = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_automative_career, null);
            convertView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            holder = new ViewHolder();
            holder.textView  = (TextView) convertView.findViewById(R.id.textView);
            holder.holder = (RelativeLayout) convertView.findViewById(R.id.holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AutomativeCareer item = mList.get(position);
        holder.textView.setText(item.getName());
        int imageId = mContext.getResources().getIdentifier(item.getBackground(), "mipmap", mContext.getPackageName());

        if (imageId > 0) {
            holder.holder.setBackgroundResource(imageId);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView textView;
        private RelativeLayout holder;
    }

}
