package com.example.user.cards;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 19/08/2016.
 */
public class MainActivity extends AppCompatActivity{

    TextView mPlayerName;

    TextView mFlopOne;
    TextView mFlopTwo;
    TextView mFlopThree;
    TextView mTurn;
    TextView mRiver;

    Deck mDeck;

    TextView mPlayerCOne;
    TextView mPlayerCTwo;

    CheckBox mPlayerCardOne;
    CheckBox mPlayerCardTwo;

    CheckBox mCommunityCardOne;
    CheckBox mCommunityCardTwo;
    CheckBox mCommunityCardThree;
    CheckBox mCommunityCardFour;
    CheckBox mCommunityCardFive;


    ArrayList<String> mPlayerSelectedCards;
    ArrayList<String> mCommunitySelectedCards;

    Button mPlus;
    Button mCall;
    Button mBet;
    Button mStart;
    Button mCheck;
    Button mFold;

    TextView mPlayerReady;

    Button mShowHand;
    TextView mWinnerName;



    TextView mPlayerBet;

    TextView mPotValue;

    TextView mToCall;

    TextView mPlayerChips;

    Player mJeff;
    Player mSteve;
    Player mDave;
    Player mBob;

    Game mGame;
    TestCards mCards;

    private static Integer mBetValue;
    private static Integer mCounter;
    private static Integer mCheckCounter;
    private static Integer mHandsShown;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerCardOne = ( CheckBox )findViewById( R.id.player_card_one );
        mPlayerCardTwo = ( CheckBox )findViewById( R.id.player_card_two );

        mCommunityCardOne = ( CheckBox )findViewById( R.id.community_card_one );
        mCommunityCardTwo = ( CheckBox )findViewById( R.id.community_card_two );
        mCommunityCardThree = ( CheckBox )findViewById( R.id.community_card_three );
        mCommunityCardFour = ( CheckBox )findViewById( R.id.community_card_four );
        mCommunityCardFive = ( CheckBox )findViewById( R.id.community_card_five );

        mShowHand = ( Button )findViewById( R.id.win );

        mPlayerName = ( TextView )findViewById( R.id.player_name);

        mPlayerBet = (TextView)findViewById( R.id.player_bet );

        mPlayerCOne = (TextView )findViewById( R.id.player_cone );
        mPlayerCTwo = ( TextView )findViewById( R.id.player_ctwo );

        mPlayerChips = (TextView )findViewById( R.id.player_chips );

        mToCall = ( TextView )findViewById( R.id.to_call );

        mFlopOne = ( TextView )findViewById( R.id.flop_one );
        mFlopTwo = ( TextView )findViewById( R.id.flop_two );
        mFlopThree = (TextView )findViewById( R.id.flop_three );
        mTurn = ( TextView )findViewById( R.id.turn );
        mRiver = ( TextView )findViewById( R.id.river );


        mGame = new Game(4);

        mJeff = new Player("Jeff", 1);

        mSteve = new Player( "Steve", 2 );

        mDave = new Player( "Dave", 3 );

        mBob = new Player( "Bob", 4 );

        mGame.addPlayerToGame(mJeff);
        mGame.addPlayerToGame(mSteve);
        mGame.addPlayerToGame(mDave);
        mGame.addPlayerToGame(mBob);

        mPlus = ( Button )findViewById( R.id.plus );
        mCall = ( Button )findViewById( R.id.call );
        mCheck = ( Button )findViewById( R.id.check );
        mFold = ( Button )findViewById( R.id.fold );
        mWinnerName = ( TextView )findViewById( R.id.winner_name );

        mBet = ( Button )findViewById( R.id.bet );
        mPotValue = ( TextView )findViewById( R.id.pot );

        mStart = ( Button )findViewById( R.id.start );
        mPlayerReady = ( TextView )findViewById( R.id.player_ready );

        mPlayerSelectedCards = new ArrayList<String>();
        mCommunitySelectedCards = new ArrayList<String>();


        mBetValue = 0;
        mCounter = 2;
        mCheckCounter = 0;
        mHandsShown = 0;
        startHand();


        hideEverthing();

        mGame.firstTurn();
        Integer pot = mGame.showPot();
        String potText = "Pot: " + " " + pot.toString();
        mPotValue.setText(potText);

        showStart();


        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideStart();

                showEverything();

                setText();
                betweenPlayers();
            }
        });

        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( mBetValue <= ( mGame.getCurrentPlayer().countChips() - 10) ) {
                    mBetValue += 10;
                    String cash = "Bet: " + mBetValue.toString();
                    mPlayerBet.setText( cash );
                }
            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerCall(mGame.getCurrentPlayer());

                mGame.turnEnd();

                setText();
                mCounter++;
                checkCheck();
                Log.d("Call check: ", mGame.getCurrentPlayer().name());
                hideEverthing();
                showStart();
            }
        });

        mBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bet();
                setText();
                mCounter++;
                checkCheck();
                hideEverthing();
                showStart();
            }
        });

        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounter = 1;
                mCheckCounter ++;
                resetBets();
                stageCheck();
                mGame.turnEnd();
                mCheck.setVisibility(View.INVISIBLE);
                setText();
                hideEverthing();
                showStart();
            }
        });

        mFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGame.fold();

                if( mGame.getArraySize() == 1 ) {
                    mGame.handWon(mGame.getCurrentPlayer());
                    nextHand();
                } else {
                    mCounter ++;
                    checkCheck();
                    setText();
                    hideEverthing();
                    showStart();
                }
            }
        });


        mShowHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandsShown++;
                cardPickLogicCheck();

                if( mHandsShown == mGame.getArraySize() ) {
                    winner();
                } else {
                    unClick();
                    mGame.turnEnd();
                    setText();
                    hideEverthing();
                    showStart();

                }

            }

        });
    }

    public void playerCall( Player player ) {
        player.call( mGame );
        mGame.addBet(player);
        Integer potInt = mGame.showPot();
        String pot = "Pot: " + potInt.toString();
        mPotValue.setText( pot );
    }


    public void checkCheck() {

        if( mGame.showPot() > 0 && mGame.seeLastBet() <= mGame.getCurrentPlayer().seeLastBet()
                && mCounter >= mGame.getArraySize() ) {
            unBold();
            unClick();
            unDisable();
            mCheck.setVisibility(View.VISIBLE);
        } else {
            mCheck.setVisibility(View.INVISIBLE);
        }
    }

    public void resetBets() {
        for( int i = 0; i < mGame.getArraySize(); i ++ ) {
            mGame.accessPlayer(i).resetLastBet();
            mGame.accessPlayer(i).resetBet();
        }
        mGame.resetBets();
        mBetValue = 0;
    }

    public void cardPickLogicCheck() {

        Logic logic = new Logic( mPlayerSelectedCards, mCommunitySelectedCards );
        logic.combineCards();
        logic.setScore();
        mGame.getCurrentPlayer().awardScore(logic.seeScore());

        mGame.getCurrentPlayer().awardKicker(logic.seeKicker());

        Integer score = mGame.getCurrentPlayer().seeScore();
        Integer kicker = mGame.getCurrentPlayer().seeKicker();

        Log.d( "Score", score.toString() );
        Log.d( "Kicker", kicker.toString() );

        removePlayerSelectedCards();
        removeCommunitySelectedCards();
    }

    public void onPlayerSelect(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch ( view.getId() ) {
            case R.id.player_card_one:
                if( checked ) {
                    mPlayerSelectedCards.add(mGame.getCurrentPlayer().seeHand().get(0).toString());
                    mPlayerCOne.setTypeface(null, Typeface.BOLD);
                    mPlayerCardOne.setEnabled(false);
                }

                break;

            case  R.id.player_card_two:
                if( checked) {
                    Log.d("Here:", "Clicked ");
                    mPlayerSelectedCards.add(mGame.getCurrentPlayer().seeHand().get(1).toString()) ;
                    mPlayerCTwo.setTypeface(null, Typeface.BOLD);
                    mPlayerCardTwo.setEnabled( false );
                }
                break;
        }
    }

    public void onCommunitySelect( View view ) {
        boolean checked = ((CheckBox) view).isChecked();

        switch( view.getId() ) {
            case R.id.community_card_one:
                if( checked ) {
                    mCommunitySelectedCards.add(mGame.seeHand().get(0).toString());
                    mFlopOne.setTypeface(null, Typeface.BOLD);
                    mCommunityCardOne.setEnabled( false );
                }
                break;
            case R.id.community_card_two:
                if( checked ) {
                    mCommunitySelectedCards.add(mGame.seeHand().get(1).toString());
                    mFlopTwo.setTypeface(null, Typeface.BOLD);
                    mCommunityCardTwo.setEnabled(false);

                }
                break;
            case R.id.community_card_three:
                if( checked ) {
                    mCommunitySelectedCards.add( mGame.seeHand().get(2).toString() );
                    mFlopThree.setTypeface(null, Typeface.BOLD);
                    mCommunityCardThree.setEnabled(false);

                }
                break;
            case R.id.community_card_four:
                if( checked ) {
                    mCommunitySelectedCards.add( mGame.seeHand().get(3).toString() );
                    mTurn.setTypeface(null, Typeface.BOLD);
                    mCommunityCardFour.setEnabled(false);

                }
                break;
            case R.id.community_card_five:
                if( checked ) {
                    mCommunitySelectedCards.add( mGame.seeHand().get(4).toString() );
                    mRiver.setTypeface(null, Typeface.BOLD);
                    mCommunityCardFive.setEnabled(false);

                }
                break;
        }
    }

//need to check how to sort the call out for the score setter. perhaps reverse the order everything is called in"

    public void setText() {
        String name = mGame.getCurrentPlayer().name();
        mPlayerName.setText(name);

        Integer lastBet = mGame.seeLastBet() - mGame.getCurrentPlayer().seeLastBet();
        if( lastBet < 0 ) {
            lastBet = 0;
        }
        String lastBetText = "To Call: " + lastBet.toString();
        mToCall.setText( lastBetText );

        String cardOne = mGame.getCurrentPlayer().seeHand().get(0).toString();
        String cardTwo = mGame.getCurrentPlayer().seeHand().get(1).toString();


        int cardOneColor = colorChanger( cardOne );
        int cardTwoColor = colorChanger( cardTwo );

        mPlayerCOne.setText( cardOne );
        mPlayerCTwo.setText( cardTwo );

        mPlayerCOne.setTextColor(cardOneColor);
        mPlayerCTwo.setTextColor( cardTwoColor );

        Integer potInt = mGame.showPot();
        String pot = "Pot: " + potInt.toString();
        mPotValue.setText( pot );

        Integer betInt = mBetValue;
        String betText = "Bet: " + betInt.toString();
        mPlayerBet.setText(betText);

        Integer chipCount = mGame.getCurrentPlayer().countChips();
        String chips = "Chips: " + chipCount.toString();
        mPlayerChips.setText(chips);
    }

    public void bet() {
        int bet = mBetValue;
        mGame.getCurrentPlayer().placeBet(bet);
        mGame.addBet(mGame.getCurrentPlayer());
        mBetValue = 0;
        mGame.turnEnd();
    }

    public void nextHand() {

        mGame.refillPlayerArray();
        mGame.sortPlayers();
        mGame.resetHand();
        Log.d( "Game cards: ", mGame.seeHand().toString());
        mGame.endHand();

        Integer number = mGame.seePlayerStart();
        Log.d("Player to start:", number.toString());
        resetPlayerHands();
        resetBets();
        startHand();
        hideEverthing();
        betweenPlayers();
        mCheckCounter = 0;
        mHandsShown = 0;
        mCounter = 2;
        resetFlop();
        mGame.firstTurn();
        showStart();
        setText();
    }

    public void hideEverthing() {
        mPlus.setVisibility(View.INVISIBLE);
        mCall.setVisibility(View.INVISIBLE);
        mBet.setVisibility(View.INVISIBLE);
        mPotValue.setVisibility(View.INVISIBLE);
        mFlopOne.setVisibility(View.INVISIBLE);
        mFlopTwo.setVisibility(View.INVISIBLE);
        mFlopThree.setVisibility(View.INVISIBLE);
        mCheck.setVisibility(View.INVISIBLE);
        mFold.setVisibility(View.INVISIBLE);
        mTurn.setVisibility(View.INVISIBLE);
        mRiver.setVisibility(View.INVISIBLE);
        mToCall.setVisibility(View.INVISIBLE);

        mPlayerName.setVisibility(View.INVISIBLE);
        mPlayerBet.setVisibility(View.INVISIBLE);
        mPlayerCOne.setVisibility(View.INVISIBLE);
        mPlayerCTwo.setVisibility(View.INVISIBLE);
        mPlayerChips.setVisibility(View.INVISIBLE);
        mShowHand.setVisibility(View.INVISIBLE);

        mPlayerReady.setVisibility(View.INVISIBLE);
    }

    public void showEverything() {
        mPlus.setVisibility(View.VISIBLE);
        mCall.setVisibility(View.VISIBLE);
        mBet.setVisibility(View.VISIBLE);
        mPotValue.setVisibility(View.VISIBLE);
        mFold.setVisibility((View.VISIBLE));
        mFlopOne.setVisibility(View.VISIBLE);
        mFlopTwo.setVisibility(View.VISIBLE);
        mFlopThree.setVisibility(View.VISIBLE);
        mTurn.setVisibility(View.VISIBLE);
        mRiver.setVisibility(View.VISIBLE);
        mShowHand.setVisibility(View.VISIBLE);
        mToCall.setVisibility(View.VISIBLE);


        mPlayerName.setVisibility(View.VISIBLE);
        mPlayerBet.setVisibility(View.VISIBLE);
        mPlayerCOne.setVisibility(View.VISIBLE);
        mPlayerCTwo.setVisibility(View.VISIBLE);
        mPlayerChips.setVisibility(View.VISIBLE);

        checkCheck();
    }

    public void hideStart() {
        mPlayerReady.setVisibility(View.INVISIBLE);
        mStart.setVisibility(View.INVISIBLE);
    }

    public void showStart() {
        disable();
        hideEverthing();
        nextPlayerText();
        mPlayerReady.setVisibility(View.VISIBLE);
        mStart.setVisibility(View.VISIBLE);
    }

    public void flop() {
        mGame.takeCard(mDeck.deal());
        mGame.takeCard(mDeck.deal());
        mGame.takeCard(mDeck.deal());
        String mGameCardOne = mGame.seeHand().get(0).toString();
        String mGameCardTwo = mGame.seeHand().get(1).toString();
        String mGameCardThree = mGame.seeHand().get(2).toString();

        int cardOneColor = colorChanger( mGameCardOne );
        int cardTwoColor = colorChanger( mGameCardTwo );
        int cardThreeColor = colorChanger( mGameCardThree );

        mFlopOne.setText( mGameCardOne );
        mFlopTwo.setText( mGameCardTwo );
        mFlopThree.setText( mGameCardThree );

        mFlopOne.setTextColor(cardOneColor);
        mFlopTwo.setTextColor( cardTwoColor );
        mFlopThree.setTextColor( cardThreeColor );


    }

    public void turn() {
        mGame.takeCard(mDeck.deal());
        String mGameCardFour = mGame.seeHand().get(3).toString();
        int cardOneColor = colorChanger(mGameCardFour);

        mTurn.setText(mGameCardFour);
        mTurn.setTextColor( cardOneColor );
    }

    public void river() {
        mGame.takeCard(mDeck.deal());
        String mGameCardFive = mGame.seeHand().get(4).toString();
        int cardOneColor = colorChanger( mGameCardFive );

        mRiver.setText( mGameCardFive );
        mRiver.setTextColor( cardOneColor );
    }

    public void resetPlayerHands() {
        for( int i = 0; i < mGame.getArraySize(); i++ ) {
            mGame.accessPlayer(i).resetHand();
        }
    }

    public void startHand() {
//        mCards = new TestCards();
        mDeck = new Deck();
        mDeck.shuffle();
//        Shuffle the deck here

        for( int i = 0; i < mGame.getArraySize(); i ++ ) {
            mGame.accessPlayer(i).takeCard( mDeck.deal() );
            mGame.accessPlayer(i).takeCard(mDeck.deal());
        }
    }

    public void stageCheck() {
        if (mCheckCounter == 1) {
            flop();
        } else if ( mCheckCounter ==  2 ) {
            turn();
        } else if( mCheckCounter == 3 ) {
            river();
        }
    }

    public void removePlayerSelectedCards() {

        int size = mPlayerSelectedCards.size();
        if ( size > 0 ) {
            for( int i = 0; i < size; i++ ) {
                mPlayerSelectedCards.remove(0);
            }
        }
    }

    public void removeCommunitySelectedCards() {

        int size = mCommunitySelectedCards.size();
        if ( size > 0 ) {
            for( int i = 0; i < size; i++ ) {
                mCommunitySelectedCards.remove(0);
            }
        }
    }

    public void nextPlayerText() {
        String nextTurn = mGame.getCurrentPlayer().name();
        String player = nextTurn + " Ready? ";

        mPlayerReady.setText( player );
    }

    public void unBold() {
        mPlayerCOne.setTypeface( null, Typeface.NORMAL );
        mPlayerCTwo.setTypeface( null, Typeface.NORMAL );
        mFlopOne.setTypeface( null, Typeface.NORMAL );
        mFlopTwo.setTypeface( null, Typeface.NORMAL );
        mFlopThree.setTypeface( null, Typeface.NORMAL );
        mTurn.setTypeface( null, Typeface.NORMAL );
        mRiver.setTypeface(null, Typeface.NORMAL);
    }

    public void unClick() {
        if( mPlayerCardOne.isChecked() ) {
            mPlayerCardOne.toggle();
        }
        if( mPlayerCardTwo.isChecked() ) {
            mPlayerCardTwo.toggle();
        }
        if( mCommunityCardOne.isChecked() ) {
            mCommunityCardOne.toggle();
        }
        if( mCommunityCardTwo.isChecked() ) {
            mCommunityCardTwo.toggle();
        }
        if( mCommunityCardThree.isChecked() ) {
            mCommunityCardThree.toggle();
        }
        if( mCommunityCardFour.isChecked() ) {
            mCommunityCardFour.toggle();
        }
        if( mCommunityCardFive.isChecked() ) {
            mCommunityCardFive.toggle();
        }
    }

    public void unDisable() {
        mPlayerCardOne.setEnabled( true );
        mPlayerCardTwo.setEnabled( true );

        mCommunityCardOne.setEnabled( true );
        mCommunityCardTwo.setEnabled( true );
        mCommunityCardThree.setEnabled( true );
        mCommunityCardFour.setEnabled( true );
        mCommunityCardFive.setEnabled( true );
    }

    public void disable() {
        mPlayerCardOne.setEnabled( false );
        mPlayerCardTwo.setEnabled( false );

        mCommunityCardOne.setEnabled( false );
        mCommunityCardTwo.setEnabled( false );
        mCommunityCardThree.setEnabled( false );
        mCommunityCardFour.setEnabled( false );
        mCommunityCardFive.setEnabled( false );
    }

    public void betweenPlayers() {
        unBold();
        unClick();
        unDisable();
        removePlayerSelectedCards();
        removeCommunitySelectedCards();
    }

    public void winner() {
        mGame.pickWinner();
        Player winner = mGame.seeWinner();
        mGame.handWon(winner);
        mWinnerName.setText(mGame.seeWinner().name());

        mGame.resetWinner();
        nextHand();
    }

    public void resetFlop() {
        mFlopOne.setText("");
        mFlopTwo.setText("");
        mFlopThree.setText("");
        mTurn.setText("");
        mRiver.setText("");

    }

    public int colorChanger( String card ) {
        if( card.charAt( 1 ) == '♦' || card.charAt( 1 ) == '♥') {
            String red = "#d52828";
            return Color.parseColor( red );
        }
        if( card.length() > 2) {
           if( card.charAt( 2 ) == '♦' || card.charAt( 2 ) == '♥') {
               String red = "#d52828";
               return Color.parseColor( red );
           }
        }
        String black = "#2c2828";
        return Color.parseColor( black );
    }


}
