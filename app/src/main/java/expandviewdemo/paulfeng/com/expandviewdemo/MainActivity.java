package expandviewdemo.paulfeng.com.expandviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.paulfeng.widget.expandview.ExpandView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandView ev;
    private TextView tv;
    private HashMap<String, List<String>> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ev = (ExpandView) findViewById(R.id.ev);
        tv = (TextView) findViewById(R.id.tv);
        map = initData();
        ev.setTitleAndContent(map);
        ev.setOnChooseListener(new ExpandView.OnChooseListener() {
            @Override
            public void onChoose(List<TextView> textLists, int pos) {
                Toast.makeText(MainActivity.this, textLists.get(pos).getText() + "is Choose", Toast.LENGTH_LONG).show();
                tv.setText("Content" + textLists.get(pos).getText());
            }
        });

        ev.setOnFoldListener(new ExpandView.OnFoldListener() {
            @Override
            public void onFold() {

            }
        });
    }

    private HashMap<String,List<String>> initData() {
        LinkedHashMap<String, List<String>> data = new LinkedHashMap<>();
        for(int i = 0; i < 4; i++) {
            ArrayList<String> list = new ArrayList<>();
            for(int j = 0; j < 7; j++) {
                list.add("A" + i + "B" + j);
            }
            data.put(list.get(0), list);
        }

        return data;
    }
}
