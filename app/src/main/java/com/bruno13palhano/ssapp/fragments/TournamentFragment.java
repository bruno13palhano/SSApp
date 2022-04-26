package com.bruno13palhano.ssapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.collection.ArraySet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.MatchHelper;
import com.bruno13palhano.ssapp.MatchValues;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.adapters.MatchOneXOneListAdapter;
import com.bruno13palhano.ssapp.data.Match;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.MatchViewModel;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TournamentFragment extends Fragment {
    private PlayerViewModel playerViewModel;
    private List<Player> playersList;
    private RecyclerView tournamentRecycler;
    private MatchOneXOneListAdapter tournamentAdapter;

    private MatchViewModel tournamentViewModel;
    private List<Match> tournamentMatchesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tournament, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_tournament_fragment);

        tournamentRecycler = view.findViewById(R.id.tournament_match_recycler);
        LinearLayoutManager tournamentLinearLayout = new LinearLayoutManager(getActivity());
        tournamentLinearLayout.setStackFromEnd(true);
        tournamentLinearLayout.setReverseLayout(true);

        tournamentAdapter = new MatchOneXOneListAdapter(new MatchOneXOneListAdapter.MatchDiff());
        tournamentRecycler.setAdapter(tournamentAdapter);
        tournamentRecycler.setLayoutManager(tournamentLinearLayout);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), players -> {
            playersList = players;
        });

        tournamentViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        tournamentViewModel.getAllTournamentMatches().observe(getViewLifecycleOwner(), matches -> {
            tournamentMatchesList = matches;
            tournamentAdapter.submitList(tournamentMatchesList);

            processBitmapsInBackground(tournamentMatchesList);
        });

        tournamentAdapter.setFirstScoreClickListener(new MatchOneXOneListAdapter.OnPlusLessClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
                MatchValues matchValues = new MatchValues();
                matchValues.setPlayerList(playersList);
                matchValues.setMatchList(tournamentMatchesList);
                matchValues.setPosition(position);
                matchValues.setOldScore(oldScore);
                matchValues.setNewScore(newScore);
                matchValues.setFirstPlayer(true);

                MatchHelper.updateMatchValues(matchValues, playerViewModel,tournamentViewModel);
            }
        });

        tournamentAdapter.setSecondScoreClickListener(new MatchOneXOneListAdapter.OnPlusLessClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
                MatchValues matchValues = new MatchValues();
                matchValues.setPlayerList(playersList);
                matchValues.setMatchList(tournamentMatchesList);
                matchValues.setPosition(position);
                matchValues.setOldScore(oldScore);
                matchValues.setNewScore(newScore);
                matchValues.setFirstPlayer(false);

                MatchHelper.updateMatchValues(matchValues, playerViewModel,tournamentViewModel);
            }
        });

        tournamentAdapter.setOnLongItemClickListener(new MatchOneXOneListAdapter.OnLongItemClickListener() {
            @Override
            public void itemLongClick(View v, int position) {
                v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(Menu.NONE, 1, Menu.NONE, R.string.label_menu_item_finish);
                        menu.add(Menu.NONE, 2, Menu.NONE, R.string.label_menu_item_edit);

                        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                setMatchStateChange(position, true);
                                finishPlayerScoreTotal(tournamentMatchesList.get(position), playersList);
                                return true;
                            }
                        });

                        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                setMatchStateChange(position, false);
                                editPlayerScoreTotal(tournamentMatchesList.get(position), playersList);
                                return true;
                            }
                        });
                    }
                });

                v.showContextMenu();
            }
        });

        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.enableUpButton();
        }

        setHasOptionsMenu(true);

        return view;
    }

    private void setMatchStateChange(int position, boolean finished){
        if(finished){
            tournamentViewModel.setMatchFinished(true, tournamentMatchesList.get(position).getMatchId());

        }else {
            tournamentViewModel.setMatchFinished(false, tournamentMatchesList.get(position).getMatchId());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_tournament_more_vert, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            requireActivity().onBackPressed();
            return true;

        }else if(id == R.id.action_tournament_new_tournament){
            SelectMatchPlayers selectTournamentPlayers = new SelectMatchPlayers();
            selectTournamentPlayers.setListPlayers(playersList);
            selectTournamentPlayers.setOnPositiveClickListener(new SelectMatchPlayers.NoticeSelectPlayers() {
                @Override
                public void onDialogPositiveClick(List<Player> players) {
                    defineMatches(players);
                }
            });
            selectTournamentPlayers.show(requireActivity().getSupportFragmentManager(), "tournament");
            return true;

        }else if(id == R.id.action_tournament_delete){
            deleteAllMatches(tournamentMatchesList);
        }

        return super.onOptionsItemSelected(item);
    }

    private void tournamentProgress(){
        if(tournamentMatchesList != null) {

            int countMatchFinished = 0;
            for (int i = 0; i < tournamentMatchesList.size(); i++) {
                if (tournamentMatchesList.get(i).isMatchFinished()) {
                    countMatchFinished++;
                }
            }

            if (countMatchFinished == tournamentMatchesList.size() && tournamentMatchesList.size() > 1) {
                List<Player> winners = defineWinners(tournamentMatchesList);

                deleteAllMatches(tournamentMatchesList);
                defineMatches(winners);
            }

            setTournamentWinner(countMatchFinished, playersList);
        }
    }

    private void setTournamentWinner(int countMatchFinished, List<Player> players){
        long winnerId;

        if (countMatchFinished == tournamentMatchesList.size() && tournamentMatchesList.size() == 1) {
            if (tournamentMatchesList.get(0).getFirstPlayerScoreMatch() > tournamentMatchesList.get(0).getSecondPlayerScoreMatch()) {
                winnerId = tournamentMatchesList.get(0).getFirstPlayerId();

            } else {
                winnerId = tournamentMatchesList.get(0).getSecondPlayerId();
            }

            updatePlayerScoreMatch(tournamentMatchesList.get(0));

            for(Player p: players){
                if(p.getPlayerId() == winnerId){
                    playerViewModel.updatePlayerWonTournaments((p.getPlayerWonTournaments() + SSUtil.TOURNAMENT_VALUE), winnerId);
                    break;
                }
            }

            deleteAllMatches(tournamentMatchesList);
        }
    }

    private void processBitmapsInBackground(List<Match> matches){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final OnProcessedListener listener = new OnProcessedListener() {
            @Override
            public void onProcessed(List<Bitmap> firstPlayerBitmaps, List<Bitmap> secondPlayerBitmaps) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tournamentAdapter.setBitmaps(firstPlayerBitmaps, secondPlayerBitmaps);
                        tournamentAdapter.notifyDataSetChanged();

                        executor.shutdown();
                    }
                });
            }
        };

        Runnable runnableBackground = new Runnable() {
            @Override
            public void run() {
                List<Bitmap> firstBitmapList = new ArrayList<>();
                List<Bitmap> secondBitmapList = new ArrayList<>();

                try{
                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    for(int i = 0; i < matches.size(); i++){
                        requireContext().getApplicationContext().getContentResolver()
                                .takePersistableUriPermission(Uri.parse(matches.get(i).getFirstPlayerImageUri()), takeFlags);
                        requireContext().getApplicationContext().getContentResolver()
                                .takePersistableUriPermission(Uri.parse(matches.get(i).getSecondPlayerImageUri()), takeFlags);
                        firstBitmapList.add(SSUtil.decodeUri(requireContext(), Uri.parse(matches.get(i).getFirstPlayerImageUri()), 50));
                        secondBitmapList.add(SSUtil.decodeUri(requireContext(), Uri.parse(matches.get(i).getSecondPlayerImageUri()), 50));
                    }

                    tournamentProgress();

                    listener.onProcessed(firstBitmapList, secondBitmapList);

                }catch (IllegalStateException | FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        };

        executor.execute(runnableBackground);
    }

    private interface OnProcessedListener{
        void onProcessed(List<Bitmap> firstPlayerBitmaps, List<Bitmap> secondPlayerBitmaps);
    }

    private List<Player> defineWinners(List<Match> matches){
        List<Player> players = new ArrayList<>();

        matches.forEach((match)->{
            updatePlayerScoreMatch(match);

            Player newPlayer = new Player("");

            if(match.getFirstPlayerScoreMatch() > match.getSecondPlayerScoreMatch()){
                newPlayer.setPlayerId(match.getFirstPlayerId());
                newPlayer.setPlayerNickName(match.getFirstPlayerNickname());
                newPlayer.setPlayerImageUri(match.getFirstPlayerImageUri());
            }else{
                newPlayer.setPlayerId(match.getSecondPlayerId());
                newPlayer.setPlayerNickName(match.getSecondPlayerNickname());
                newPlayer.setPlayerImageUri(match.getSecondPlayerImageUri());
            }

            newPlayer.setPlayerScoreMatch(0);
            players.add(newPlayer);
        });

        return players;
    }

    private void updatePlayerScoreMatch(Match match){
        playerViewModel.updatePlayerScoreMatch(match.getFirstPlayerScoreMatch(), match.getFirstPlayerId());
        playerViewModel.updatePlayerScoreMatch(match.getSecondPlayerScoreMatch(), match.getSecondPlayerId());
    }

    private void finishPlayerScoreTotal(Match match, List<Player> players){
        if(!match.isMatchFinished()) {
            for(Player p: players){
                if(match.getFirstPlayerScoreMatch() > match.getSecondPlayerScoreMatch()){
                    if (p.getPlayerId() == match.getFirstPlayerId()) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() + SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                    if (p.getPlayerId() == match.getSecondPlayerId() && p.getPlayerScoreTotal() > 0) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() - SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }

                }else{
                    if (p.getPlayerId() == match.getSecondPlayerId()) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() + SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                    if (p.getPlayerId() == match.getFirstPlayerId() && p.getPlayerScoreTotal() > 0) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() - SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                }
            }
        }
    }

    private void editPlayerScoreTotal(Match match, List<Player> players){
        if(match.isMatchFinished()){
            for(Player p: players){
                if(match.getFirstPlayerScoreMatch() > match.getSecondPlayerScoreMatch()){
                    if (p.getPlayerId() == match.getFirstPlayerId()) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() - SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                    if (p.getPlayerId() == match.getSecondPlayerId()) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() + SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                }else{
                    if (p.getPlayerId() == match.getSecondPlayerId()) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() - SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                    if (p.getPlayerId() == match.getFirstPlayerId()) {
                        playerViewModel.updatePlayerScoreTotal((p.getPlayerScoreTotal() + SSUtil.RANK_MATCH_VALUE), p.getPlayerId());
                    }
                }
            }
        }
    }

    private void defineMatches(List<Player> players){
        Random randomIndex = new Random();
        Set<Integer> indexSet = new ArraySet<>();
        int[] indexResult = new int[players.size()];
        int index;

        for(int i =0; i < players.size();) {
            index = randomIndex.nextInt(players.size());

            if (indexSet.add(index)) {
                indexResult[i] = index;
                i++;
            }
        }

        List<Match> matchList = new ArrayList<>();

        setMatch(players, matchList, indexResult);

        for(Match m: matchList)
            tournamentViewModel.insertMatch(m);

    }

    private void setMatch(List<Player> players, List<Match> matchList, int[] indexResult){
        if(indexResult.length % 2 == 0) {
            for (int i = 0; i < indexResult.length; i = i + 2) {
                Match newMatch = new Match();
                newMatch.setTournament(true);
                newMatch.setFirstPlayerId(players.get(indexResult[i]).getPlayerId());
                newMatch.setFirstPlayerNickname(players.get(indexResult[i]).getPlayerNickName());
                newMatch.setFirstPlayerImageUri(players.get(indexResult[i]).getPlayerImageUri());
                newMatch.setFirstPlayerScoreMatch(0);
                newMatch.setSecondPlayerId(players.get(indexResult[i + 1]).getPlayerId());
                newMatch.setSecondPlayerNickname(players.get(indexResult[i + 1]).getPlayerNickName());
                newMatch.setSecondPlayerImageUri(players.get(indexResult[i + 1]).getPlayerImageUri());
                newMatch.setSecondPlayerScoreMatch(0);

                matchList.add(newMatch);
            }
        }else{
            Match match = new Match();
            match.setTournament(true);
            match.setFirstPlayerId(players.get(indexResult[0]).getPlayerId());
            match.setFirstPlayerNickname(players.get(indexResult[0]).getPlayerNickName());
            match.setFirstPlayerImageUri(players.get(indexResult[0]).getPlayerImageUri());
            match.setFirstPlayerScoreMatch(0);
            match.setSecondPlayerId(players.get(indexResult[0]).getPlayerId());
            match.setSecondPlayerNickname(players.get(indexResult[0]).getPlayerNickName());
            match.setSecondPlayerImageUri(players.get(indexResult[0]).getPlayerImageUri());
            match.setSecondPlayerScoreMatch(0);

            matchList.add(match);

            for (int i = 1; i < indexResult.length; i = i + 2) {
                Match newMatch = new Match();
                newMatch.setTournament(true);
                newMatch.setFirstPlayerId(players.get(indexResult[i]).getPlayerId());
                newMatch.setFirstPlayerNickname(players.get(indexResult[i]).getPlayerNickName());
                newMatch.setFirstPlayerImageUri(players.get(indexResult[i]).getPlayerImageUri());
                newMatch.setFirstPlayerScoreMatch(0);
                newMatch.setSecondPlayerId(players.get(indexResult[i + 1]).getPlayerId());
                newMatch.setSecondPlayerNickname(players.get(indexResult[i + 1]).getPlayerNickName());
                newMatch.setSecondPlayerImageUri(players.get(indexResult[i + 1]).getPlayerImageUri());
                newMatch.setSecondPlayerScoreMatch(0);

                matchList.add(newMatch);
            }
        }
    }

    private void deleteAllMatches(List<Match> matches){
        for(Match m: matches)
            tournamentViewModel.deleteMatch(m);
    }

    public void onStop(){
        super.onStop();
    }

}