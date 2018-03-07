package cse_110.flashback_player;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

/**
 * Created by pnad0911 on 3/3/18.
 */

public class AlbumAdapterExpandable extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDataSource;
    private Map<String, List<Song>> mSongList;

    public AlbumAdapterExpandable(Context context, List<String> listDataHeader,
                                  Map<String, List<Song>> listChildData) {
        mContext = context;
        mDataSource = listDataHeader;
        mSongList = listChildData;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return mSongList.get(mDataSource.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int AlbumPosition, final int songPosition,
                             boolean isLastSong, View convertView, ViewGroup parent) {

        final Song songName = (Song) getChild(AlbumPosition,songPosition);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_song_album, null);
        }
        TextView Child = convertView.findViewById(R.id.name);
        TextView  a = convertView.findViewById(R.id.artist);
        Child.setText(songName.getTitle());
        a.setText(songName.getArtist());
        final Button likeBt = (Button) convertView.findViewById(R.id.like_bt);
        int like = songName.getSongStatus(NormalActivity.getContextOfApplication());
        if(like == 0) {
            likeBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_black_24dp, 0);
        } else if(like == 1) {
            likeBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart, 0);
        } else {
            likeBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
        }
        likeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(likeBt,songName);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSongList.get(mDataSource.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDataSource.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mDataSource.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int AlbumPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(AlbumPosition);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.album_list_row, null);
        }
        TextView AlbumName = (TextView) convertView.findViewById(R.id.name);
        AlbumName.setTypeface(null, Typeface.BOLD);
        AlbumName.setText(headerTitle);
        return convertView;
    }
    private void toggle(Button button, Song song) {
        int songLiked = song.getSongStatus(NormalActivity.getContextOfApplication());
        if(songLiked == 0) {
            song.like(NormalActivity.getContextOfApplication());
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart, 0);
        } else if(songLiked == 1){
            song.dislike(NormalActivity.getContextOfApplication());
            TabFlashback.songIdx = 0;
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
        } else {
            song.neutral(NormalActivity.getContextOfApplication());
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_black_24dp, 0);
        }
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
