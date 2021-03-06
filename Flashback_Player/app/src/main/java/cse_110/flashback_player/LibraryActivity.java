package cse_110.flashback_player;


import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


/**
 * Created by Yutong on 2/9/2018.
 * Added helper method 2/12/2018 (Duy)
 */
public class LibraryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    public static Map<String, String[]> data;
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static Location loc;
    private LocationReadyCallback locationCallback;
    private LocationRequest mLocationRequest;
    public static OffsetDateTime setTime;
    public static boolean usingCurrentTime = true;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    public static Context contextOfApplication;
    public static SongList songListGen; public static List<Song> songList;
    private static TabSongs tab1; private static TabAlbum tab2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WUTFACE", "started LibraryActivity");
        setTime = OffsetDateTime.now();
        setContentView(R.layout.activity_main2);

        contextOfApplication = getApplicationContext();
        tab1 = new TabSongs(); tab2 = new TabAlbum();
        SharedPreferences sharedPreferences = getSharedPreferences("mode", MODE_PRIVATE);
        if (sharedPreferences.getString("current", "").equalsIgnoreCase("flashback")) {
            Intent intent = new Intent(this, VibeActivity.class);
            startActivity(intent);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current", "normal");
        editor.apply();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LinearLayout container = (LinearLayout) findViewById(R.id.appbar);
                AnimationDrawable anim = (AnimationDrawable) container.getBackground();
                anim.setEnterFadeDuration(6000);
                anim.setExitFadeDuration(5000);
                anim.start();
            }
        });
        thread.start();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        startLocationUpdates();


        setLocationReadyCallback(new LocationReadyCallback() {
            @Override
            public void locationReady() {
                getLocation();
            }
        });

        FloatingActionButton toggle = (FloatingActionButton) findViewById(R.id.mode);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, VibeActivity.class);
                songPlayer.pause();
                startActivityForResult(intent, 1);
            }
        });

        songListGen = new SongList(this);
        songListGen.reg(tab1); songListGen.reg(tab2);
        songList = songListGen.getAllsong();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setHint("Enter URL here");
        alert.setTitle("DOWNLOADS");
        alert.setMessage("Enter Your URL here").setCancelable(false);
        alert.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String url = edittext.getText().toString();
                SongDownloadHelper songDownloadHelper2 = new SongDownloadHelper(url,songListGen,LibraryActivity.this);
                songDownloadHelper2.startDownload();
                dialog.cancel();
                dialog.dismiss();
                alert.create();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                dialog.dismiss();
                alert.create();
            }
        });
        alert.setView(edittext);


        final AlertDialog.Builder time_setAlert = new AlertDialog.Builder(this);
        final EditText time_slot = new EditText(this);
        time_setAlert.setTitle("Time Setter");
        time_slot.setText(setTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        time_setAlert.setMessage("Enter time below, in unix milliseconds");
        time_setAlert.setPositiveButton("Set Time", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String time = time_slot.getText().toString();
                Log.e("WutFace", "time: " + time);
                usingCurrentTime = false;
                setTime = OffsetDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                dialog.cancel();
                dialog.dismiss();
                time_setAlert.create();
            }
        });
        time_setAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                dialog.dismiss();
                time_setAlert.create();
            }
        });
        time_setAlert.setView(time_slot);
        ImageButton download = findViewById(R.id.download);
        final AlertDialog alertd = alert.create();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertd.show();
            }
        });

        //for setting time
        ImageButton set_time = findViewById(R.id.set_time);
        final AlertDialog time_alert = time_setAlert.create();
        set_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                time_alert.show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> sortingOptions = new ArrayList<String>();
        sortingOptions.add("Title"); sortingOptions.add("Artist"); sortingOptions.add("Album"); sortingOptions.add("Favorite");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortingOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SongListSorter songListSorter = new SongListSorter();
        List<Song> sort = new ArrayList<>(songList);
        if(position == 0) tab1.updateDisplay(songListSorter.sortByTitle(sort));
        if(position == 1) tab1.updateDisplay(songListSorter.sortByArtist(sort));
        if(position == 2) tab1.updateDisplay(songListSorter.sortByAlbum(sort));
        if(position == 3) tab1.updateDisplay(songListSorter.sortByStatus(sort));
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, LibraryActivity.class);
            startActivity(refresh);
            this.finish();
        }
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
            switch (position) {

                case 0:
                    //TabSongs tab1 = new TabSongs();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("songPlayer", songPlayer);
                    tab1.setArguments(bundle);
//                    tab1.updateDisplay(songListGen.getAllsong());
                    return tab1;
                case 1:
                    //TabAlbum tab2 = new TabAlbum();
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
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SONGS";
                case 1:
                    return "ALBUMS";
                default:
                    return null;
            }
        }
    }
//   ---------------------------------- Get Location method here  ---------------------------------

    /* Get current Location */
    public static Location getLocation() {
        return loc;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        checkPermission();

        mFusedLocationClient = getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location oldLoc = loc;
                        loc = locationResult.getLastLocation();
                        if (oldLoc == null && locationCallback != null) {
                            locationCallback.locationReady();
                        }
                    }
                },
                Looper.myLooper());
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    public void setLocationReadyCallback(LocationReadyCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public interface LocationReadyCallback {
        public abstract void locationReady();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
