package io.github.ryanhoo.music.ui.details;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.ryanhoo.music.R;
import io.github.ryanhoo.music.data.model.Song;
import io.github.ryanhoo.music.ui.base.adapter.IAdapterView;
import io.github.ryanhoo.music.utils.TimeUtils;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/11/16
 * Time: 6:27 AM
 * Desc: SongItemView
 */
public class SongItemView extends RelativeLayout implements IAdapterView<Song> {

    @InjectView(R.id.text_view_index)
    TextView textViewIndex;
    @InjectView(R.id.text_view_name)
    TextView textViewName;
    @InjectView(R.id.text_view_info)
    TextView textViewInfo;
    @InjectView(R.id.layout_action)
    View buttonAction;

    public SongItemView(Context context) {
        super(context);
        View inflate = View.inflate(context, R.layout.item_play_list_details_song, this);
        ButterKnife.inject(this,inflate);
    }

    @Override
    public void bind(Song song, int position) {
        textViewIndex.setText(String.valueOf(position + 1));
        textViewName.setText(song.getDisplayName());
        textViewInfo.setText(String.format("%s | %s", TimeUtils.formatDuration(song.getDuration()), song.getArtist()));
    }
}
