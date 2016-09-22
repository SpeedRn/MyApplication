package com.example.sdpc.myapplication.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.sdpc.myapplication.R;

/**
 * Created by sdpc on 16-8-31.
 */
public class VideoFragment extends BaseFragment {


    VideoView video;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.video_fragment,null);
        video = (VideoView) v.findViewById(R.id.video_view);
        return v;
    }



    @Override
    public void release() {
        if(video !=null){
            video.pause();
            video.stopPlayback();
        }
    }

    @Override
    public void start() {
        if(video !=null){
            video.setVideoURI(Uri.parse("http://dlhls.cdn.zhanqi.tv/zqlive/45338_MRc2N.m3u8"));
            video.start();
        }
    }

    @Override
    public String getPageTitle() {
        return "Video";
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }
}
