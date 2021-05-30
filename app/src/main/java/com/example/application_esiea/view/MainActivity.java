package com.example.application_esiea.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

;
import com.example.application_esiea.Constants;
import com.example.application_esiea.Pokeapi;
import com.example.application_esiea.R;
import com.example.application_esiea.model.Pokemon;
import com.example.application_esiea.model.RestPokemonResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://pokeapi.co/";
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences SharedPreferences;
    private Gson gson;
    private Object ListType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences = getSharedPreferences("application_esiea", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();

        List< Pokemon > pokemonList = getDataFromCache();
        if (pokemonList != null) {
            showList(pokemonList);
        } else {
            makeApiCall();
        }
    }

    private List<Pokemon> getDataFromCache(){
        String jsonPokemon = SharedPreferences.getString(Constants.KEY_POKEMON_LIST,null);
        if(jsonPokemon == null){
            return null;
        } else {
            Type listType = new TypeToken<List<Pokemon>>() {}.getType();
            return gson.fromJson(jsonPokemon, (Class<List<Pokemon>>) ListType);
        }

}
    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void showList(List<Pokemon>PokemonList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new ListAdapter(PokemonList);
        recyclerView.setAdapter(mAdapter);
    }


    private void makeApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Pokeapi PokeApi = retrofit.create(Pokeapi.class);

        android.telecom.Call<RestPokemonResponse> call = PokeApi.getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponse>() {
            @Override
            public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {

                    if (response.isSuccessful() && response.body() != null)  {
                        List<Pokemon> PokemonList = response.body().getResults();
                        saveList(PokemonList);
                       showList(PokemonList);
                    } else {
                        ShowError();
                    }
                }

            @Override
            public void onFailure(Call<RestPokemonResponse> call, Throwable t) {
                ShowError();
                

            }
        });


    }

    private void saveList(List<Pokemon> pokemonList) {
        String jsonString = gson.toJson(pokemonList);
        SharedPreferences
                .edit()
                .putInt("cle_integer", 3)
                .putString(Constants.KEY_POKEMON_LIST, "jsonString")
                .apply();
        Toast.makeText(getApplicationContext(), "List saved", Toast.LENGTH_SHORT).show();
    }

    private void ShowError() {
        Toast.makeText(getApplicationContext(), "API ERROR", Toast.LENGTH_SHORT).show();
    }

    private class typeToken<T> {
    }
}







