import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MyGameServer
{
    public static final int PORTNUMBER = 8080;

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(PORTNUMBER);
        List<UserBean> list = new ArrayList<UserBean>();

        // Keep the GameServer listening
        while (true)
        {
            // listening / blocked
            Socket socket = serverSocket.accept();

            new Thread(new Runnable()
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

                @Override
                public void run()
                {
                    try
                    {

                        while (true)
                        {
                            int guesses = 0;
                            boolean guessed = false;
                            // optionHint
                            out.println("Type any of the following options (FROM SERVER): ");
                            String option = in.readLine();
                            if ("NEWGAME".equalsIgnoreCase(option))
                            {
                                // inputNameHint
                                out.println("PLEASE GIVE YOUR NAME: ");

                                String playerName = in.readLine();


                                // NumCompare()
                                Random gen = new Random();
                                int target = 0;
                                while (hasDupes(target = (gen.nextInt(9000) + 1000))) ;
                                System.out.println("target: " + target); // target result
                                String targetStr = target + "";

                                while (guesses < 6)
                                {
                                    // welcomeHint
                                    out.println("Hi " + playerName + ", Guess your number. It is your " + (guesses + 1) + " ATTEMPT.");
                                    // inputDigits
                                    out.println("Guess a 4-digit number with no duplicate digits: ");
                                    // input
                                    String guessStr = in.readLine();

                                    /**
                                     * Logic of PAUSE
                                     */
                                    if ("PAUSE".equalsIgnoreCase(guessStr))
                                    {
                                        UserBean userBean = new UserBean();
                                        userBean.setGuesses(guesses);
                                        userBean.setPlayerName(playerName);
                                        userBean.setTarget(targetStr);
                                        int passcode = new Random().nextInt(900) + 100;
                                        userBean.setPasscode(passcode);
                                        out.println("GAME STATUS HAS BEEN SAVED. USE FOLLOWING PASSCODE TO RESUME: " + passcode);
                                        list.add(userBean);
                                        break;
                                    }
                                    try
                                    {
                                        if (hasDupes(Integer.parseInt(guessStr)) || Integer.parseInt(guessStr) < 1000)
                                            continue;
                                    } catch (NumberFormatException e)
                                    {
                                        continue;
                                    }
                                    guesses++; // Must after "PAUSE"
                                    int bulls = 0;
                                    int cows = 0;
                                    for (int j = 0; j < 4; j++)
                                    {
                                        if (guessStr.charAt(j) == targetStr.charAt(j))
                                        {
                                            bulls++;
                                        } else if (targetStr.contains(guessStr.charAt(j) + ""))
                                        {
                                            cows++;
                                        }
                                    }
                                    if (bulls == 4)
                                    {
                                        guessed = true;
                                        // guessed
                                        out.println(guessed);
                                        // resultInfo
                                        out.println("WELL DONE!! YOU HAVE GUESSED CORRECTLY IN " + guesses + " ATTEMPTS.");
                                        break; // jump out of the loop, so that's why need the boolean flag 'guessed', in case not to go in to the 'if' condition down below
                                    } else
                                    {
                                        // guessed
                                        out.println(guessed);
                                        // resultInfo
                                        out.println(cows + " Cows and " + bulls + " Bulls.");
                                    }
                                }
                                if (guessed == false && guesses == 6)
                                {
                                    // targetInfo
                                    out.println("SORRY YOU HAVE LOST THE GAME, CORRECT NUMBER IS: " + targetStr);
                                }
                                /**
                                 * Logic of PAUSE
                                 */
                                if (guessed == false && guesses < 6)
                                {
                                    continue;
                                }
                            } else if ("RESUME".equalsIgnoreCase(option))
                            {
                                // passcodeHint
                                out.println("Please input your passcode: ");
                                String inputPasscode = in.readLine();
                                boolean flagPasscode = false; // flag of passcode

                                for (UserBean userBean : list) // iterator
                                {
                                    try
                                    {
                                        // Case 1: can find the passcode
                                        if (userBean.getPasscode() == Integer.parseInt(inputPasscode))
                                        {
                                            // target number, get from "UserBean"
                                            String targetStr = userBean.getTarget();

                                            flagPasscode = true;
                                            // flag == true
                                            out.println(flagPasscode);

                                            /*
                                                coz when "PAUSE" "break;" the "for" loop, then goes into the "if" condition to "continue;"
                                                it will go up to the top while loop to initialize the guesses & guessed
                                                in the following code, "guesses" must be re-assigned
                                             */
                                            guesses = userBean.getGuesses();
                                            out.println(guesses);
                                            if (guesses == 6)
                                            {
                                                out.println("Sorry, You've used all [ 6 ] attempts!!! Please start your new game!!");
                                                break;
                                            }
                                            while (guesses < 6)
                                            {
                                                // resumeHint
                                                out.println("WELCOME BACK " + userBean.getPlayerName() + ". It is your ‘" + (guesses + 1) + "’ ATTEMPT.");
                                                // inputDigits
                                                out.println("Guess a 4-digit number with no duplicate digits: ");
                                                // input
                                                String guessStr = in.readLine();
                                                /**
                                                 * Logic of PAUSE
                                                 */
                                                if ("PAUSE".equalsIgnoreCase(guessStr))
                                                {
                                                    userBean.setGuesses(guesses);
                                                    int passcode = new Random().nextInt(900) + 100;
                                                    userBean.setPasscode(passcode);
                                                    out.println("GAME STATUS HAS BEEN SAVED. USE FOLLOWING PASSCODE TO RESUME: " + passcode);
                                                    break;
                                                }
                                                try
                                                {
                                                    if (hasDupes(Integer.parseInt(guessStr)) || Integer.parseInt(guessStr) < 1000)
                                                        continue;
                                                } catch (NumberFormatException e)
                                                {
                                                    continue;
                                                }
                                                guesses++; // Must after "PAUSE"
                                                int bulls = 0;
                                                int cows = 0;
                                                for (int j = 0; j < 4; j++)
                                                {
                                                    if (guessStr.charAt(j) == targetStr.charAt(j))
                                                    {
                                                        bulls++;
                                                    } else if (targetStr.contains(guessStr.charAt(j) + ""))
                                                    {
                                                        cows++;
                                                    }
                                                }
                                                if (bulls == 4)
                                                {
                                                    guessed = true;
                                                    // guessed
                                                    out.println(guessed);
                                                    // resultInfo
                                                    out.println("WELL DONE!! YOU HAVE GUESSED CORRECTLY IN " + guesses + " ATTEMPTS.");
                                                    break;
                                                } else
                                                {
                                                    // guessed
                                                    out.println(guessed);
                                                    // resultInfo
                                                    out.println(cows + " Cows and " + bulls + " Bulls.");
                                                }
                                            }
                                            if (guessed == false && guesses == 6)
                                            {
                                                // targetInfo
                                                out.println("SORRY YOU HAVE LOST THE GAME, CORRECT NUMBER IS: " + targetStr);
                                                userBean.setGuesses(guesses);
                                            }

                                            break; // do not need to keep iterating if found that user
                                        }
                                    } catch (NumberFormatException e)
                                    {
                                        break;
                                    } catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                                /**
                                 * Logic of PAUSE
                                 */
                                if (guessed == false && guesses < 6 && flagPasscode == true)
                                {
                                    continue;
                                }
                                // Case 2: can NOT find the passcode
                                if (flagPasscode == false)
                                {
                                    out.println(flagPasscode);
                                    // wrongPasscodeHint
                                    out.println("WRONG <OPTION/PASSCODE>!! PLEASE SPECIFY IT AGAIN");
                                    continue;
                                }
                            } else if ("EXIT".equalsIgnoreCase(option))
                            {
                                out.println("End game! Thanks for playing");
                                break; // jump out of the loop to 'finally', then close socket
                            } else
                            {
                                out.println("WRONG OPTION!! PLEASE SPECIFY OPTION AGAIN!!!");
                                continue;
                            }
                        }

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        try
                        {
                            in.close();
                            out.close();
                            socket.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    public static boolean hasDupes(int num)
    {
        boolean[] digs = new boolean[10];
        while (num > 0)
        {
            if (digs[num % 10]) return true;
            digs[num % 10] = true;
            num /= 10;
        }
        return false;
    }
}
