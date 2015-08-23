package net.zsrd.zsdaily;

import java.util.ArrayList;

import net.zsrd.zsdaily.drag.DateAdapter;
import net.zsrd.zsdaily.drag.DragGrid;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

    /** GridView. */
    private DragGrid gridView;
    TranslateAnimation left, right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        net.zsrd.zsdaily.drag.Configure.init(this);
        gridView = (DragGrid) findViewById(R.id.gridview);
        ArrayList<String> l = new ArrayList<String>();
        for (int i = 0; i < 9; i++) {
            l.add("" + i);
        }
        DateAdapter adapter = new DateAdapter(MainActivity.this, l);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "click it!", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
