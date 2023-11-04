import java.util.ArrayList;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;


        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room courtyard, theater, pub, lab, office, entranceHall, cellar;

        // create the rooms
        courtyard = new Room("standing in the courtyard");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        entranceHall = new Room("in the Entrance Hall the Exit is to the north but it seems locked! How do you get out?");
        cellar = new Room("in the dark dank cellar");

        Item key = new Item("A silver key", "Shiny! Could this be useful?");
        Item garbage = new Item("A pile of garbage", "Im not poor enough to pay any attention to that, clean your shit teacher.");
        cellar.addItem(key);
        cellar.addItem(garbage);

        // initialise room exits North, East, South, West
        courtyard.setExits("north", entranceHall);
        courtyard.setExits("east", theater);
        courtyard.setExits("west", pub);
        courtyard.setExits("south", lab);

        theater.setExits("west", courtyard);

        pub.setExits("east", courtyard);

        lab.setExits("north", courtyard);
        lab.setExits("east", office);

        cellar.setExits("up", office);

        office.setExits("west", lab);
        office.setExits("down", cellar);

        entranceHall.setExits("south", courtyard);

        currentRoom = courtyard;  // start game outside
    }

    private void look(){
        ArrayList<Item> roomItems = currentRoom.getItems();

        if (!roomItems.isEmpty()) {
            System.out.println("You see the following items in the room:");
            for (Item item : roomItems) {
                System.out.println(item.getItemName() + ": " + item.getItemDescription());
            }
        } else {
            System.out.println("There are no items in the room.");
        }
    }

    private void pickup(){
        System.out.println("You picked up nothing!");
    }
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("look")) {
            look();
        } else if(commandWord.equals("pickup")){
                pickup();
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.getExit("north");
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.getExit("east");
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.getExit("south");
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.getExit("west");
        }
        if(direction.equals("down")) {
            nextRoom = currentRoom.getExit("down");
        }
        if(direction.equals("up")) {
            nextRoom = currentRoom.getExit("up");
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            printLocationInfo();

    }

    }

    private void printLocationInfo() {
        System.out.println(currentRoom.getLongDescription());
    }
    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */

    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }


}
