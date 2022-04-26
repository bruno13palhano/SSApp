package com.bruno13palhano.ssapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.activities.AllPlayersActivity;
import com.bruno13palhano.ssapp.adapters.PlayerListAdapter;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerFragment extends Fragment {
    private PlayerViewModel playerViewModel;
    private PlayerListAdapter adapter;

    private List<Player> playerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_players, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_player_fragment);

        RecyclerView recyclerView = view.findViewById(R.id.player_recycler);
        adapter = new PlayerListAdapter(new PlayerListAdapter.PlayerDiff());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), players ->{
            playerList = players;

            adapter.submitList(playerList);
            processInBg(playerList);

        });

//        //teste do firebase na PlayerViewModel
//        playerViewModel.getAllPlayersFirebase().observe(getViewLifecycleOwner(), players -> {
//            playerList = players;
//
//            adapter.submitList(playerList);
//            processInBg(players);
//            adapter.notifyDataSetChanged();
//        });
        // TODO: 17/04/2022 implementar os listeners da série, e desativar o firebase por enquanto

        adapter.setOnDeleteIconClickListener( position -> {
            playerViewModel.deletePlayer(playerList.get(position));
        });

        adapter.setOnRecyclerClickListener(position -> {
            Bundle bundle = new Bundle();

            bundle.putInt("position", position);
            bundle.putLong("id", playerList.get(position).getPlayerId());
            bundle.putString("nickname", playerList.get(position).getPlayerNickName());
            bundle.putString("imageUri", playerList.get(position).getPlayerImageUri());
            bundle.putLong("dateInMillis", playerList.get(position).getPlayerDateInMillis());
            bundle.putInt("scoreMatch", playerList.get(position).getPlayerScoreMatch());
            bundle.putInt("scoreTotal", playerList.get(position).getPlayerScoreTotal());
            bundle.putInt("scoreSeries", playerList.get(position).getPlayerScoreSeries());
            bundle.putInt("numberOfWonTournaments", playerList.get(position).getPlayerWonTournaments());
            bundle.putInt("numberOfWins", playerList.get(position).getPlayerNumberOfWins());
            bundle.putInt("numberOfMatches", playerList.get(position).getPlayerNumberOfMatches());

            Fragment fragment = new ShowPlayerFragment();
            fragment.setArguments(bundle);
            SSUtil.setFragment(requireActivity(), fragment, true);

        });

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.disableUpButton();
        }

        setHasOptionsMenu(true);

//        allPlayersFB();

        return view;
    }

    private void allPlayersFB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("players")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                System.out.println("player id:"+document.getId()+
                                        " => "+ document.getData());
                            }

                        }else{
                            System.out.println("não listou!");
                        }
                    }
                });
    }

    public interface OnProcessedListener{
        void onProcessed(List<Bitmap> listBitmaps);
    }

    private void processInBg(final List<Player> players){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final OnProcessedListener listener = new OnProcessedListener(){
            @Override
            public void onProcessed(List<Bitmap> listBitmaps){
                // Use the handler so we're not trying to update the UI from the bg thread
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        adapter.setBitmaps(listBitmaps);
                        adapter.notifyDataSetChanged();

                        executor.shutdown();
                    }
                });
            }
        };

        Runnable backgroundRunnable = new Runnable(){
            @Override
            public void run(){
                try {
                    List<Bitmap> listBitmaps = new ArrayList<>();

                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    for (int i = 0; i < players.size(); i++) {
                        String imageUri = players.get(i).getPlayerImageUri();
                        requireActivity().getContentResolver().takePersistableUriPermission(Uri.parse(imageUri), takeFlags);
                        listBitmaps.add(i, SSUtil.decodeUri(requireActivity(), Uri.parse(imageUri), 56));
                    }

                    listener.onProcessed(listBitmaps);

                } catch (FileNotFoundException | IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        };

       executor.execute(backgroundRunnable);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_add_player, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        if(itemId == R.id.action_add_player){
            Fragment addPersonFragment = new AddPlayerFragment();
            SSUtil.setFragment(requireActivity(), addPersonFragment, true);
        }else{
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}