package com.bruno13palhano.ssapp;

import com.bruno13palhano.ssapp.data.Match;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.MatchViewModel;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;

import java.util.List;

public class MatchHelper {
    public static void updateMatchValues(MatchValues matchValues, PlayerViewModel playerViewModel, MatchViewModel matchViewModel){
        int newScore = matchValues.getNewScore();
        int oldScore = matchValues.getOldScore();
        int scoreDifference = newScore - oldScore;
        int position = matchValues.getPosition();
        boolean isFirstPlayerScore = matchValues.isFirstPlayer();
        List<Player> playerList = matchValues.getPlayerList();
        List<Match> matchList = matchValues.getMatchList();

        long matchId = matchValues.getMatchList().get(position).getMatchId();
        long matchFirstPlayerId = matchValues.getMatchList().get(position).getFirstPlayerId();
        long matchSecondPlayerId = matchValues.getMatchList().get(position).getSecondPlayerId();

        int currentFirstPlayerNumberOfMatches = setCurrentPlayerNumberOfMatches(matchFirstPlayerId, playerList);
        int currentSecondPlayerNumberOfMatches = setCurrentPlayerNumberOfMatches(matchSecondPlayerId, playerList);

        if(isFirstPlayerScore){
            int currentFirstPlayerNumberOfWins = setCurrentPlayerNumberOfWins(matchFirstPlayerId, playerList);

            if(isUpdateFirstPlayerScoreValid(matchList, position, newScore)) {
                matchViewModel.updateFirstPlayerScoreMatch(newScore, matchId);

                int newFirstPlayerNumberOfWins = setNewPlayerNumberOfWins(currentFirstPlayerNumberOfWins, scoreDifference);

                playerViewModel.updatePlayerNumberOfWins(newFirstPlayerNumberOfWins, matchList.get(position).getFirstPlayerId());

                int newFirstPlayerNumberOfMatches = setNewPlayerNumberOfMatches(currentFirstPlayerNumberOfMatches, scoreDifference);
                int newSecondPlayerNumberOfMatches = setNewPlayerNumberOfMatches(currentSecondPlayerNumberOfMatches, scoreDifference);

                playerViewModel.updatePlayerNumberOfMatches(newFirstPlayerNumberOfMatches, matchList.get(position).getFirstPlayerId());
                playerViewModel.updatePlayerNumberOfMatches(newSecondPlayerNumberOfMatches, matchList.get(position).getSecondPlayerId());
            }

        }else{
            int currentSecondPlayerNumberOfWins = setCurrentPlayerNumberOfWins(matchSecondPlayerId, playerList);

            if(isUpdateSecondPlayerScoreValid(matchList, position, newScore)){
                matchViewModel.updateSecondPlayerScoreMatch(newScore, matchId);

                int newSecondPlayerNumberOfWins = setNewPlayerNumberOfWins(currentSecondPlayerNumberOfWins, scoreDifference);

                playerViewModel.updatePlayerNumberOfWins(newSecondPlayerNumberOfWins, matchList.get(position).getSecondPlayerId());

                int newFirstPlayerNumberOfMatches = setNewPlayerNumberOfMatches(currentFirstPlayerNumberOfMatches, scoreDifference);
                int newSecondPlayerNumberOfMatches = setNewPlayerNumberOfMatches(currentSecondPlayerNumberOfMatches, scoreDifference);

                playerViewModel.updatePlayerNumberOfMatches(newFirstPlayerNumberOfMatches, matchList.get(position).getFirstPlayerId());
                playerViewModel.updatePlayerNumberOfMatches(newSecondPlayerNumberOfMatches, matchList.get(position).getSecondPlayerId());
            }
        }
    }

    private static int setCurrentPlayerNumberOfWins(long matchPlayerId, List<Player> playersList){
        for(int i = 0; i < playersList.size(); i++){
            if(playersList.get(i).getPlayerId() == matchPlayerId){
                return playersList.get(i).getPlayerNumberOfWins();
            }
        }

        return 0;
    }

    private static int setCurrentPlayerNumberOfMatches(long matchPlayerId, List<Player> playersList){
        for(int i = 0; i < playersList.size(); i++){
            if(playersList.get(i).getPlayerId() == matchPlayerId){
                return playersList.get(i).getPlayerNumberOfMatches();
            }
        }

        return 0;
    }

    private static boolean isUpdateFirstPlayerScoreValid(List<Match> matchList, int position, int newScore){
        return !(matchList.get(position).getFirstPlayerScoreMatch() == 0 && newScore == 0);
    }

    private static boolean isUpdateSecondPlayerScoreValid(List<Match> matchList, int position, int newScore){
        return !(matchList.get(position).getSecondPlayerScoreMatch() == 0 && newScore == 0);
    }

    private static int setNewPlayerNumberOfMatches(int currentPlayerNumberOfMatches, int scoreDifference){
        if((currentPlayerNumberOfMatches + scoreDifference) < 0){
            return 0;
        }else{
            return currentPlayerNumberOfMatches + scoreDifference;
        }
    }

    private static int setNewPlayerNumberOfWins(int currentPlayerNumberOfWins, int scoreDifference){
        if((currentPlayerNumberOfWins + scoreDifference) < 0){
            return 0;
        }else{
            return scoreDifference + currentPlayerNumberOfWins;
        }
    }

    public static void quickSortPlayerList(Player[] players, int left, int right){
        int index = partition(players, left, right);

        if(left < index -1){
            quickSortPlayerList(players, left, index -1);
        }
        if(index < right){
            quickSortPlayerList(players, index, right);
        }
    }

    private static int partition(Player[] players, int left, int right){
        int pivot = players[(left + right) / 2].getPlayerScoreTotal();

        while(left <= right){

            while (players[left].getPlayerScoreTotal() < pivot)
                left++;

            while (players[right].getPlayerScoreTotal() > pivot)
                right--;

            if(left <= right){
                swap(players, left, right);
                left++;
                right--;
            }
        }

        return left;
    }

    private static void swap(Player[] players, int left, int right){
        Player aux = players[left];
        players[left] = players[right];
        players[right] = aux;
    }

    public static void finishMatchSeries(MatchValues matchValues, PlayerViewModel playerViewModel, MatchViewModel matchViewModel){
        long firstPlayerId = matchValues.getMatchList().get(matchValues.getPosition()).getFirstPlayerId();
        long secondPlayerId = matchValues.getMatchList().get(matchValues.getPosition()).getSecondPlayerId();
        int firstPlayerScoreSeries = matchValues.getMatchList().get(matchValues.getPosition()).getFirstPlayerScoreSeries();
        int firstPlayerScoreTotal = 0;
        int secondPlayerScoreSeries = matchValues.getMatchList().get(matchValues.getPosition()).getSecondPlayerScoreSeries();
        int secondPlayerScoreTotal = 0;

        boolean firstPlayerFlag = false, secondPlayerFlag = false;

        for(int i = 0; i < matchValues.getPlayerList().size(); i++){
            if(firstPlayerId == matchValues.getPlayerList().get(i).getPlayerId()){
                firstPlayerScoreTotal = matchValues.getPlayerList().get(i).getPlayerScoreTotal();
                firstPlayerFlag = true;
            }
            if(secondPlayerId == matchValues.getPlayerList().get(i).getPlayerId()){
                secondPlayerScoreTotal = matchValues.getPlayerList().get(i).getPlayerScoreTotal();
                secondPlayerFlag = true;
            }
            if(firstPlayerFlag && secondPlayerFlag){
                break;
            }
        }

        playerViewModel.updatePlayerScoreSeries(firstPlayerScoreSeries, firstPlayerId);
        playerViewModel.updatePlayerScoreSeries(secondPlayerScoreSeries, secondPlayerId);

        if(firstPlayerScoreSeries > secondPlayerScoreSeries){
            playerViewModel.updatePlayerScoreTotal((firstPlayerScoreTotal + SSUtil.RANK_MATCH_VALUE), firstPlayerId);

            if(secondPlayerScoreTotal >= SSUtil.RANK_MATCH_VALUE) {
                playerViewModel.updatePlayerScoreTotal((secondPlayerScoreTotal - SSUtil.RANK_MATCH_VALUE), secondPlayerId);
            }

        }else if(secondPlayerScoreSeries > firstPlayerScoreSeries){
            playerViewModel.updatePlayerScoreTotal((secondPlayerScoreTotal + SSUtil.RANK_MATCH_VALUE), secondPlayerId);

            if(firstPlayerScoreTotal >= SSUtil.RANK_MATCH_VALUE) {
                playerViewModel.updatePlayerScoreTotal((firstPlayerScoreTotal - SSUtil.RANK_MATCH_VALUE), firstPlayerId);
            }
        }

        matchViewModel.deleteMatch(matchValues.getMatchList().get(matchValues.getPosition()));
    }

    public static void finishOneXOneMatch(MatchValues matchValues, PlayerViewModel playerViewModel, MatchViewModel matchViewModel){
        long firstPlayerId = matchValues.getMatchList().get(matchValues.getPosition()).getFirstPlayerId(),
                secondPlayerId = matchValues.getMatchList().get(matchValues.getPosition()).getSecondPlayerId();

        int firstPlayerScoreMatch = matchValues.getMatchList().get(matchValues.getPosition()).getFirstPlayerScoreMatch(),
                secondPlayerScoreMatch = matchValues.getMatchList().get(matchValues.getPosition()).getSecondPlayerScoreMatch(),
                firstPlayerScoreTotal = 0, secondPlayerScoreTotal = 0;

        boolean firstPlayerFlag = false, secondPlayerFlag = false;
        for(int i = 0; i < matchValues.getPlayerList().size(); i++){
            if(matchValues.getPlayerList().get(i).getPlayerId() == firstPlayerId){
                firstPlayerScoreTotal = matchValues.getPlayerList().get(i).getPlayerScoreTotal();
                firstPlayerFlag = true;
            }
            if(matchValues.getPlayerList().get(i).getPlayerId() == secondPlayerId){
                secondPlayerScoreTotal = matchValues.getPlayerList().get(i).getPlayerScoreTotal();
                secondPlayerFlag = true;
            }
            if(firstPlayerFlag && secondPlayerFlag){
                break;
            }
        }

        if(firstPlayerScoreMatch > secondPlayerScoreMatch){
            playerViewModel.updatePlayerScoreTotal((SSUtil.RANK_MATCH_VALUE + firstPlayerScoreTotal), firstPlayerId);

            playerViewModel.updatePlayerScoreMatch(firstPlayerScoreMatch, firstPlayerId);
            playerViewModel.updatePlayerScoreMatch(secondPlayerScoreMatch, secondPlayerId);

            if(secondPlayerScoreTotal >= SSUtil.RANK_MATCH_VALUE){
                playerViewModel.updatePlayerScoreTotal((secondPlayerScoreTotal - SSUtil.RANK_MATCH_VALUE), secondPlayerId);
            }

        }else if(secondPlayerScoreMatch > firstPlayerScoreMatch){
            playerViewModel.updatePlayerScoreTotal((SSUtil.RANK_MATCH_VALUE + secondPlayerScoreTotal), secondPlayerId);

            playerViewModel.updatePlayerScoreMatch(firstPlayerScoreMatch, firstPlayerId);
            playerViewModel.updatePlayerScoreMatch(secondPlayerScoreMatch, secondPlayerId);

            if(firstPlayerScoreTotal >= SSUtil.RANK_MATCH_VALUE){
                playerViewModel.updatePlayerScoreTotal((firstPlayerScoreTotal - SSUtil.RANK_MATCH_VALUE), firstPlayerId);
            }
        }

        deleteOneXOneMatch(matchValues, matchViewModel);
    }

    public static void deleteOneXOneMatch(MatchValues matchValues, MatchViewModel matchViewModel){
        matchViewModel.deleteMatch(matchValues.getMatchList().get(matchValues.getPosition()));
    }
}
