package com.romelapj.recipesapp.ui.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Step;

public class RecipeDetailFragment extends Fragment {

    public static final String ARG_ITEM_STEP = "item_step";
    private final String SELECTED_POSITION = "position";
    private final String IS_READY = "is_ready";

    private Step mItem;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayerView;
    private View recipeDetail;
    private TextView title;
    private TextView description;

    private boolean isReady = true;
    private long position = -1;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_STEP)) {
            mItem = getArguments().getParcelable(ARG_ITEM_STEP);
        }

        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
            isReady = savedInstanceState.getBoolean(IS_READY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        simpleExoPlayerView = rootView.findViewById(R.id.video_view);
        recipeDetail = rootView.findViewById(R.id.recipe_detail);
        title = rootView.findViewById(R.id.titleTextView);
        description = rootView.findViewById(R.id.descriptionTextView);


        if (mItem != null) {
            title.setText(mItem.getShortDescription());
            description.setText(mItem.getDescription());
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializerPlayer(Uri.parse(mItem.getVideoURL()));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recipeDetail.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    private void initializerPlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            if (position > 0) {
                player.seekTo(position);
            }
            player.setPlayWhenReady(isReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            position = player.getCurrentPosition();
            isReady = player.getPlayWhenReady();
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, position);
        outState.putBoolean(IS_READY, isReady);
    }
}
