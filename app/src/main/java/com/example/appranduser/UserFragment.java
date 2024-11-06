package com.example.appranduser;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    private TextView tvNome, tvEmail;
    private ImageView ivFoto;
    private Spinner multiSelectSpinner, genderSpinner;
    private Button btPesquisar;
    private ArrayList<String> selectedCountries = new ArrayList<>();
    private Bitmap bmp;
    private Result res;

    private static final String[] COUNTRIES = {"AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI", "FR", "GB", "IE", "IN", "IR", "MX", "NL", "NO", "NZ", "RS", "TR", "UA", "US"};
    private static final String[] GENDERS = {"Selecione um Gênero", "Masculino", "Feminino"};

    public UserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Inicializa os componentes de UI
        tvNome = view.findViewById(R.id.tvNome);
        tvEmail = view.findViewById(R.id.tvIdade);
        ivFoto = view.findViewById(R.id.ivFoto);
        multiSelectSpinner = view.findViewById(R.id.multiSelectSpinner);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        btPesquisar = view.findViewById(R.id.btPesquisar);

        btPesquisar.setOnClickListener(v -> {
            String selectedGender = genderSpinner.getSelectedItem().toString();
            String genero = selectedGender.equals("Feminino") ? "female" : selectedGender.equals("Masculino") ? "male" : "";
            consultarUsuarioAleatorio(genero, selectedCountries,1);
        });

        carregaMultiSelect();
        carregaGender();

        if (MainActivity.user == null) {
            consultarUsuarioAleatorio("", new ArrayList<>(),0);
        } else {
            definirDadosUsuarioAleatorio(MainActivity.user);
        }

        return view;
    }

    private void definirDadosUsuarioAleatorio(Result user) {
        res = user;
        tvNome.setText(user.name.getNomeCompleto());
        tvEmail.setText(user.email);
        carregarImagem(user.picture.large);
        MainActivity.latitudeUser = Double.parseDouble(user.location.coordinates.latitude);
        MainActivity.longitudeUser = Double.parseDouble(user.location.coordinates.longitude);
    }

    private void carregarImagem(String url) {
        new Thread(() -> {
            try {
                bmp = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
                ivFoto.post(() -> ivFoto.setImageBitmap(bmp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void consultarUsuarioAleatorio(String genero, ArrayList<String> listaNacionalidades, int flag) {
        String nacionalidades = String.join(",", listaNacionalidades);
        Call<User> call = new RetrofitConfig().getUserService().getUser(genero, nacionalidades);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Result user = response.body().results.get(0);
                Log.i("user", user.name.first);

                if (MainActivity.user == null || flag ==1) {
                    MainActivity.user = user;
                }

                definirDadosUsuarioAleatorio(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("erro", t.getMessage());
            }
        });
    }

    private void montaPlaceholderNacionalidade() {
        String placeholder = selectedCountries.isEmpty() ? "Clique aqui" : String.join(", ", selectedCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new String[]{placeholder});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multiSelectSpinner.setAdapter(adapter);
    }

    private void carregaMultiSelect() {
        montaPlaceholderNacionalidade();
        multiSelectSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                openMultiSelectDialog();
            }
            return true;
        });
    }

    private void openMultiSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selecione as nacionalidades");

        boolean[] selectedItems = new boolean[COUNTRIES.length];
        builder.setMultiChoiceItems(COUNTRIES, selectedItems, (dialog, which, isChecked) -> {
            if (isChecked) {
                selectedCountries.add(COUNTRIES[which]);
            } else {
                selectedCountries.remove(COUNTRIES[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> montaPlaceholderNacionalidade());
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            selectedCountries.clear();
            Arrays.fill(selectedItems, false);
            montaPlaceholderNacionalidade();
        });
        builder.create().show();
    }

    private void carregaGender() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, GENDERS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }
}
