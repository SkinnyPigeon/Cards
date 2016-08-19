public class Player {

  private int mChips;
  private int mBet;
  private int mSmallBlindValue;
  private int mBigBlindValue;
  private int mPlayerNumber;
  private boolean mFolded;
  private boolean mFirstToBet;

  public Player( int playerNumber ) {
    mPlayerNumber = playerNumber;
    mBet = 0;
    mSmallBlindValue = 5;
    mBigBlindValue = 10;
    mFolded = false;
    mFirstToBet = false;
  }

  public int seePlayerNo() {
    return mPlayerNumber;
  }

  public void setBet( int chips ) {
    mBet += chips;
  }

  public int seeBet() {
    return mBet;
  }

  public void smallBlind() {
    mBet = mSmallBlindValue;
  }

  public void bigBlind() {
    mBet = mBigBlindValue;
  }

  public void fold() {
    mFolded = true;
  }

  public boolean seeFolded() {
    return mFolded;
  }

  public void setFirstBet() {
    mFirstToBet = true;
  }

  public boolean seeFirstBet() {
    return mFirstToBet;
  }

}













