package cse_110.flashback_player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yutong on 2/7/18.
 * Adaptor that populates songs into the list view.
 */

//tutorial from https://www.raywenderlich.com/124438/android-listview-tutorial
public class SongAdapterUpcoming extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Song> mDataSource;
    public SongAdapterUpcoming(Context context, List<Song> items){
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

        View rowView = mInflater.inflate(R.layout.song_list_row_upcoming, parent, false);
        TextView songNameView = (TextView) rowView.findViewById((R.id.name));
        TextView artistView = (TextView) rowView.findViewById((R.id.artist));
        TextView albumView = (TextView) rowView.findViewById((R.id.album));
        final ImageView waiting = rowView.findViewById(R.id.waiting);
//        Animation rotation = AnimationUtils.loadAnimation(VibeActivity.getContextOfApplication(), R.anim.rotate);
//        rotation.setRepeatCount(Animation.INFINITE);
//        waiting.startAnimation(rotation);
//        Button b = rowView.findViewById(R.id.bt);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                update(waiting);
//            }
//        });
        final Song song = (Song) getItem(position);
        songNameView.setText(song.getTitle());
        artistView.setText(song.getArtist());
        albumView.setText(song.getAlbum());

        boolean downloaded = song.getDownloadStatus();
        if(!downloaded) {
            Animation rotation = AnimationUtils.loadAnimation(VibeActivity.getContextOfApplication(), R.anim.rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            waiting.startAnimation(rotation);
        } else {
            waiting.clearAnimation();
            waiting.setBackgroundResource(R.drawable.ic_check_black_24dp);
        }
        return rowView;
    }
//    public void update(ImageView waiting) {
//        waiting.clearAnimation();
//        waiting.setBackgroundResource(R.drawable.ic_check_black_24dp);
//    }
}



