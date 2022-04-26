package com.bruno13palhano.ssapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
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
import android.widget.TextView;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.MatchHelper;
import com.bruno13palhano.ssapp.MatchValues;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.activities.AllPlayersActivity;
import com.bruno13palhano.ssapp.adapters.MatchOneXOneListAdapter;
import com.bruno13palhano.ssapp.adapters.RotationMatchListAdapter;
import com.bruno13palhano.ssapp.data.Match;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.data.Rotation;
import com.bruno13palhano.ssapp.viewmodels.MatchViewModel;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;
import com.bruno13palhano.ssapp.viewmodels.RotationViewModel;
import com.bruno13palhano.ssapp.viewmodels.TestViewModel;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private MatchViewModel matchViewModel;
    private PlayerViewModel playerViewModel;
    private RotationViewModel rotationViewModel;

    private RecyclerView oneXOneRecycler, seriesRecycler, rotationRecycler;

    private MatchOneXOneListAdapter matchAdapter, seriesAdapter;
    private RotationMatchListAdapter rotationAdapter;

    private List<String> playersNamesList;
    private List<Player> playersList;
    private List<Match> matchesList, seriesList;
    private List<Rotation> playersRotationsList;

    private boolean matchInsertFlag, seriesInsertFlag, rotationUpdateFlag, rotationInsertFlag;
    private final Set<String> rotationPlayersNameSet = new HashSet<>();

    private List<Bitmap> playersBitmapImages;
    private List<Player> topFivePlayers = new ArrayList<>();
    private List<Player> playersRanking = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_home_fragment);

        // TODO: 20/04/2022 Integrar com o firebsae e usar Paging 3. Começar amanhã. Implementar uma API REST para testar o paging 3
        
        TextView oneXOneHeader = view.findViewById(R.id.match_recycler_header_one_versus_one);
        oneXOneRecycler = view.findViewById(R.id.match_recycler_one_versus_one);
        LinearLayoutManager matchLinearLayout = new LinearLayoutManager(getActivity());
        matchLinearLayout.setStackFromEnd(true);
        matchLinearLayout.setReverseLayout(true);
        matchLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        matchAdapter = new MatchOneXOneListAdapter(new MatchOneXOneListAdapter.MatchDiff());
        oneXOneRecycler.setAdapter(matchAdapter);
        oneXOneRecycler.setLayoutManager(matchLinearLayout);

        playersNamesList = new ArrayList<>();

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), players -> {
            playersList = players;

            playersNamesList = SSUtil.parseListPLayerNamesInBackground(playersList);
            sortPlayers(playersList);
        });

        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        matchViewModel.getAllRankMatches().observe(getViewLifecycleOwner(), matches -> {
            matchesList = matches;

            if(matches.size() < 1){
                oneXOneHeader.setVisibility(View.INVISIBLE);
            }else{
                oneXOneHeader.setVisibility(View.VISIBLE);
            }

            matchAdapter.submitList(matchesList);

            processMatchesBitmapsInBackground(matchesList, matchAdapter, oneXOneRecycler, matchInsertFlag);
            matchInsertFlag = false;

        });


        //area de testes
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Player> player = service.submit(
                new Callable<Player>() {
                    public Player call() {
                        return playersList.get(4);
                    }
                });
        Futures.addCallback(
                player,
                new FutureCallback<Player>() {
                    // we want this handler to run immediately after we push the big red button!
                    public void onSuccess(Player player) {
                        System.out.println("nome do jogador: "+player.getPlayerNickName());
                    }
                    public void onFailure(Throwable thrown) {
                        System.out.println("deu ruim");
                    }
                },
                service);
        //area de testes

        matchAdapter.setOnLongItemClickListener(new MatchOneXOneListAdapter.OnLongItemClickListener() {
            @Override
            public void itemLongClick(View v, int position) {
                v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(Menu.NONE, 1, Menu.NONE, R.string.label_menu_item_finish);
                        menu.add(Menu.NONE, 2, Menu.NONE, R.string.label_menu_item_delete);

                        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                MatchValues matchValues = new MatchValues();
                                matchValues.setMatchList(matchesList);
                                matchValues.setPlayerList(playersList);
                                matchValues.setPosition(position);

                                MatchHelper.finishOneXOneMatch(matchValues, playerViewModel, matchViewModel);

                                return true;
                            }
                        });

                        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                MatchValues matchValues = new MatchValues();
                                matchValues.setMatchList(matchesList);
                                matchValues.setPosition(position);

                                MatchHelper.deleteOneXOneMatch(matchValues, matchViewModel);

                                return true;
                            }
                        });
                    }
                });

                v.showContextMenu();
            }
        });

        matchAdapter.setFirstScoreClickListener(new MatchOneXOneListAdapter.OnPlusLessClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
                MatchValues matchValues = new MatchValues();
                matchValues.setMatchList(matchesList);
                matchValues.setPlayerList(playersList);
                matchValues.setNewScore(newScore);
                matchValues.setOldScore(oldScore);
                matchValues.setPosition(position);
                matchValues.setFirstPlayer(true);

                MatchHelper.updateMatchValues(matchValues, playerViewModel, matchViewModel);
            }
        });

        matchAdapter.setSecondScoreClickListener(new MatchOneXOneListAdapter.OnPlusLessClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
                MatchValues matchValues = new MatchValues();
                matchValues.setMatchList(matchesList);
                matchValues.setPlayerList(playersList);
                matchValues.setNewScore(newScore);
                matchValues.setOldScore(oldScore);
                matchValues.setPosition(position);
                matchValues.setFirstPlayer(false);

                MatchHelper.updateMatchValues(matchValues, playerViewModel, matchViewModel);
            }
        });

        TextView seriesHeader = view.findViewById(R.id.match_recycler_header_series);
        seriesRecycler = view.findViewById(R.id.match_recycler_series);
        seriesAdapter = new MatchOneXOneListAdapter(new MatchOneXOneListAdapter.MatchDiff());
        seriesAdapter.setSeriesLayout(true);
        seriesRecycler.setAdapter(seriesAdapter);
        LinearLayoutManager seriesLinearLayout = new LinearLayoutManager(getContext());
        seriesLinearLayout.setStackFromEnd(true);
        seriesLinearLayout.setReverseLayout(true);
        seriesLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        seriesRecycler.setLayoutManager(seriesLinearLayout);

        matchViewModel.getAllSeriesMatches().observe(getViewLifecycleOwner(), series ->{
            seriesList = series;

            if(series.size() < 1){
                seriesHeader.setVisibility(View.INVISIBLE);
            }else{
                seriesHeader.setVisibility(View.VISIBLE);
            }

            seriesAdapter.submitList(seriesList);

            processMatchesBitmapsInBackground(series, seriesAdapter, seriesRecycler, seriesInsertFlag);
            seriesInsertFlag = false;

        });

        seriesAdapter.setOnLongItemClickListener(new MatchOneXOneListAdapter.OnLongItemClickListener() {
            @Override
            public void itemLongClick(View v, int position) {
                v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(Menu.NONE, 1, Menu.NONE, R.string.label_menu_item_matches_per_series);
                        menu.add(Menu.NONE, 2, Menu.NONE, R.string.label_menu_item_finish);
                        menu.add(Menu.NONE, 3, Menu.NONE, R.string.label_menu_item_delete);

                        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                SelectSeriesDialog selectSeriesDialog = new SelectSeriesDialog();
                                selectSeriesDialog.show(requireActivity().getSupportFragmentManager(), "series");
                                selectSeriesDialog.setOnPositiveClickListener(new SelectSeriesDialog.NoticeNumberOfMatches() {
                                    @Override
                                    public void onDialogPositiveClick(int numberOfMatches) {
                                        matchViewModel.updateMatchesPerSeries(numberOfMatches, seriesList.get(position).getMatchId());
                                    }
                                });

                                return true;
                            }
                        });

                        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                MatchValues matchValues = new MatchValues();
                                matchValues.setMatchList(seriesList);
                                matchValues.setPlayerList(playersList);
                                matchValues.setPosition(position);

                                MatchHelper.finishMatchSeries(matchValues, playerViewModel, matchViewModel);
                                return true;
                            }
                        });

                        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                matchViewModel.deleteMatch(matchesList.get(position));
                                return true;
                            }
                        });
                    }
                });

                v.showContextMenu();

            }
        });

        seriesAdapter.setFirstScoreClickListener(new MatchOneXOneListAdapter.OnPlusLessClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
                MatchValues matchValues = new MatchValues();
                matchValues.setMatchList(seriesList);
                matchValues.setPlayerList(playersList);
                matchValues.setNewScore(newScore);
                matchValues.setOldScore(oldScore);
                matchValues.setPosition(position);
                matchValues.setFirstPlayer(true);

                MatchHelper.updateMatchValues(matchValues, playerViewModel, matchViewModel);
            }
        });

        seriesAdapter.setSecondScoreClickListener(new MatchOneXOneListAdapter.OnPlusLessClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
                MatchValues matchValues = new MatchValues();
                matchValues.setMatchList(seriesList);
                matchValues.setPlayerList(playersList);
                matchValues.setNewScore(newScore);
                matchValues.setOldScore(oldScore);
                matchValues.setPosition(position);
                matchValues.setFirstPlayer(false);

                MatchHelper.updateMatchValues(matchValues, playerViewModel, matchViewModel);
            }
        });

        seriesAdapter.setOnFirstPlayerSeriesListener(new MatchOneXOneListAdapter.OnViewUpdate() {
            @Override
            public void onViewUpdate(int score, int position) {
                matchViewModel.updateFirstPlayerScoreSeries(score, seriesList.get(position).getMatchId());

                matchViewModel.updateFirstPlayerScoreMatch(0, seriesList.get(position).getMatchId());
                matchViewModel.updateSecondPlayerScoreMatch(0, seriesList.get(position).getMatchId());
            }
        });

        seriesAdapter.setOnSecondPlayerSeriesListener(new MatchOneXOneListAdapter.OnViewUpdate() {
            @Override
            public void onViewUpdate(int score, int position) {
                matchViewModel.updateSecondPlayerScoreSeries(score, seriesList.get(position).getMatchId());

                matchViewModel.updateSecondPlayerScoreMatch(0, seriesList.get(position).getMatchId());
                matchViewModel.updateFirstPlayerScoreMatch(0, seriesList.get(position).getMatchId());
            }
        });

        TextView rotationHeader = view.findViewById(R.id.match_recycler_header_rotation);
        rotationRecycler = view.findViewById(R.id.match_recycler_rotation);
        rotationAdapter = new RotationMatchListAdapter(new RotationMatchListAdapter.PlayerDiff());
        rotationRecycler.setAdapter(rotationAdapter);
        LinearLayoutManager rotationLinearLayout = new LinearLayoutManager(getActivity());
        rotationLinearLayout.setStackFromEnd(true);
        rotationLinearLayout.setReverseLayout(true);
        rotationRecycler.setLayoutManager(rotationLinearLayout);

        rotationViewModel = new ViewModelProvider(this).get(RotationViewModel.class);
        rotationViewModel.getAllRotationPlayers().observe(getViewLifecycleOwner(), rotations -> {
            playersRotationsList = rotations;
            rotationAdapter.submitList(playersRotationsList);

            if(rotations.size() < 1){
                rotationHeader.setVisibility(View.INVISIBLE);
            }else{
                rotationHeader.setVisibility(View.VISIBLE);
            }

            processRotationInBackground(playersRotationsList);
        });

        // TODO: 25/04/2022 adicionar quantidade de pártidas..
        rotationAdapter.setOnScoreClickListener(new RotationMatchListAdapter.OnScoreClickListener() {
            @Override
            public void onClick(int oldScore, int newScore, int position) {
//                rotationViewModel.updateScoreRotationPlayer(newScore, playersRotationsList.get(position).getPlayerId());
                MatchValues matchValues = new MatchValues();
                matchValues.setNewScore(newScore);
                matchValues.setOldScore(oldScore);
                matchValues.setPosition(position);
                matchValues.setPlayerList(playersList);
                matchValues.setRotationList(playersRotationsList);

                MatchHelper.updateRotationMatchValues(matchValues, playerViewModel, rotationViewModel);

                rotationUpdateFlag = true;
            }
        });

        rotationAdapter.setOnLongItemClickListener(new RotationMatchListAdapter.OnLongItemClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(Menu.NONE, 1, Menu.NONE, R.string.label_menu_item_finish);
                        menu.add(Menu.NONE, 2, Menu.NONE, R.string.label_menu_item_delete);

                        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                finishRotationMatch(position);
                                return true;
                            }
                        });

                        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                deleteRotationMatch(position);
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
            mainActivity.disableUpButton();
        }

        setHasOptionsMenu(true);
        
        return view;
    }

    public void finishRotationMatch(int position){
        playerViewModel.updatePlayerScoreMatch(playersRotationsList.get(position).getScore(), playersRotationsList.get(position).getPlayerId());
        deleteRotationMatch(position);
    }

    public void deleteRotationMatch(int position){
        rotationViewModel.deleteRotationPlayer(playersRotationsList.get(position));
        rotationPlayersNameSet.remove(playersRotationsList.get(position).getNickname());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_home_more_vert, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_one_versus_one){
            SelectMatchFirstPlayer selectMatchMode = new SelectMatchFirstPlayer();
            selectMatchMode.setOnPositiveClickListener(new SelectMatchFirstPlayer.NoticeSelectMatchFirstPlayer() {
                @Override
                public void onDialogPositiveClick(int positionFirstPlayer, int positionSecondPlayer) {
                    Match match = new Match();
                    match.setFirstPlayerId(playersList.get(positionFirstPlayer).getPlayerId());
                    match.setFirstPlayerNickname(playersList.get(positionFirstPlayer).getPlayerNickName());
                    match.setFirstPlayerImageUri(playersList.get(positionFirstPlayer).getPlayerImageUri());
                    match.setFirstPlayerScoreMatch(0);
                    match.setSecondPlayerId(playersList.get(positionSecondPlayer).getPlayerId());
                    match.setSecondPlayerNickname(playersList.get(positionSecondPlayer).getPlayerNickName());
                    match.setSecondPlayerImageUri(playersList.get(positionSecondPlayer).getPlayerImageUri());
                    match.setSecondPlayerScoreMatch(0);

                    matchViewModel.insertMatch(match);
                    matchInsertFlag = true;
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialogFragment) {

                }
            });
            selectMatchMode.setListPlayers(playersNamesList);
            selectMatchMode.show(requireActivity().getSupportFragmentManager(), "1x1");

            return true;

        }else if(id == R.id.action_series){
            SelectMatchFirstPlayer selectMatchFirstPlayer = new SelectMatchFirstPlayer();
            selectMatchFirstPlayer.setOnPositiveClickListener(new SelectMatchFirstPlayer.NoticeSelectMatchFirstPlayer() {
                @Override
                public void onDialogPositiveClick(int positionFirstPlayer, int positionSecondPlayer) {
                    Match match = new Match();
                    match.setSeries(true);
                    match.setFirstPlayerId(playersList.get(positionFirstPlayer).getPlayerId());
                    match.setFirstPlayerNickname(playersList.get(positionFirstPlayer).getPlayerNickName());
                    match.setFirstPlayerImageUri(playersList.get(positionFirstPlayer).getPlayerImageUri());
                    match.setFirstPlayerScoreMatch(0);
                    match.setFirstPlayerScoreSeries(0);
                    match.setSecondPlayerId(playersList.get(positionSecondPlayer).getPlayerId());
                    match.setSecondPlayerNickname(playersList.get(positionSecondPlayer).getPlayerNickName());
                    match.setSecondPlayerImageUri(playersList.get(positionSecondPlayer).getPlayerImageUri());
                    match.setSecondPlayerScoreMatch(0);
                    match.setSecondPlayerScoreSeries(0);

                    matchViewModel.insertMatch(match);
                    seriesInsertFlag = true;
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialogFragment) {

                }
            });

            selectMatchFirstPlayer.setListPlayers(playersNamesList);
            selectMatchFirstPlayer.show(requireActivity().getSupportFragmentManager(), "series");

            return true;

        }else if(id == R.id.action_rotation){
            SelectMatchPlayers selectMatchPlayers = new SelectMatchPlayers();
            selectMatchPlayers.setOnPositiveClickListener(new SelectMatchPlayers.NoticeSelectPlayers() {
                @Override
                public void onDialogPositiveClick(List<Player> players) {
                    for(int i = 0; i < players.size(); i++){
                        if(rotationPlayersNameSet.add(players.get(i).getPlayerNickName())){
                            Rotation rotation = new Rotation();
                            rotation.setPlayerId(players.get(i).getPlayerId());
                            rotation.setNickname(players.get(i).getPlayerNickName());
                            rotation.setImageUri(players.get(i).getPlayerImageUri());
                            rotation.setScore(0);

                            rotationViewModel.insertRotationPlayer(rotation);
                            rotationInsertFlag = true;
                        }
                    }
                }
            });
            selectMatchPlayers.setListPlayers(playersList);
            selectMatchPlayers.show(requireActivity().getSupportFragmentManager(), "rotation");

            return true;

        }else if(id == R.id.action_tournament){
            Fragment fragment = new TournamentFragment();
            SSUtil.setFragment(requireActivity(), fragment, true);

        }else if(id == R.id.action_players_info){
            Intent intent = new Intent(requireActivity(), AllPlayersActivity.class);
            startActivity(intent);

        }else if(id == R.id.action_top_five_ranking){
            TopFiveDialog topFiveDialog = new TopFiveDialog();
            topFiveDialog.setPlayersBitmapList(playersBitmapImages);
            topFiveDialog.setPlayerList(topFivePlayers);
            topFiveDialog.show(requireActivity().getSupportFragmentManager(), "top");

        }else if(id == R.id.action_ranking){
            RankingDialog rankingDialog = new RankingDialog();
            rankingDialog.setPlayersBitmapList(playersBitmapImages);
            rankingDialog.setPlayerList(playersRanking);
            rankingDialog.show(requireActivity().getSupportFragmentManager(), "ranking");
        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnSortPlayersListener{
        void onSorted(List<Player> players);
    }

    private void sortPlayers(List<Player> players){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler();

        final OnSortPlayersListener listener = new OnSortPlayersListener() {
            @Override
            public void onSorted(List<Player> players) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        processPlayersBitmaps(players);
                        executor.shutdown();
                    }
                });
            }
        };

        executor.execute(() -> {
            topFivePlayers.clear();
            playersRanking.clear();

            Player[] p = new Player[players.size()];
            for(int i = 0; i < p.length; i++){
                p[i] = players.get(i);
            }

            try {
                MatchHelper.quickSortPlayerList(p, 0, players.size() - 1);

            }catch (IndexOutOfBoundsException | NullPointerException ignored){

            }

            for (int i = p.length - 1; i >= 0; i--) {
                playersRanking.add(p[i]);

                if(p.length >= 5){
                    if(i >= p.length - 6){
                        topFivePlayers.add(p[i]);
                    }
                }else{
                    topFivePlayers.add(p[i]);
                }
            }

            listener.onSorted(playersRanking);
        });
    }

    public void onStop(){
        super.onStop();
        rotationUpdateFlag = false;
    }

    public interface OnProcessedListener{
        void onProcessed(List<Bitmap> firstList, List<Bitmap> secondList);
    }

    private void processMatchesBitmapsInBackground(final List<Match> matches, MatchOneXOneListAdapter matchAdapter,
                                                   RecyclerView oneXOneRecycler, boolean matchInsertFlag){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final OnProcessedListener listener = new OnProcessedListener() {
            @Override
            public void onProcessed(List<Bitmap> firstList, List<Bitmap> secondList) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        matchAdapter.setBitmaps(firstList, secondList);
                        matchAdapter.notifyDataSetChanged();

                        if(matchInsertFlag){
                            oneXOneRecycler.scrollToPosition(matchAdapter.getItemCount()-1);
                        }

                        executor.shutdown();
                    }
                });
            }
        };

        Runnable backgroundRunnable = new Runnable() {
            @Override
            public void run() {
                List<Bitmap> firstList = new ArrayList<>();
                List<Bitmap> secondList = new ArrayList<>();

                try {
                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    for (int i = 0; i < matches.size(); i++) {
                        String firstImageUri = matches.get(i).getFirstPlayerImageUri();
                        String secondImageUri = matches.get(i).getSecondPlayerImageUri();
                        requireContext().getApplicationContext().getContentResolver().takePersistableUriPermission(Uri.parse(firstImageUri), takeFlags);
                        requireContext().getApplicationContext().getContentResolver().takePersistableUriPermission(Uri.parse(secondImageUri), takeFlags);
                        firstList.add(SSUtil.decodeUri(requireContext(), Uri.parse(firstImageUri), 50));
                        secondList.add(SSUtil.decodeUri(requireContext(), Uri.parse(secondImageUri), 50));
                    }

                    listener.onProcessed(firstList, secondList);

                } catch (IllegalStateException  | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        executor.execute(backgroundRunnable);
    }

    public interface OnProcessedRotationListener{
        void onProcessed(List<Bitmap> playersBitmaps);
    }

    private void processRotationInBackground(final List<Rotation> rotations){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final OnProcessedRotationListener listener = new OnProcessedRotationListener() {
            @Override
            public void onProcessed(List<Bitmap> playersBitmaps) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        rotationAdapter.setBitmaps(playersBitmaps);
                        rotationAdapter.notifyDataSetChanged();

                        if(!rotationUpdateFlag) {
                            rotationAdapter.notifyItemRangeChanged(rotationAdapter.getItemCount() - 6, 6);
                        }

                        executor.shutdown();
                    }
                });
            }
        };

        Runnable backgroundRunnable = new Runnable() {
            @Override
            public void run() {
                List<Bitmap> playersBitmaps = new ArrayList<>();

                try{
                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    for(int i = 0; i < rotations.size(); i++){
                        String image = rotations.get(i).getImageUri();
                        requireContext().getApplicationContext().getContentResolver()
                                .takePersistableUriPermission(Uri.parse(image), takeFlags);
                        playersBitmaps.add(SSUtil.decodeUri(requireContext(), Uri.parse(image), 50));
                        rotationPlayersNameSet.add(rotations.get(i).getNickname());
                    }

                    listener.onProcessed(playersBitmaps);

                }catch (IllegalStateException | FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        };

        executor.execute(backgroundRunnable);
    }

    private void processPlayersBitmaps(final List<Player> players){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler();

        final PlayerFragment.OnProcessedListener listener = new PlayerFragment.OnProcessedListener(){
            @Override
            public void onProcessed(List<Bitmap> listBitmaps){
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        playersBitmapImages = listBitmaps;
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

}