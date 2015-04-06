package com.badve.ajinkya.top10movies.extras;

import com.android.volley.RequestQueue;
import com.badve.ajinkya.top10movies.MyApplication;
import com.badve.ajinkya.top10movies.pojo.Movie;

import org.json.JSONObject;
import org.xml.sax.Parser;

import java.util.ArrayList;


/**
 * Created by Windows on 02-03-2015.
 */
public class MovieUtils {
    /*public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.sendRequestBoxOfficeMovies(requestQueue, Endpoints.getRequestUrl(30));
        ArrayList<Movie> listMovies = Parser.parseJSONResponse(response);
        MyApplication.getWritableDatabase().insertMoviesBoxOffice(listMovies, true);
        return listMovies;
    }*/
}
