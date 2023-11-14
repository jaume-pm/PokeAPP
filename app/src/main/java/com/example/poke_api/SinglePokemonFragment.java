package com.example.poke_api;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SinglePokemonFragment extends Fragment {

    TextView txt_name, txt_weight, txt_height;
    EditText etxt_name;

    public SinglePokemonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_pokemon, container, false);
        Button btn_getInfo = v.findViewById(R.id.btn_getInfo);
        txt_name = v.findViewById(R.id.txt_name);
        txt_height = v.findViewById(R.id.txt_height);
        txt_weight = v.findViewById(R.id.txt_weight);
        etxt_name = v.findViewById(R.id.etxt_name);

        btn_getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://pokeapi.co/api/v2/") // Base URL
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PokemonAPIService pokapis = retrofit.create(PokemonAPIService.class);
                pokapis.getPokemon(etxt_name.getText().toString().toLowerCase()).enqueue(new Callback<Pokemon>() {
                    @Override
                    public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                        if (response.isSuccessful()) {
                            Pokemon p = response.body();
                            if (p != null) {
                                txt_name.setText(p.getName());
                                txt_height.setText(String.valueOf(p.getHeight()));
                                txt_weight.setText(String.valueOf(p.getWeight()));
                            } else {
                                txt_name.setText("Pokemon not found");
                                txt_height.setText("");
                                txt_weight.setText("");
                            }
                        } else {
                            txt_name.setText("Error: " + response.code());
                            txt_height.setText("Pokémon" + etxt_name.getText().toString() + "No trobat.");
                            txt_weight.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<Pokemon> call, Throwable t) {
                        txt_name.setText("Error: " + t.getMessage());
                        txt_height.setText("");
                        txt_weight.setText("");
                    }
                });
            }
        });

        return v;
    }
}
