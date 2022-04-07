package com.example.pahteapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.pahteapp.domain.movie.Movie;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void Movieruntime_convertedToHoursAndMinutes(){
        Movie movie = new Movie();
        movie.setRuntime(148);

        assertEquals(movie.getRuntime() / 60 + "h" + movie.getRuntime() % 60 + "m" ,"2h28m");
    }
    @Test
    public void Movieruntime_convertedToHoursAndMinutes2(){
        Movie movie = new Movie();
        movie.setRuntime(180);

        assertEquals(movie.getRuntime() / 60 + "h" + movie.getRuntime() % 60 + "m" ,"3h0m");
    }
    @Test
    public void Movieruntime_convertedToHoursAndMinutes3(){
        Movie movie = new Movie();
        movie.setRuntime(239);

        assertEquals(movie.getRuntime() / 60 + "h" + movie.getRuntime() % 60 + "m" ,"3h59m");
    }


    @Test
    public void MovieVoteAverage_dividedBy2_1(){
        Movie movie = new Movie();
        movie.setVoteAverage(8.2);

        assertEquals(movie.getVoteAverage() / 2, 4.1, 0.01);
    }
    @Test
    public void MovieVoteAverage_dividedBy2_2(){
        Movie movie = new Movie();
        movie.setVoteAverage(4.0);

        assertEquals(movie.getVoteAverage() / 2, 2, 0.01);
    }


}