/**
 * Save all the attributes data which Server needs to retrieve
 * In Server, using List (ArrayList) to save every saved user
 */
public class UserBean
{
    private String playerName;
    private int passcode;
    private int guesses;
    private String target;

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public int getPasscode()
    {
        return passcode;
    }

    public void setPasscode(int passcode)
    {
        this.passcode = passcode;
    }

    public int getGuesses()
    {
        return guesses;
    }

    public void setGuesses(int guesses)
    {
        this.guesses = guesses;
    }
}
