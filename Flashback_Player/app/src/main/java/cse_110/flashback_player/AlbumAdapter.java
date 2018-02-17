package cse_110.flashback_player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yutong on 2/7/18.
 * Adaptor that populates albums into the list view.
 */

//tutorial from https://www.raywenderlich.com/124438/android-listview-tutorial
public class AlbumAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDataSource;

    public AlbumAdapter(Context context, List<String> items){
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
    public View getView(int position, View convertView, ViewGroup parent){

        //get view for row item
        View rowView = mInflater.inflate(R.layout.album_list_row, parent, false);

        //get view elements in list row
        TextView albumNameView = (TextView) rowView.findViewById((R.id.name));

        String albumName = (String) getItem(position);

        albumNameView.setText(albumName);

        return rowView;
    }
}
