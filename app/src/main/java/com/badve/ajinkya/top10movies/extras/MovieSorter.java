package com.badve.ajinkya.top10movies.extras;

import com.badve.ajinkya.top10movies.pojo.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by Windows on 18-02-2015.
 */
public class MovieSorter {
    public void sortMoviesByName(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }
    public void sortMoviesByDate(ArrayList<Movie> movies){

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                Date lhsDate=lhs.getReleaseDateTheater();
                Date rhsDate=rhs.getReleaseDateTheater();
                if(lhs.getReleaseDateTheater()!=null && rhs.getReleaseDateTheater()!=null)
                {
                    return rhs.getReleaseDateTheater().compareTo(lhs.getReleaseDateTheater());
                }
                else {
                    return 0;
                }

            }
        });
    }
    public void sortMoviesByRating(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int ratingLhs=lhs.getAudienceScore();
                int ratingRhs=rhs.getAudienceScore();
                if(ratingLhs<ratingRhs)
                {
                    return 1;
                }
                else if(ratingLhs>ratingRhs)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }
}
