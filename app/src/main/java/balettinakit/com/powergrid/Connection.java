package balettinakit.com.powergrid;

/*
* created by Ilmari Ayres
*/

import java.io.*;
import java.net.*;

public class Connection
{
    public  enum POWER_STATE {POWER_OFF, POWER_QUEUED, POWER_GRANTED_ON, POWER_FORCED_ON, POWER_UNKNOWN};

    private Socket socket;

    private DataOutputStream outToServer;
    private BufferedReader inFromServer;

    private String token;

    public Connection(String host, int port) throws UnknownHostException, IOException
    {
        socket = new Socket(host, port);
        outToServer = new DataOutputStream(socket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Connect to a server and create a new instance of Connection.
     * @param host IP, URL or host name of the server
     * @param port Server port
     * @return Connection object (null if connection unsuccessful)
     */
    public static Connection ConnectionFactory(String host, int port)
    {
        try
        {
            return new Connection(host, port);
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
            return null;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Login to the server to get access to a house.
     * @param houseID Number of the house
     * @param password Password
     */
    public void login(int houseID, String password)
    {
        try
        {
            String toWrite = "Login\n" + (String.valueOf(houseID) + "\n") + ";\n";
            outToServer.write(toWrite.getBytes());

            token = inFromServer.readLine();

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of the names of the devices in the house you've logged in
     * to. House ID corresponds to its index in the array.
     * @return Names of the devices
     */
    public String[] houseGetDevices()
    {
        try
        {
            String toWrite = "getAppliances\n" + token + "\n" + ";\n";
            outToServer.write(toWrite.getBytes());

            int num = Integer.parseInt(inFromServer.readLine());

            String[] ret = new String[num];
            for(int i = 0; i < num; i++)
            {
                ret[i] = inFromServer.readLine();
            }

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());

            return ret;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the history of power usage every minute.
     * @param start First entry to send
     * @param end Last entry to send (use -1 to get the currently latest entry)
     * @return Power usage history. Array index corresponds to minutes.
     */
    public int[] houseGetHistory(int start, int end)
    {
        try
        {
            String toWrite = "getHistory\n" + token + "\n" + start + "\n" + end + "\n;\n";
            outToServer.write(toWrite.getBytes());

            int num = Integer.parseInt(inFromServer.readLine());

            int[] ret = new int[num];
            for(int i = 0; i < num; i++)
            {
                ret[i] = Integer.parseInt(inFromServer.readLine());
            }

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());

            return ret;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the power state of the device.
     * @param id Device id
     * @return Power state of the device
     */
    public POWER_STATE deviceGetPowerState(int id)
    {
        POWER_STATE ret = POWER_STATE.POWER_UNKNOWN;

        try
        {
            String toWrite = "getPower\n" + token + "\n" + id + "\n;\n";
            System.out.println(toWrite);
            outToServer.write(toWrite.getBytes());

            String state = inFromServer.readLine();

            switch(state)
            {
                case "off":
                    ret = POWER_STATE.POWER_OFF;
                    break;
                case "queue":
                    ret = POWER_STATE.POWER_QUEUED;
                    break;
                case "granted":
                    ret = POWER_STATE.POWER_GRANTED_ON;
                    break;
                case "forced":
                    ret = POWER_STATE.POWER_FORCED_ON;
                    break;
            }

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());

            return ret;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Puts the device in the turn on queue.
     * @param id Device ID
     */
    public void deviceQueue(int id)
    {
        try
        {
            String toWrite = "queue\n" + token + "\n" + id + "\n;\n";
            outToServer.write(toWrite.getBytes());

            inFromServer.readLine();

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Forces the device on.
     * @param id Device ID
     */
    public void deviceForceOn(int id)
    {
        try
        {
            String toWrite = "forceOn\n" + token + "\n" + id + "\n;\n";
            outToServer.write(toWrite.getBytes());

            inFromServer.readLine();

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Turns the device off.
     * @param id Device ID
     */
    public void deviceTurnOff(int id)
    {
        try
        {
            String toWrite = "turnOff\n" + token + "\n" + id + "\n;\n";
            outToServer.write(toWrite.getBytes());

            inFromServer.readLine();

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Returns the power handling tier of the device.
     * @param id Device ID
     * @return Power handling tier (1 to 3), 0 if error
     */
    public int deviceGetTier(int id)
    {
        int ret = 0;
        try
        {
            String toWrite = "getTier\n" + token + "\n" + id + "\n;\n";
            outToServer.write(toWrite.getBytes());

            ret = Integer.parseInt(inFromServer.readLine());

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }


    /**
     * Sets the power handling tier of the device.
     * @param id Device ID
     * @param tier handling tier (1 to 3)
     */
    public void deviceSetTier(int id, int tier)
    {
        try
        {
            String toWrite = "setTier\n" + token + "\n" + id + "\n" + tier + "\n;\n";
            outToServer.write(toWrite.getBytes());

            inFromServer.readLine();

            while(inFromServer.ready())
                System.out.println(inFromServer.readLine());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
