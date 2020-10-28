package net.sunyounglee.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Step;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String TAG = StepDetailFragment.class.getSimpleName();
    public static final String ARG_STEP = "step_arg";
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private String mVideoUri;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean landscapeMode = false;

    //  private StepViewModel stepViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            if (getArguments() != null) {
                Bundle args = getArguments();
                mStep = args.getParcelable(ARG_STEP);
            } else {
                Log.d(TAG, "ingredient bundle is null");
            }
        } else {
            mStep = savedInstanceState.getParcelable("STEP_SAVE_INSTANCE");
        }

//        StepViewRepository stepViewRepository = StepViewRepository.getInstance(mStep);
//        StepViewModelFactory stepViewModelFactory = new StepViewModelFactory(mStep, getActivity().getApplication(), stepViewRepository);
//        stepViewModel = new ViewModelProvider(this, stepViewModelFactory).get(StepViewModel.class);

        getOrientationMode();


        mPlayerView = view.findViewById(R.id.playerView);
        ImageView mRecipeThumbnail = view.findViewById(R.id.iv_recipe_thumbnail);
        if (mStep != null) {
            if (!landscapeMode || (isTablet() && landscapeMode)) {
                TextView tvDescription = view.findViewById(R.id.tv_description);
                tvDescription.setText(mStep.getDescription());
            }

            if (!mStep.getVideoURL().isEmpty()) {
                mVideoUri = mStep.getVideoURL();
                Log.d(TAG, "video is not null" + mVideoUri);
                mPlayerView.setVisibility(View.VISIBLE);
                mRecipeThumbnail.setVisibility(View.INVISIBLE);
                initializePlayer(Uri.parse(mVideoUri));
            } else if (!mStep.getThumbnailURL().isEmpty()) {
                String mThumbnailUri = mStep.getThumbnailURL();
                Log.d(TAG, "thumbnail is not null: " + mThumbnailUri);
                mPlayerView.setVisibility(View.INVISIBLE);
                mRecipeThumbnail.setVisibility(View.VISIBLE);
                Glide.with(this.getActivity())
                        .asBitmap()
                        .load(mThumbnailUri)
                        .into(mRecipeThumbnail);
            } else {
                Log.d(TAG, "both are not null");
                mPlayerView.setVisibility(View.INVISIBLE);
                mRecipeThumbnail.setVisibility(View.VISIBLE);
                mRecipeThumbnail.setImageResource(R.drawable.no_image_available);
            }
            Log.d(TAG, "VIDEO URI: " + mVideoUri);
        }
    }

    public void initializePlayer(Uri parse) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), getContext().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);
            mExoPlayer.prepare(mediaSource);
            // mExoPlayer.setPlayWhenReady(true);
        }
    }

    public void releasePlayer() {
        if (mExoPlayer != null) {
            playWhenReady = mExoPlayer.getPlayWhenReady();
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();

            mExoPlayer.stop();
            mExoPlayer.removeListener(this);
            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
        if (Util.SDK_INT >= 24 && mExoPlayer != null) {
            initializePlayer(Uri.parse(mVideoUri));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        //  hideSystemUi();
        if ((Util.SDK_INT < 24 && mExoPlayer != null)) {
            initializePlayer(Uri.parse(mVideoUri));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            Log.d(TAG, "onPlayerStateChanged: PLAYING");
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            Log.d(TAG, "onPlayerStateChanged: PAUSED");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("STEP_SAVE_INSTANCE", mStep);
    }

    private void getOrientationMode() {
        int orientation_num = getResources().getConfiguration().orientation;
        landscapeMode = orientation_num != 1;
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 600;
            mPlayerView.setLayoutParams(params);
        }
    }
}