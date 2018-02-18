package cse_110.flashback_player;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yutong on 2/9/2018.
 * Added helper method 2/12/2018 (Duy)
 */
public class Main2Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SongPlayer songPlayer = new SongPlayer(this);

    public MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    public static Map<String,String[]> data;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        getData(); // ------------------------- Just Don't Delete This Line :) -----------------------


        FloatingActionButton toggle = (FloatingActionButton) findViewById(R.id.mode);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
//                startActivity(intent);
            }
        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // At the same time it passes songPlayer to each tab so they share one reference
            switch (position){
                case 0:
                    Tab1allsongs tab1 = new Tab1allsongs();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("songPlayer", songPlayer);
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    Tab2album tab2 = new Tab2album();
                    Bundle bundle2 = new Bundle();
                    bundle2.putParcelable("songPlayer", songPlayer);
                    tab2.setArguments(bundle2);
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "SONGS";
                case 1:
                    return "ALBUMS";
                default:
                    return null;
            }
        }
    }

    // --------------------------------- Here Is The Reason ------------------------------
    public void getData() {
        data = new HashMap<>();
        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        for (Field f : raw) {
            try {
                AssetFileDescriptor afd = this.getResources().openRawResourceFd(f.getInt(null));
                mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                String al = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String ti = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String ar = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String[] list = new String[3];
                list[0] = ti;list[1] = ar;list[2] = al;
                data.put(f.getName(),list);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
