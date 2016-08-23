package com.example.user.cards;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Game  {

    private int mPot;
    private int mlastBet;
    private int mPlayerTurn;
    private int mCurrentPlayer;
    private int mPlayerStart;
    private int mFirstBet;
    private int mBetPlayer;
    private int mNoOfPlayers;
    private int mNoOfFoldedPlayers;
    private ArrayList< String > mSharedCards;
    private ArrayList< Player > mPlayers;
    private ArrayList< Player > mPlayerCell;
    private PlayerComparator mPlayerComparator;
    private KickerComparator mkickerComparator;
    private PlayerSorter mPlayerSorter;
    private Player mHandWinner;
    private Player mKickerWinner;

    public Game( int noOfPlayers ){
        mPot = 0;
        mPlayerTurn = 1;
        mPlayerStart = 1;
        mCurrentPlayer = 1;
        mFirstBet = 1;
        mNoOfPlayers = noOfPlayers;
        mNoOfFoldedPlayers = 0;
        mSharedCards = new ArrayList< String >();
        mPlayerComparator = new PlayerComparator();
        mkickerComparator = new KickerComparator();
        mPlayerSorter = new PlayerSorter();
        mPlayerCell = new ArrayList< Player >();

        mPlayers = new ArrayList< Player >();
        mlastBet = 0;
    }

    public Player getCurrentPlayer() {
        return mPlayers.get(0);
    }

    public int getArraySize() {
        return mPlayers.size();
    }

    public void addPlayerToGame( Player player ) {
        mPlayers.add(player);
    }

    public void turnEnd() {
        Collections.rotate(mPlayers, 3);
    }

    public void fold() {
        mPlayerCell.add(mPlayers.remove(0));
        turnEnd();
    }

    public void refillPlayerArray() {
        Integer number = mPlayerCell.size();
        if( mPlayerCell.size() > 0 ) {
            for( int i = 0; i < number; i ++ ) {
                mPlayers.add( mPlayerCell.remove( 0 ));
            }
        }
    }

    public String seePlayersArray() {
        Integer size = mPlayers.size();
        return size.toString();
    }

    public Player accessPlayer( int arrayIndex ) {
        return mPlayers.get(arrayIndex);
    }

    public int showPot() {
        return mPot;
    }

    public int seeCurrentPlayer() {
        return mCurrentPlayer;
    }

    public boolean playerCheck( Player player ) {
        if( player.number() == mPlayerTurn ) {
            return true;
        } else {
            return false;
        }
    }






    public int seePlayerStart() {
        return mPlayerStart;
    }

    public void resetPlayers() {
        Integer number = mPlayers.size();
        for( int i = 0; i < number; i ++ ) {
            mPlayers.remove(0);
        }
    }

    public void pickWinner() {

        ArrayList<Player> mWinnerArray = new ArrayList< Player >(mPlayers);

        Collections.sort(mWinnerArray, mPlayerComparator);

        Player firstPlayer = mWinnerArray.get( mWinnerArray.size() - 1 );
        Player secondPlayer = mWinnerArray.get( mWinnerArray.size() - 2 );

        Integer firstPlayerScore = firstPlayer.seeScore();
        Integer secondPlayerScore = secondPlayer.seeScore();

        if( firstPlayer.seeScore() == secondPlayer.seeScore() ) {
            for (int i = 0; i < mWinnerArray.size() - 1; i++) {

                firstPlayer = mWinnerArray.get(i);
                secondPlayer = mWinnerArray.get(i + 1);
                firstPlayerScore = firstPlayer.seeScore();
                secondPlayerScore = secondPlayer.seeScore();

                if ((int) firstPlayerScore == (int) secondPlayerScore) {
                    pickKicker();
                }

            }
        } else {
            mHandWinner = mWinnerArray.get( mWinnerArray.size() - 1 );
        }

    }

    public void pickKicker() {
        ArrayList<Player> mKickerArray  = new ArrayList<Player>(mPlayers);
        Collections.sort(mKickerArray, mkickerComparator);
        mKickerWinner = mKickerArray.get( mKickerArray.size() - 1 );
    }


    public void sortPlayers() {
        Collections.sort( mPlayers, mPlayerSorter );
    }


    public Player seeWinner() {

        return mHandWinner != null ? mHandWinner : mKickerWinner;
    }

    public void takeCard( String card ) {
        mSharedCards.add( card );
    }



    public void endTurn() {
        if( mCurrentPlayer == mNoOfPlayers ) {
            mCurrentPlayer = 1;
        } else {
            mCurrentPlayer += 1;
        }
    }

    public void endHand() {
        if( mCurrentPlayer == mNoOfPlayers ) {
            mPlayerStart = 1;
        } else {
            mPlayerStart += 1;
        }
    }

    public void passOverFoldedPlayer() {
        if( mCurrentPlayer == mPlayers.size() ) {
            mPlayerStart = 1;
        } else {
            mPlayerStart += 1;
        }
    }

    public void setFold( Player one, Player two, Player three, Player four ) {
        if( one.status() && ( one.number() == mPlayerStart ) ) {
            passOverFoldedPlayer();
        }
        if(two.status() && ( two.number() == mPlayerStart ) ) {
            passOverFoldedPlayer();
        }
        if( three.status() && ( three.number() == mPlayerStart  )) {
            passOverFoldedPlayer();
        }
        if( four.status() && ( four.number() == mPlayerStart ) ) {
            passOverFoldedPlayer();
        }
    }

    public void firstTurn( Player one, Player two, Player three, Player four ) {
        one.turnOffBigBlind();
        two.turnOffBigBlind();
        three.turnOffBigBlind();
        four.turnOffBigBlind();
        if( one.number() == mPlayerStart ) {
            one.smallBlind();
            addBet(one);
            turnEnd();
            two.bigBlind();
            two.turnOnBigBlind();
            addBet(two);
            turnEnd();
            three.setFirstBet();
            setCurrentPlayer(three);
        } else if ( two.number() == mPlayerStart ) {
            two.smallBlind();
            addBet(two);
            turnEnd();
            three.bigBlind();
            three.turnOnBigBlind();
            addBet(three);
            turnEnd();
            four.setFirstBet();
            setCurrentPlayer(four);
        } else if ( three.number() == mPlayerStart ) {
            three.smallBlind();
            addBet(three);
            turnEnd();
            four.bigBlind();
            four.turnOnBigBlind();
            addBet(four);
            turnEnd();
            one.setFirstBet();
            setCurrentPlayer( one );
        } else {
            four.smallBlind();
            addBet(four);
            turnEnd();
            one.bigBlind();
            one.turnOnBigBlind();
            addBet(one);
            turnEnd();
            two.setFirstBet();
            setCurrentPlayer( two );
        }
    }

    public void firstTurn( Player one, Player two, Player three ) {
        one.turnOffBigBlind();
        two.turnOffBigBlind();
        three.turnOffBigBlind();
        if( one.number() == mPlayerStart ) {
            one.smallBlind();
            addBet(one);
            endTurn();
            two.bigBlind();
            two.turnOnBigBlind();
            addBet(two);
            endTurn();
            three.setFirstBet();
            setCurrentPlayer( three );
        } else if ( two.number() == mPlayerStart ) {
            two.smallBlind();
            addBet(two);
            endTurn();
            three.bigBlind();
            three.turnOnBigBlind();
            addBet(three);
            endTurn();
            one.setFirstBet();
            setCurrentPlayer( one );
        } else {
            three.smallBlind();
            addBet(three);
            endTurn();
            one.bigBlind();
            one.turnOnBigBlind();
            addBet(one);
            endTurn();
            two.setFirstBet();
            setCurrentPlayer( two );
        }
    }

    public void firstTurn(Player one, Player two) {
        one.turnOffBigBlind();
        two.turnOffBigBlind();
        if( one.number() == mPlayerStart ) {
            one.smallBlind();
            addBet(one);
            endTurn();
            two.bigBlind();
            two.turnOnBigBlind();
            addBet(two);
            endTurn();
            one.setFirstBet();
            setCurrentPlayer( one );
        } else if ( two.number() == mPlayerStart ) {
            two.smallBlind();
            addBet(two);
            endTurn();
            one.bigBlind();
            one.turnOnBigBlind();
            addBet(one);
            endTurn();
            two.setFirstBet();
            setCurrentPlayer( two );
        }
    }

    public void setCurrentPlayer() {
        mCurrentPlayer = mFirstBet;
    }

    public void setCurrentPlayer( Player player ) {
        mCurrentPlayer = player.number();
    }

    public void megaCheck( Player one, Player two, Player three, Player four ) {
        setFold( one, two, three, four );
        if( one.status() && two.status() ) {
            firstTurn( three, four );
        } else if( one.status() && three.status() ) {
            firstTurn( two, four );
        } else if( one.status() && four.status() ) {
            firstTurn( two, three );
        } else if( two.status() && three.status() ) {
            firstTurn( one, four );
        } else if( two.status() && four.status() ) {
            firstTurn( one, three );
        } else if( three.status() && four.status() ) {
            firstTurn( one, two );
        } else if ( one.status() ) {
            firstTurn( two, three, four );
        } else if ( two.status() ) {
            firstTurn( one, three, four );
        } else if ( three.status() ) {
            firstTurn( one, two, four );
        } else if ( four.status() ) {
            firstTurn( one, two, three );
        } else {
            firstTurn( one, two, three, four );
        }
    }

    public Player turnCheck( Player player ) {
        if( player.number() != seeCurrentPlayer() ) {
            return null;
        } else {
            return player;
        }
    }

    public void foldMaster( Player one ) {
        if (seeCurrentPlayer() == one.number()){
            foldCheck(one);
            foldWin(one);
        }
    }

    public void foldCheck( Player player ) {
        if( player.status() ) {
            setFoldedPlayerCount();
//            endTurn();
        }
    }

    public void setFoldedPlayerCount() {
        Integer number = mCurrentPlayer;

        Log.d( "PLayer Folding: ", number.toString() );
        mNoOfFoldedPlayers += 1;
    }

    public int seeFolded() {
        return mNoOfFoldedPlayers;
    }

    public void foldWin( Player player ) {
        if( mNoOfFoldedPlayers == ( mNoOfPlayers - 1 )) {
            if( !player.status() ) {
                player.winChips( mPot );
            }
        }
    }

//    public ArrayList showPlayers() {
//        return mWinnerArray;
//    }

    public ArrayList seeHand() {
        return mSharedCards;
    }

    public void addBet( Player player ) {
        mPot += player.giveBet();
        mlastBet = player.giveBet();
    }

    public int turn() {
        return mPlayerTurn;
    }

    public int numberOfPlayers() {
        return mNoOfPlayers;
    }

    public void nextTurn() {
        if( mPlayerTurn == mNoOfPlayers ) {
            mPlayerTurn = 1;
        } else {
            mPlayerTurn += 1;
        }
    }

    public void startHand() {
        mPlayerTurn = mPlayerStart;
    }

    public void handWon( Player player ) {
        player.winChips( mPot );
//        if( mPlayerStart == mNoOfPlayers ) {
//            mPlayerStart = 1;
//        } else {
//            mPlayerStart += 1;
//        }
    }

    public int seeLastBet() {
        return mlastBet;
    }

    public void setLastBet() {
        mlastBet = 0;
    }

    public void setFirstBet( int playerNumber ) {
        if( mFirstBet == mNoOfPlayers ) {
            mFirstBet = 1;
        } else {
            mFirstBet += 1;
        }
    }

    public int seeFirstBet() {
        return mFirstBet;
    }

    public void setBetPlayer() {
        if( mPlayerStart + 2 > mNoOfPlayers ) {
            mBetPlayer = mPlayerStart + 1;
        }
    }

    public void resetBets() {
        mlastBet = 0;
    }

}













