package com.example.pahteapp.domain.list;

import com.example.pahteapp.domain.movie.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {
    //Dit is voor de lijsten zodat we geen aparte klasse hoeven aan te maken
    @SerializedName("items")
    @Expose
    private List<Movie> items = null;

    @SerializedName("id")
    @Expose
    private Integer id = null;

    @Override
    public String toString() {
        return "MovieList{" +
                "items=" + items +
                ", id=" + id +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public List<Movie> getItems() {
        return items;
    }

    public void setItems(List<Movie> items) {
        this.items = items;
    }
}
