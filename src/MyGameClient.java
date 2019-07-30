import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MyGameClient
{
    public static final String GAMESERVERIP = "127.0.0.1"; // "localhost" as well


    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket(GAMESERVERIP, MyGameServer.PORTNUMBER);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

        while (true)
        {

            boolean guessed = false;
            int guesses = 0;

            // welcomeHint
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            System.out.println(in.readLine());

            // optionHint
            String optionHint = in.readLine();
            System.out.print(optionHint);

            String option = new Scanner(System.in).nextLine();
            // option
            out.println(option);


            /**
             * [ HELP ]
             */
            if ("HELP".equalsIgnoreCase(option))
            {
                System.out.println(in.readLine());
                System.out.println(in.readLine());
                System.out.println(in.readLine());
                System.out.println(in.readLine());
            }
            /**
             * [ NEW_GAME ]
             */
            else if ("NEWGAME".equalsIgnoreCase(option))
            {
                String inputNameHint = in.readLine();
                System.out.print(inputNameHint);

                String playerName = new Scanner(System.in).nextLine();
                out.println(playerName);

                while (guesses < 6)
                {
                    String welcomeHint = in.readLine();
                    System.out.println(welcomeHint);

                    String inputDigits = in.readLine();
                    System.out.print(inputDigits);

                    String input = new Scanner(System.in).nextLine();
                    // input
                    out.println(input);

                    // PAUSE
                    if ("PAUSE".equalsIgnoreCase(input))
                    {
                        String getPasscode = in.readLine();
                        System.out.println(getPasscode);
                        break; // jump out of the for loop
                    }
                    try
                    {
                        if (hasDupes(Integer.parseInt(input)) || Integer.parseInt(input) < 1000) continue;
                    } catch (NumberFormatException e)
                    {
                        continue;
                    }

                    guesses++;
                    String guessedStr = in.readLine();
                    String resultInfo;
                    if ("true".equalsIgnoreCase(guessedStr))
                    {
                        resultInfo = in.readLine();
                        System.out.println(resultInfo);
                        guessed = true;
                        break;
                    } else if ("false".equalsIgnoreCase(guessedStr))
                    {
                        resultInfo = in.readLine();
                        System.out.println(resultInfo);
                    }
                }

                /**
                 *  corresponding to the "if" condition in Server;
                 *  char stream will only readLine if the 'guessed' status is false
                 */
                if (guessed == false && guesses == 6)
                {
                    String targetInfo = in.readLine();
                    System.out.println(targetInfo);
                }

                /**
                 * Logic of PAUSE
                 */
                if (guessed == false && guesses < 6)
                {
                    continue;
                }
            }
            /**
             * [ RESUME ]
             */
            else if ("RESUME".equalsIgnoreCase(option))
            {
                String passcodeHint = in.readLine();
                System.out.print(passcodeHint);

                String inputPasscode = new Scanner(System.in).nextLine();
                out.println(inputPasscode);

                String flagPasscode = in.readLine();
                if ("true".equalsIgnoreCase(flagPasscode))
                {
                    String guessesStr = in.readLine();
                    guesses = Integer.parseInt(guessesStr);
                    if (guesses == 6)
                    {
                        String guessesFullInfo = in.readLine();
                        System.out.println(guessesFullInfo);
                        continue;
                    }
                    while (guesses < 6)
                    {
                        String resumeHint = in.readLine();
                        System.out.println(resumeHint);

                        String inputDigits = in.readLine();
                        System.out.print(inputDigits);

                        String input = new Scanner(System.in).nextLine();
                        // input
                        out.println(input);

                        // PAUSE
                        if ("PAUSE".equalsIgnoreCase(input))
                        {
                            String getPasscode = in.readLine();
                            System.out.println(getPasscode);
                            break;
                        }

                        try
                        {
                            if (hasDupes(Integer.parseInt(input)) || Integer.parseInt(input) < 1000)
                            {
                                continue;
                            }
                        } catch (NumberFormatException e)
                        {
                            continue;
                        }

                        guesses++;
                        String guessedStr = in.readLine();
                        String resultInfo;
                        if ("true".equalsIgnoreCase(guessedStr))
                        {
                            resultInfo = in.readLine();
                            System.out.println(resultInfo);
                            guessed = true;
                            break;
                        } else if ("false".equalsIgnoreCase(guessedStr))
                        {
                            resultInfo = in.readLine();
                            System.out.println(resultInfo);
                        }
                    }
                    /**
                     *  corresponding to the "if" condition in Server;
                     *  char stream will only readLine if the 'guessed' status is false
                     */
                    if (guessed == false && guesses == 6)
                    {
                        String targetInfo = in.readLine();
                        System.out.println(targetInfo);
                    }

                    /**
                     * Logic of PAUSE
                     */
                    if (guessed == false && guesses < 6)
                    {
                        continue;
                    }

                } else if ("false".equalsIgnoreCase(flagPasscode))
                {
                    String wrongPasscodeHint = in.readLine();
                    System.out.println(wrongPasscodeHint);
                    continue;
                }

            }
            /**
             * [ EXIT ]
             */
            else if ("EXIT".equalsIgnoreCase(option))
            {
                String endHint = in.readLine();
                System.out.println(endHint);
                in.close();
                out.close();
                socket.close();
                break;
            } else
            {
                // wrongOption
                String wrongOption = in.readLine();
                System.out.println(wrongOption);
                continue;
            }
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




