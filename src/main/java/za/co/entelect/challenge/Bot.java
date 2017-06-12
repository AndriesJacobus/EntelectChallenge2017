package za.co.entelect.challenge;

import com.google.gson.Gson;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.PlaceShipCommand;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Bot
{
    private String workingDirectory;
    private String key;
    private final String commandFileName = "command.txt";
    private final String placeShipFileName = "place.txt";
    private final String stateFileName = "state.json";
    private final Gson gson;

    public Bot(String key, String workingDirectory)
    {
        this.workingDirectory = workingDirectory;
        this.key = key;
        this.gson = new Gson();
    }

    public void execute() throws IOException
    {
        GameState gameState = gson.fromJson(new StringReader(loadState()), GameState.class);

        if (gameState.Phase == 1)
        {
            PlaceShipCommand placeShipCommand = placeShips(gameState);
            writePlaceShips(placeShipCommand);
        }
        else
        {
            Command command = makeMove(gameState);
            writeMove(command);
        }
    }

    private PlaceShipCommand placeShips(GameState state)
    {
        ArrayList<ShipType> shipsToPlace = new ArrayList<>();
        shipsToPlace.add(ShipType.Battleship);
        shipsToPlace.add(ShipType.Carrier);
        shipsToPlace.add(ShipType.Cruiser);
        shipsToPlace.add(ShipType.Destroyer);
        shipsToPlace.add(ShipType.Submarine);

        //decide on points
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Direction> directions = new ArrayList<>();
        if (state.MapDimension == 7)
        {
            points.add(new Point(0, 5));
            points.add(new Point(5, 1));
            points.add(new Point(0, 0));
            points.add(new Point(6, 0));
            points.add(new Point(6, 6));

            directions.add(Direction.East);
            directions.add(Direction.North);
            directions.add(Direction.North);
            directions.add(Direction.West);
            directions.add(Direction.West);
        }
        else if (state.MapDimension == 10)
        {
            points.add(new Point(8, 8));
            points.add(new Point(0, 8));
            points.add(new Point(0, 0));
            points.add(new Point(9, 9));
            points.add(new Point(5, 4));

            directions.add(Direction.South);
            directions.add(Direction.East);
            directions.add(Direction.East);
            directions.add(Direction.West);
            directions.add(Direction.North);
        }
        else if (state.MapDimension == 14)
        {
            points.add(new Point(12, 5));
            points.add(new Point(0, 12));
            points.add(new Point(5, 6));
            points.add(new Point(13, 13));
            points.add(new Point(5, 1));

            directions.add(Direction.North);
            directions.add(Direction.East);
            directions.add(Direction.North);
            directions.add(Direction.West);
            directions.add(Direction.East);
        }

        /*
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 0));
        points.add(new Point(3, 1));
        points.add(new Point(4, 2));
        points.add(new Point(7, 3));
        points.add(new Point(1, 8));

        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.North);
        directions.add(Direction.East);
        directions.add(Direction.North);
        directions.add(Direction.North);
        directions.add(Direction.East);
        */

        return new PlaceShipCommand(shipsToPlace, points, directions);
    }

    private PlaceShipCommand placeShips()
    {
        ArrayList<ShipType> shipsToPlace = new ArrayList<>();
        shipsToPlace.add(ShipType.Battleship);
        shipsToPlace.add(ShipType.Carrier);
        shipsToPlace.add(ShipType.Cruiser);
        shipsToPlace.add(ShipType.Destroyer);
        shipsToPlace.add(ShipType.Submarine);

        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 0));
        points.add(new Point(3, 1));
        points.add(new Point(4, 2));
        points.add(new Point(7, 3));
        points.add(new Point(1, 8));

        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.North);
        directions.add(Direction.East);
        directions.add(Direction.North);
        directions.add(Direction.North);
        directions.add(Direction.East);

        return new PlaceShipCommand(shipsToPlace, points, directions);
    }

    private String loadState() throws IOException
    {
        StringBuilder jsonText = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(workingDirectory, stateFileName)));
        String line = bufferedReader.readLine();
        while (line != null) {
            jsonText.append(line);
            jsonText.append("\r\n");
            line = bufferedReader.readLine();
        }
        return jsonText.toString();
    }

    private Command makeMove(GameState state)
    {
//        int possibleShipCommands = Code.values().length;
//        int lowerBounds = 0;
//        Code commandCode = Code.values()[ThreadLocalRandom.current().nextInt(lowerBounds, possibleShipCommands)];
//        int upperBounds = state.MapDimension;
//        int xCoord = ThreadLocalRandom.current().nextInt(lowerBounds, possibleShipCommands + upperBounds);
//        int yCoord = ThreadLocalRandom.current().nextInt(lowerBounds, possibleShipCommands + upperBounds);
//        return new Command(commandCode, xCoord, yCoord);

        int possibleShipCommands = Code.values().length;
        int lowerBounds = 0;
        Code commandCode = Code.FIRESHOT;
        int upperBounds = state.MapDimension;
        int xCoord = 0;
        int yCoord = 0;

        //======================Read opponenet map======================
        String opp = Arrays.toString(state.OpponentMap.Cells.toArray());
        opp = opp.substring(1, opp.length() - 1);
        //System.out.println("T: " + opp);

        String[] cs = opp.split(", ");
        //System.out.println("CS len: " + cs.length);
        String[][] map = new String[state.MapDimension][state.MapDimension];
        int c = state.MapDimension - 1;

        for (int i = 0; i < cs.length;)
        {
            for (int j = 0; j < state.MapDimension; j++)
            {
                map[c][j] = cs[i++];
                if (i >= cs.length)
                {
                    break;
                }
            }
            c--;
        }

        System.out.println("Map:");
        for (int i = 0; i < map.length; i++)
        {
            System.out.println(Arrays.toString(map[i]));
        }
        //======================Read opponenet map======================

        try
        {
            File f = new File("HMap " + state.MapDimension + "x" + state.MapDimension + ".txt");
            if(f.exists() && !f.isDirectory())
            {
                BufferedReader mapR = new BufferedReader(new FileReader(f));
                String line = mapR.readLine();
                int[][] heatMap;

                if (line != null)
                {
                    String[] mapLine = line.split(", ");
                    heatMap = new int[mapLine.length][mapLine.length];
                    c = 0;

                    while (line != null)
                    {
                        mapLine = line.split(", ");

                        for (int i = 0; i < mapLine.length; i++)
                        {
                            heatMap[c][i] = Integer.parseInt(mapLine[i]);
                        }

                        line = mapR.readLine();
                        c++;
                    }

//                    System.out.println("Heat map:\n");
//                    for (int i = 0; i < heatMap[0].length; i++)
//                    {
//                        System.out.println(Arrays.toString(heatMap[i]));
//                    }

                    //Now I have the Opponent Map as well as heat map.
                    //Strategy: fire at all heated places on map (most to least)

                    int val = 0;    //value of current highest heatMap point
                    int x = 0;      //x coord of current highest heatMap point
                    int y = 0;      //x coord of current highest heatMap point

                    for (int i = state.MapDimension - 1; i >= 0; i--)
                    {
                        for (int j = 0; j < state.MapDimension; j++)
                        {
                            if (heatMap[i][j] > val && cs[(state.MapDimension * j ) + (state.MapDimension - (i + 1))].equals("~"))
                            {
                                //biggest map coord that is open
                                val = heatMap[i][j];
                                x = j;
                                y = state.MapDimension - (i + 1);
                            }
                        }
                    }

                    xCoord = x;
                    yCoord = y;
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Command(commandCode, xCoord, yCoord);
    }

    private void writeMove(Command command) throws IOException
    {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, commandFileName)));
        bufferedWriter.write(command.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private void writePlaceShips(PlaceShipCommand placeShipCommand) throws IOException
    {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, placeShipFileName)));
        bufferedWriter.write(placeShipCommand.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private void log(String message)
    {
        System.out.println(String.format("[BOT]\t%s", message));
    }
}
