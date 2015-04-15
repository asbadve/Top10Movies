package com.badve.ajinkya.top10movies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.badve.ajinkya.top10movies.MyApplication;
import com.badve.ajinkya.top10movies.R;
import com.badve.ajinkya.top10movies.adapters.AdapterBoxOffice;
import com.badve.ajinkya.top10movies.extras.Keys;
import com.badve.ajinkya.top10movies.logging.L;
import com.badve.ajinkya.top10movies.pojo.Movie;
import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;
import com.hudomju.swipe.adapter.ViewAdapter;

import static com.badve.ajinkya.top10movies.extras.Keys.EndpointBoxOffice.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.badve.ajinkya.top10movies.extras.UrlEndpoints.*;
import network.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentBoxoffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentBoxoffice extends Fragment {

    private static final String URL_ROTANTOMATOS = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    ArrayList<Movie> movieList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView recyclerViewBoxOfficeMovies;
    private AdapterBoxOffice adapterBoxOffice;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentBoxoffice.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentBoxoffice newInstance(String param1, String param2) {
        fragmentBoxoffice fragment = new fragmentBoxoffice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public fragmentBoxoffice() {
        // Required empty public constructor
    }

    /**
     * SwipeDismiss callback
     *
     * Remove items, and show undobar
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayList<Movie> parseJsonRequest(JSONObject response) {
        ArrayList<Movie> listMovie = new ArrayList<>();
        if (response!=null || response.length()>0) {

            try {
                if (response.has(KEY_MOVIES)) {
                    JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                    for (int i=0;i<arrayMovies.length();i++){
                        JSONObject jsonObject = arrayMovies.getJSONObject(i);
                        long id = jsonObject.getLong(KEY_ID);
                        String title = jsonObject.getString(KEY_TITLE);
                        JSONObject objectReleaseDate = jsonObject.getJSONObject(KEY_RELEASE_DATES);
                        String releaseDate=null;
                        if(objectReleaseDate.has(KEY_THEATER)){
                            releaseDate=objectReleaseDate.getString(KEY_THEATER);
                        }else {
                            releaseDate="NA";
                        }

                        int audianceScore=-1;
                        JSONObject objectRating = jsonObject.getJSONObject(KEY_RATINGS);
                        if(objectRating.has(KEY_AUDIENCE_SCORE)){

                            audianceScore=objectRating.getInt(KEY_AUDIENCE_SCORE);
                        }
                        String synopsis = jsonObject.getString(KEY_SYNOPSIS);

                        JSONObject objectPoster = jsonObject.getJSONObject(KEY_POSTERS);

                        String urlThumbnail = null;

                        if(objectPoster.has(KEY_THUMBNAIL)){
                            urlThumbnail = objectPoster.getString(KEY_THUMBNAIL);
                        }
                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);
                        movie.setSynopsis(synopsis);
                        Date releasedate = dateFormat.parse(releaseDate);
                        movie.setReleaseDateTheater(releasedate);
                        movie.setAudienceScore(audianceScore);
                        movie.setUrlThumbnail(urlThumbnail);
                        listMovie.add(movie);
                    }
                    L.T(getActivity(),listMovie.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listMovie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boxoffice, container, false);


        recyclerViewBoxOfficeMovies = (RecyclerView)view.findViewById(R.id.recyclerViewBoxOfficeMovies);
        recyclerViewBoxOfficeMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        recyclerViewBoxOfficeMovies.setAdapter(adapterBoxOffice);
        movieList =  addMovieList();
        adapterBoxOffice.setMovies(movieList);

        final SwipeToDismissTouchListener touchListener =
                new SwipeToDismissTouchListener(
                        new RecyclerViewAdapter(recyclerViewBoxOfficeMovies),
                        new SwipeToDismissTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int i) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ViewAdapter viewAdapter, int i) {
                                adapterBoxOffice.remove(i);
                            }
                        });

        recyclerViewBoxOfficeMovies.setOnTouchListener(touchListener);

        recyclerViewBoxOfficeMovies.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());

        recyclerViewBoxOfficeMovies.addOnItemTouchListener(new SwipeableItemClickListener(getActivity().getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (view.getId() == R.id.txt_delete) {
                    touchListener.processPendingDismisses();
                } else if (view.getId() == R.id.txt_undo) {
                    touchListener.undoPendingDismiss();
                } else { // R.id.txt_data
                    Toast.makeText(getActivity().getApplicationContext(), "Position " + i, Toast.LENGTH_SHORT).show();
                }
            }
        }));

        //sendJasonRequest();
        return view;


    }

    private void sendJasonRequest() {

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getUrlRotantomatos(10),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //movieList =  parseJsonRequest(response);

//                        adapterBoxOffice.setMovie(movieList);
                        //movieList =  addMovieList();
                        //adapterBoxOffice.setMovies(movieList);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      /*  movieList =  addMovieList();
                        adapterBoxOffice.setMovies(movieList);*/

                    }
                });
        requestQueue.add(request);


    }

    private ArrayList<Movie> addMovieList() {
        Date date = new Date(2015,8,12);

        ArrayList<Movie> listMovie = new ArrayList<>();
        for (int i =0;i<15;i++){
            Movie movie = new Movie(123,"Hera Pheri",date,23,"This is synopsis","URL","urlSelf","urlCast","BEst movie ever seen","Url similar");
            listMovie.add(movie);
        }
        Movie movie = new Movie(123,"aera Pheri",date,23,"This is synopsis","URL","urlSelf","urlCast","BEst movie ever seen","Url similar");
        Movie movie2 = new Movie(123,"rera Pheri",date,23,"This is synopsis","URL","urlSelf","urlCast","BEst movie ever seen","Url similar");
        listMovie.add(movie);
        listMovie.add(movie2);
        return listMovie;
    }

    public static String getUrlRotantomatos(int limit){
        //return URL_BO+"?apikey="+MyApplication.API_KEY_ROTTEN_TOMATOS+"&limit="+limit;
        return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOS
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }

}
