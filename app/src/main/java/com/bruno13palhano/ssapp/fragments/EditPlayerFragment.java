package com.bruno13palhano.ssapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.activities.AllPlayersActivity;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class EditPlayerFragment extends Fragment {
    private ImageView inputPlayerImageUri;
    private EditText inputPlayerNickname, inputPlayerBirthday;
    private Calendar calendar;
    private String day, month, year;
    private long playerBirthdayInMillis;

    private Player player;
    private PlayerViewModel playerViewModel;

    private int playerPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_player_fragment);

        View view = inflater.inflate(R.layout.fragment_edit_player, container, false);

        inputPlayerImageUri = view.findViewById(R.id.edit_player_image_icon);
        inputPlayerNickname = view.findViewById(R.id.edit_player_name);
        inputPlayerBirthday = view.findViewById(R.id.edit_player_birthday);

        inputPlayerBirthday.setFocusable(false);
        setHasOptionsMenu(true);

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.enableUpButton();
        }

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        calendar = Calendar.getInstance();

        Intent intent = this.getActivity().getIntent();
        Bundle bundle = this.getArguments();

        if(bundle != null){
            player = new Player(bundle.getString("nickname"));
            player.setPlayerId(bundle.getLong("id"));
            player.setPlayerImageUri(bundle.getString("imageUri"));
            player.setPlayerDateInMillis(bundle.getLong("dateInMillis"));
            player.setPlayerScoreMatch(bundle.getInt("scoreMatch"));
            player.setPlayerScoreTotal(bundle.getInt("scoreTotal"));
            player.setPlayerScoreSeries(bundle.getInt("scoreSeries"));
            player.setPlayerWonTournaments(bundle.getInt("numberOfWonTournaments"));
            playerPosition = bundle.getInt("position");

            try{
                inputPlayerImageUri.setImageBitmap(SSUtil.decodeUri(requireActivity(), Uri.parse(bundle.getString("imageUri")),
                        150));
            }catch (NullPointerException | FileNotFoundException ignored){}

            inputPlayerNickname.setText(bundle.getString("nickname"));
            calendar.setTimeInMillis(bundle.getLong("dateInMillis"));

            formatPlayerBirthday();

            inputPlayerBirthday.setText(day.concat("/"+month+"/"+year));
        }

        inputPlayerImageUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        inputPlayerBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return view;
    }

    private void showDatePicker(){
        MaterialDatePicker.Builder<Long> dateBuilder =
                MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText(R.string.label_player_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = dateBuilder.build();

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

                playerBirthdayInMillis = calendar.getTimeInMillis();
                player.setPlayerDateInMillis(playerBirthdayInMillis);

                formatPlayerBirthday();

                inputPlayerBirthday.setText(day.concat("/"+month+"/"+year));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);
        if(requestCode == 2 && resultCode == getActivity().RESULT_OK){
            player.setPlayerImageUri(resultData.getData().toString());
            inputPlayerImageUri.setImageURI(resultData.getData());
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
//            Intent intent = new Intent(getContext(), AllPlayersActivity.class);
//
//            intent.putExtra("id", player.getPlayerId());
//            intent.putExtra("nickname", player.getPlayerNickName());
//            intent.putExtra("imageUri", player.getPlayerImageUri());
//            intent.putExtra("dateInMillis", player.getPlayerDateInMillis());
//            intent.putExtra("scoreMatch", player.getPlayerScoreMatch());
//            intent.putExtra("scoreTotal", player.getPlayerScoreTotal());
//            intent.putExtra("scoreSeries", player.getPlayerScoreSeries());
//            intent.putExtra("numberOfWonTournaments", player.getPlayerWonTournaments());
//            intent.putExtra("position", playerPosition);
//
//            startActivity(intent);
            return true;

        }else if(id == R.id.action_done){
            editPlayerDone();
        }

        return super.onOptionsItemSelected(item);
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

    private void editPlayerDone(){
        String playerNickname = inputPlayerNickname.getText().toString();
        if(!playerNickname.isEmpty()){
            player.setPlayerNickName(playerNickname);
            playerViewModel.updatePlayer(player);

            getActivity().onBackPressed();

        }else{
            Toast.makeText(getContext(), R.string.alert_fill_data_player, Toast.LENGTH_SHORT).show();
        }
    }

    private void setIntentAllPlayersActivity(){
        Intent intent = new Intent(getActivity(), AllPlayersActivity.class);
        intent.putExtra("fragment", "editPlayer");
        intent.putExtra("id", player.getPlayerId());
        intent.putExtra("nickname", player.getPlayerNickName());
        intent.putExtra("imageUri", player.getPlayerImageUri());
        intent.putExtra("dateInMillis", player.getPlayerDateInMillis());
        intent.putExtra("scoreMatch", player.getPlayerScoreMatch());
        intent.putExtra("scoreTotal", player.getPlayerScoreTotal());
        intent.putExtra("scoreSeries", player.getPlayerScoreSeries());
        intent.putExtra("numberOfWonTournaments", player.getPlayerWonTournaments());
        intent.putExtra("position", playerPosition);

        startActivity(intent);
    }
}