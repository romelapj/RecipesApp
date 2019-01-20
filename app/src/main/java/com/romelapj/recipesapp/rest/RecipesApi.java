package com.romelapj.recipesapp.rest;


import com.romelapj.recipesapp.models.Recipe;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RecipesApi {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Single<List<Recipe>> getRecipes();

}
