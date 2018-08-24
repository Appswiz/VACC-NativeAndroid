package au.com.vacc.timesheets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import me.relex.circleindicator.CircleIndicator;


public class VetInSchoolActivity extends AppCompatActivity {
    int [] layouts = {R.layout.item_vet_in_school_1, R.layout.item_vet_in_school_2};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_in_school);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        final VetInSchoolAdapter adapter = new VetInSchoolAdapter(this, layouts);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    public class VetInSchoolAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        private int[] list;
        public VetInSchoolAdapter(Context context, int[] list) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(list[position], container, false);
            view.findViewById(R.id.call_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "0398291130";
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_DIAL, call);
                    startActivity(surf);
                }
            });
            ((ViewPager) container).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ScrollView) object);

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}