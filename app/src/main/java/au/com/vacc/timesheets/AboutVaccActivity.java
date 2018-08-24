package au.com.vacc.timesheets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


public class AboutVaccActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_vacc);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        final TextView textView = (TextView) findViewById(R.id.textView);
        final ImageView responsibilityImage = (ImageView) findViewById(R.id.image_responsibility);
        final Button findButton = (Button) findViewById(R.id.find_button);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://vacc.com.au/Fas-results/vacc"));
                startActivity(i);
            }
        });
        String[] options = getResources().getStringArray(R.array.about_vacc_array);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scrollView.smoothScrollTo(0, 0);
                switch (position)
                {
                    case 0:
                        textView.setText(getString(R.string.about_about_section));
                        responsibilityImage.setVisibility(View.GONE);
                        findButton.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        textView.setText(getString(R.string.about_history_section));
                        responsibilityImage.setVisibility(View.GONE);
                        findButton.setVisibility(View.GONE);
                        break;

                    case 2:
                        textView.setText(getString(R.string.about_management_section));
                        responsibilityImage.setVisibility(View.GONE);
                        findButton.setVisibility(View.GONE);
                        break;

                    case 3:
                        textView.setText(getString(R.string.about_executive_section));
                        responsibilityImage.setVisibility(View.GONE);
                        findButton.setVisibility(View.GONE);
                        break;

                    case 4:
                        textView.setText(getString(R.string.about_vision_section));
                        responsibilityImage.setVisibility(View.GONE);
                        findButton.setVisibility(View.GONE);
                        break;

                    case 5:
                        textView.setText(getString(R.string.about_responsibility_section));
                        responsibilityImage.setVisibility(View.VISIBLE);
                        findButton.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}