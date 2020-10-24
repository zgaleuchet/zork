package zork;import java.io.File;import java.io.FileReader;import java.io.IOException;import java.util.*;/** * Class Game - the main class of the "Zork" game. * * Author:  Michael Kolling * Version: 1.1 * Date:    March 2000 *  *  This class is the main class of the "Zork" application. Zork is a very *  simple, text based adventure game.  Users can walk around some scenery. *  That's all. It should really be extended to make it more interesting! *  *  To play this game, create an instance of this class and call the "play" *  routine. *  *  This main class creates and initialises all the others: it creates all *  rooms, creates the parser and starts the game.  It also evaluates the *  commands that the parser returns. */public class Game {		private Parser parser;	private boolean hasKey = false;	private boolean hasEaten = false;	private Room currentRoom;	private Room cell,corridor, kitchen, securityWing, court, exitGate;	private String username;	private StopWatch stopWatch = new StopWatch();	/**	 * Create the game and initialise its internal map.	 */	public Game() {				parser = new Parser();				// Create all the rooms and link their exits together.		cell = new Room("You are in your cell. \nThe guard dropped his key on the ground. Take it! (take key)");		cell.setKey(true);		corridor = new Room("You are in the corridor. \nThere’s nothing in this room.");		kitchen = new Room("You are in the kitchen. \nYou haven’t eaten in a long time, eat the apple. (eat apple/watermelon)");		securityWing = new Room("You are in the security wing. \nYou used your key to open this room. You have zero keys left. \nIn this room you find another key. Take it! (take key)");		securityWing.setKey(true);		securityWing.setClose(true);		court = new Room("You are in the court. \nThere’s nothing in this room.");		exitGate = new Room("You are at the exit gate. Congratulations! \nYou found the exit. You are free! (quit)");		exitGate.setClose(true);		// initialise room exits		cell.setExits(null, null, corridor, null);		corridor.setExits(cell, null, null, kitchen);		kitchen.setExits(null, corridor, securityWing, null);		securityWing.setExits(kitchen, null, court, null);		court.setExits(securityWing, exitGate, null, null);		exitGate.setExits(null, null, null, null);		currentRoom = corridor; // start game outside			}	/**	 *  Main play routine.  Loops until end of play.	 */	public void play() {		boolean hasEnteredName = false;		while (!hasEnteredName){			System.out.println("Please enter your Name:");			Scanner scanner = new Scanner(System.in);			this.username = scanner.nextLine();			hasEnteredName = true;		}		printWelcome();		stopWatch.start();		// Enter the main command loop.  Here we repeatedly read commands and		// execute them until the game is over.		boolean finished = false;		while (!finished) {			Command command = parser.getCommand();			finished = processCommand(command);		}		System.out.println("Thank you for playing.  Good bye.");		stopWatch.stop(username);		readLeaderboard();	}	/**	 * Print out the opening message for the player.	 */	private void printWelcome() {		System.out.println("\nWelcome to the Escape Room. \nYou are a Prisoner and you need to find the exit gate. \nFind keys to enter closed rooms. \nGood Luck!");		System.out.println(currentRoom.longDescription());	}	/**	 * Given a command, process (that is: execute) the command.	 * If this command ends the game, true is returned, otherwise false is	 * returned.	 */	private boolean processCommand(Command command) {		if (command.isUnknown()) {			System.out.println("I don't know what you mean...");			return false;		}		String commandWord = command.getCommandWord();		if (commandWord.equals("help")) {			printHelp();		}		else if (commandWord.equals("take")){			takeKey(command, currentRoom);		}		else if (commandWord.equals("eat")){			eat(command, currentRoom);		}		else if (commandWord.equals("go")) {			goRoom(command);		} else if (commandWord.equals("quit") ) {			if (command.hasSecondWord()) {				System.out.println("Quit what?");			} else {				return true; // signal that we want to quit			}		} else {			System.out.println("Don't give up now!");		}		return false;	}	/*	 * implementations of user commands:	 */ 	/**	 * Print out some help information.	 * Here we print some stupid, cryptic message and a list of the 	 * command words.	 */	private void printHelp() {		System.out.println("You are a prisoner, find the keys.");		System.out.println("There is a key in the North.");		System.out.println();		System.out.println("Your command words are:");		System.out.println(parser.showCommands());	}	/** 	 * Try to go to one direction. If there is an exit, enter the new	 * room, otherwise print an error message.	 */	private void goRoom(Command command) {		// if there is no second word, we don't know where to go...		if (!command.hasSecondWord()) {			System.out.println("Go where?");		} else {			String direction = command.getSecondWord();			// Try to leave current room.			Room nextRoom = currentRoom.nextRoom(direction);			if (nextRoom == null) {				System.out.println("There is no door!");			} else if (!nextRoom.isClosed()) {					currentRoom = nextRoom;					System.out.println(currentRoom.longDescription());				} else if (hasKey == true) {					nextRoom.setClose(false);					hasKey = false;					currentRoom = nextRoom;					System.out.println(currentRoom.longDescription());				} else if (hasKey == false){					System.out.println("You don’t have a key, find it to enter the room.");				}		}	}	private void takeKey(Command command, Room room){		if(command.getSecondWord().equals("key")){			if(room.getKey()){				String keyAsciiArt = "  .--.\n" +									 " /.-. '----------.\n" +									 " \\'-' .--\"--\"\"-\"-'\n" +									 "  '--'\n";				System.out.println(keyAsciiArt);				System.out.println("You took the key in this room.");				hasKey = true;				room.setKey(false);			}else{				System.out.println("There is no key in this room.");			}		} else {			System.out.println("You can only take keys.");		}	}	private void eat(Command command, Room room){		if (room == kitchen) {			if (command.getSecondWord().equals("apple")) {				stopWatch.eatApple();				System.out.println("" +						"        .:'\n" +						"    __ :'__\n" +						" .'`__`-'__``.\n" +						":__________.-'\n" +						":_________:\n" +						" :_________`-;\n" +						"  `.__.-.__.'");			} else if (command.getSecondWord().equals("watermelon")) {				stopWatch.eatWatermelon();				System.out.println(						"     ______\n" +								" .-'' ____ ''-.\n" +								"/.-=\"\"    \"\"=__\\_________\n" +								"|-===wwwwww|\\ , , , , , /|\n" +								"\\'-=,,____,,\\\\ ` ' ` ' //\n" +								" '-..______..\\'._____.'/\n" +								"              `'-----'`"				);			} else {				System.out.println("There is no such food in the kitchen. Find find something else.");			}		} else {			System.out.println("You can only eat in the kitchen.");		}	}	private void readLeaderboard(){		try {			String[] list = {""};			list = load_list("scoreboard.txt");			ArrayList<String> words = new ArrayList<String>(Arrays.asList(list));			Collections.sort(words, String.CASE_INSENSITIVE_ORDER);			for(String s: list){				System.out.println(s);			}		}		catch (IOException e) {		}	}	public static String[] load_list( String filename ) throws IOException {		File fileReader = new File( filename );		Scanner inputFile = new Scanner( fileReader );		List<String> L = new ArrayList<String>();		while ( inputFile.hasNextLine() ) {			L.add(inputFile.nextLine());		}		return L.toArray(new String[L.size()]);	}}