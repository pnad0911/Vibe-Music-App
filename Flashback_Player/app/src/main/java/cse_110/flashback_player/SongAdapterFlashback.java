package cse_110.flashback_player;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yutong on 2/7/18.
 * Adaptor that populates songs into the list view.
 */

//tutorial from https://www.raywenderlich.com/124438/android-listview-tutorial
public class SongAdapterFlashback extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Song> mDataSource;

    public SongAdapterFlashback(Context context, List<Song> items){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position){
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get view for row item
        View rowView = mInflater.inflate(R.layout.song_list_row_flashback, parent, false);

        //get view elements in list row
        final TextView songNameView = (TextView) rowView.findViewById((R.id.name));
        TextView artistView = (TextView) rowView.findViewById((R.id.artist));
        TextView albumView = (TextView) rowView.findViewById((R.id.album));

        final Song song = (Song) getItem(position);

        songNameView.setText(song.getTitle());
        artistView.setText(song.getArtist());
        albumView.setText(song.getAlbum());
        final Button likeBt = (Button) rowView.findViewById(R.id.like_bt);
        Boolean like = TabFlashback.flashbackPlaylist.songCurrentlyLiked(song);
        if(like == null) {
            likeBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_black_24dp, 0);
        } else if(like) {
            likeBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart, 0);
        } else {
            likeBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
        }
        likeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(likeBt,song);
            }
        });

        return rowView;
    }
    private void toggle(Button button, Song song) {
        Boolean songLiked = song.songCurrentlyLiked();
        if(songLiked == null) {
//            TabFlashback.flashbackPlaylist.likeSong(song);
            song.likeSong();
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart, 0);
        } else if(songLiked){
//            TabFlashback.flashbackPlaylist.dislikeSong(song);
            song.dislikeSong();
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
        } else {
//            TabFlashback.flashbackPlaylist.neutralSong(song);
            song.neutralSong();
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_black_24dp, 0);
        }
    }
}
