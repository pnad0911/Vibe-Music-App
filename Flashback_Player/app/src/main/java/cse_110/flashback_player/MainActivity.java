package cse_110.flashback_player;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String[] myStringArray = new String[10];
        myStringArray[0] = "AAA";
        myStringArray[1] = "BBB";
        myStringArray[2] = "CCC";
        myStringArray[3] = "DDD";
        myStringArray[4] = "EEE";
        myStringArray[5] = "AAA";
        myStringArray[6] = "BBB";
        myStringArray[7] = "CCC";
        myStringArray[8] = "DDD";
        myStringArray[9] = "EEE";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.song_list_row, myStringArray);

        ListView listView = (ListView) findViewById(R.id.song_list);
        listView.setAdapter(adapter);
    }

}
