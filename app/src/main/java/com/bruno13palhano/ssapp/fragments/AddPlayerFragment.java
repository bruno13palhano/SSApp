package com.bruno13palhano.ssapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddPlayerFragment extends Fragment {
    private Calendar calendar;
    private EditText inputPlayerNickName, inputPlayerBirthday;
    private ImageView inputPlayerImage;

    private String playerImageURI;
    private String day;
    private String month;
    private String year;
    private long playerAgeInMillis;
    private long playerCreateProfileDateInMillis;

    private PlayerViewModel playerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_settings_fragment);

        View view = inflater.inflate(R.layout.fragment_add_player, container, false);

        inputPlayerNickName = view.findViewById(R.id.add_player_name);
        inputPlayerImage = view.findViewById(R.id.add_player_image_icon);
        inputPlayerBirthday = view.findViewById(R.id.add_player_birthday);
        inputPlayerBirthday.setFocusable(false);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        setHasOptionsMenu(true);

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.enableUpButton();
        }

        calendar = Calendar.getInstance();

        inputPlayerBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        inputPlayerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        return view;
    }
    private void showDatePicker(){
        MaterialDatePicker.Builder<Long> dateBuildr =
                MaterialDatePicker.Builder.datePicker();
        dateBuildr.setTitleText(R.string.label_player_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = dateBuildr.build();

        datePicker.show(getActivity().getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar currentCalendar = Calendar.getInstance();
                Calendar referentialCalendar = Calendar.getInstance();
                referentialCalendar.setTimeInMillis(MaterialDatePicker.todayInUtcMilliseconds());

                long timeZoneAdjustment = (currentCalendar.getTime().getTime()) - (referentialCalendar.getTime().getTime());
                long hourAdjustment = TimeUnit.MILLISECONDS.toHours(timeZoneAdjustment);

                calendar.setTimeInMillis(selection);
                calendar.add(Calendar.HOUR_OF_DAY, (int)hourAdjustment);

                //data da criação do perfil
                playerCreateProfileDateInMillis = currentCalendar.getTimeInMillis();

                //idade do jogador em milisegundos
                playerAgeInMillis = calendar.getTimeInMillis();

                //calculo da idade em anos, deve ser usado em outra parte do app e não aqui.
                TimeUnit.MILLISECONDS.toDays((currentCalendar.getTime().getTime() - calendar.getTime().getTime())/365);

                formatPlayerBirthday();

                //aqui entra a view model...
                inputPlayerBirthday.setText(day.concat("/"+month+"/"+year));
            }
        });
    }

    public void formatPlayerBirthday(){
        if(calendar.get(Calendar.DAY_OF_MONTH) < 10){
            day = "0"+calendar.get(Calendar.DAY_OF_MONTH);
        }else{
            day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        if(calendar.get(Calendar.MONTH)+1 < 10){
            month = "0"+(calendar.get(Calendar.MONTH)+1);
        }else{
            month = String.valueOf(calendar.get(Calendar.MONTH));
        }

        year = String.valueOf(calendar.get(Calendar.YEAR));
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);
        if(requestCode == 2 && resultCode == getActivity().RESULT_OK){
            playerImageURI = resultData.getData().toString();

            try{
                inputPlayerImage.setImageBitmap(SSUtil.decodeUri(requireActivity(), resultData.getData(),
                        150));
            }catch (NullPointerException | FileNotFoundException ignored){}
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            getActivity().onBackPressed();
            return true;

        }else if(id == R.id.action_done){
            String playerNickName = inputPlayerNickName.getText().toString();

            Player player = new Player(playerNickName);
            player.setPlayerImageUri(playerImageURI);
            player.setPlayerDateInMillis(playerAgeInMillis);
            player.setPlayerCreateProfileDateInMillis(playerCreateProfileDateInMillis);
            playerViewModel.insertPlayer(player);

            Fragment fragment = new PlayerFragment();
            SSUtil.setFragment(getActivity(), fragment, false);
            return true;
        }

        return (super.onOptionsItemSelected(item));
    }
}
