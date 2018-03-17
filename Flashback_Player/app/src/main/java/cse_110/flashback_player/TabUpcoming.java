package cse_110.flashback_player;

/**
 * Created by Yutong on 2/10/18.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabUpcoming extends Fragment implements SongListListener{

    private List<Song> songList;
    private SongPlayer songPlayer;
    private SongList songListGen;
    public SongAdapterUpcoming adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tabupcoming, container, false);
        Bundle bundle1 = this.getArguments();
        songPlayer = (SongPlayer) bundle1.getParcelable("songPlayer");

//        songListGen = new SongList(this.getActivity());
//        songList = songListGen.getAllsong();
        songList = new ArrayList<>();
        adapter = new SongAdapterUpcoming(this.getActivity(), songList);
        final ListView sListView = (ListView) rootView.findViewById(R.id.song_list);
        sListView.setAdapter(adapter);
        // Handle on click event
//        sListView.setClickable(true);
//        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//            }
//        });
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    public void updateDisplay(List<Song> list) {
        songList.clear();
        songList.addAll(list);
        adapter.notifyDataSetChanged();
    }
    public void updateDisplay(Map<String, List<Song>> map, List<String> albumNames){}
    public void updateDisplayUpcoming(List<Song> list) { }

}
