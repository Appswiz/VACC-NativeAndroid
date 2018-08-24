package au.com.vacc.timesheets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;

import au.com.vacc.timesheets.adapter.AutomativeCareersAdapter;
import au.com.vacc.timesheets.model.AutomativeCareer;
import au.com.vacc.timesheets.utils.Utils;


public class AutomativeCareersActivity extends AppCompatActivity {
    private ArrayList<AutomativeCareer> list = new ArrayList<AutomativeCareer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automative_careers);
        getData(Utils.convertStringToJsonNode(this, R.raw.automative_careers));
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PDFViewerActivity.class);
                intent.putExtra("pdfFileName",list.get(position).getFileName());
                intent.putExtra("isCareers",true);
                startActivity(intent);
            }
        });
        listView.setAdapter(new AutomativeCareersAdapter(this, 0, list));
    }

    private void getData(JsonNode jsonNode) {
        Iterator<JsonNode> listIterator = jsonNode.getElements();
        while (listIterator.hasNext()) {
            JsonNode parentNode = listIterator.next();
            AutomativeCareer item = new AutomativeCareer(parentNode);
            list.add(item);
        }
    }
}