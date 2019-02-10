package com.romelapj.recipesapp.ui.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Step;

public class RecipeDetailFragment extends Fragment {

    public static final String ARG_ITEM_STEP = "item_step";

    private Step mItem;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayerView;
    private BandwidthMeter bandwidthMeter;
    private View recipeDetail;
    private TextView title;
    private TextView description;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_STEP)) {
            mItem = getArguments().getParcelable(ARG_ITEM_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        simpleExoPlayerView = rootView.findViewById(R.id.video_view);
        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        recipeDetail = rootView.findViewById(R.id.recipe_detail);
        title = rootView.findViewById(R.id.titleTextView);
        description = rootView.findViewById(R.id.descriptionTextView);

        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

        if (mItem != null) {
            title.setText(mItem.getShortDescription());
            description.setText(mItem.getDescription());
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);
        DefaultHttpDataSourceFactory recipe = new DefaultHttpDataSourceFactory(Util.getUserAgent(getContext(), "recipe"));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(recipe).createMediaSource(Uri.parse(mItem.getVideoURL()));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recipeDetail.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        player.stop();
        player.release();
        player = null;
    }
}
