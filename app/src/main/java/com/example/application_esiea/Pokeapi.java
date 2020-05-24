package com.example.application_esiea;

import android.telecom.Call;

import com.example.application_esiea.model.RestPokemonResponse;

import retrofit2.http.GET;



public interface Pokeapi {
    @GET("/api/v2/pokemon")
    Call<RestPokemonResponse > getPokemonResponse();


}
