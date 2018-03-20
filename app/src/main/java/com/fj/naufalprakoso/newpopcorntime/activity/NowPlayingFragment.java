package com.fj.naufalprakoso.newpopcorntime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fj.naufalprakoso.newpopcorntime.R;
import com.fj.naufalprakoso.newpopcorntime.adapter.NowPlayingAdapter;
import com.fj.naufalprakoso.newpopcorntime.entity.NewMovieModel;
import com.fj.naufalprakoso.newpopcorntime.entity.ResponseNewMovieModel;
import com.fj.naufalprakoso.newpopcorntime.service.ApiInterface;
import com.fj.naufalprakoso.newpopcorntime.service.ApiService;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NaufalPrakoso on 19/03/18.
 */

public class NowPlayingFragment extends Fragment {
    private final static String api_key = "5bc5c506bf3ad45dd420e17ebb8486cf";
    private static final String SAVE_DATA_NOW_PLAYING = "save_data";

    RecyclerView NowPlayingrecyclerView;
    List<NewMovieModel> movieModels;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_now_playing, container, false);
        NowPlayingrecyclerView = (RecyclerView) view.findViewById(R.id.rv_now_playing);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NowPlayingrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NowPlayingrecyclerView.setHasFixedSize(true);
        if (savedInstanceState != null){
            movieModels = (List<NewMovieModel>) savedInstanceState.getSerializable(SAVE_DATA_NOW_PLAYING);
            NowPlayingrecyclerView.setAdapter(new NowPlayingAdapter(movieModels, getActivity()));
        }else {
            getRequest();
        }
    }

    private void getRequest(){
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        Call<ResponseNewMovieModel> call = apiInterface.getNowPlayingMovie(api_key);
        call.enqueue(new Callback<ResponseNewMovieModel>() {
            @Override
            public void onResponse(Call<ResponseNewMovieModel> call, Response<ResponseNewMovieModel> response) {
                movieModels = response.body().getResults();
                NowPlayingrecyclerView.setAdapter(new NowPlayingAdapter(movieModels, getActivity()));
            }

            @Override
            public void onFailure(Call<ResponseNewMovieModel> call, Throwable t) {
                Toast.makeText(getContext(), "Error: "+ t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_DATA_NOW_PLAYING, (Serializable) movieModels);
    }
}
