// Importing required packages

import java.util.*;         // java.util.random is used for generating random integers, float values etc.    
import java.awt.event.*;    // Used for event handling
import javax.swing.*;       // Used to design GUI in Java
import java.awt.*;

// Creating Class for running main() function
public class snakesAndLadders {
    public static void main(String[] args) {
        new startScreen();      // Display start screen
    }
}

// class startScreen used to display basic home screen consisting of three buttons - 'start', 'rules' and 'credits'.
class startScreen extends JFrame implements ActionListener {
    JButton start,rules,credits;
    JLabel snakeIm, ladderIm;
    Image icon = Toolkit.getDefaultToolkit().getImage("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\snakesAndLaddersIcon.png");   // Importing image for window Icon
    Font F = new Font("Arial", Font.BOLD, 15);
    startScreen() {
        snakeIm = new JLabel(new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\snake (Custom).jpg"));
        snakeIm.setBounds(0,20,250,130);
        ladderIm = new JLabel(new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\ladder (Custom).jpg"));
        ladderIm.setBounds(350,10,130,140);
        start = new JButton("PLAY");
        start.setBounds(243,30,80,30);
        start.setFont(F);
        start.addActionListener(this);
        rules = new JButton("RULES");
        rules.setBounds(237,65,90,30);
        rules.setFont(F);
        rules.addActionListener(this);
        credits = new JButton("CREDITS");
        credits.setBounds(230,100,110,30);
        credits.setFont(F);
        credits.addActionListener(this);
        add(snakeIm);
        add(ladderIm);
        add(start);
        add(rules);
        add(credits);
        setSize(500,200);
        setTitle("Snakes & Ladders.exe");
        setLayout(null);
        setVisible(true);
        setIconImage(icon);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            dispose();
            new players();  
        }
        if (e.getSource() == rules) {
            String msg = "How to play : \n\n-->Each player puts their counter on the first position.  \n-->Take it in turns to roll the dice. Move your counter forward the number of spaces shown on the dice.  \n-->If your counter lands at the bottom of a ladder, you can move up to the top of the ladder.  \n-->If your counter lands on the head of a snake, you must slide down to the bottom of the snake.  \n-->The first player to get to the square numbered 100 is the winner.  \n\nHave fun!\n\n";
            JOptionPane.showMessageDialog(this,msg);    // Displays the rules of the game when 'RULES' button is pressed.
        }
        if (e.getSource() == credits) {
            String msg = "------Snakes & Ladders------\n\n\n---Developed by : Anish Joshi---\n\n---Developed on : 31 Dec 2022---\n\n---Editor : Visual Studio Code---\n\n---GUI Developed using Java Swing---\n\nThank you for checking out my contribution :)    \n\n";
            JOptionPane.showMessageDialog(this,msg);    // Displays Developer Credits when 'CREDITS' button is pressed.
        }
    }
}

class players extends JFrame implements ActionListener {
    static int n;
    JComboBox<String> numOfP;   // JComboBox is used for creating dropdown of player count.
    JLabel l;
    final String[] nums;
    players() {
        nums = new String[] {"2","3","4"};
        numOfP = new JComboBox<>(nums);
        numOfP.addActionListener(this);
        l = new JLabel("Select number of players : ");
        add(l);
        add(numOfP);
        setSize(200,150);
        setLayout(new FlowLayout());
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == numOfP) {
            String str = new String();
            str = numOfP.getSelectedItem().toString();
            n = Integer.valueOf(str);
            dispose();
            new playBox();    // playBox constructor is called for actually starting the game.
        }
    }
}

// class playBox implements all the logical operation required for the game to run.
class playBox extends JFrame implements ActionListener {
    board b = new board();  // A fresh snakes and ladders board is displayed with initial position of all players set to zero.
    int num;
    static int turn = 0;
    static int[] playerPos = new int[players.n];
    ImageIcon pgp = new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\pgp (Custom).png");   // Importing images for each game piece by colour.
    ImageIcon ggp = new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\ggp (Custom).png");
    ImageIcon bgp = new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\bgp (Custom).png");
    ImageIcon ygp = new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\ygp (Custom).png");
    ImageIcon[] pieces = new ImageIcon[] {pgp,ggp,bgp,ygp};     // An array of each game piece image is created.

    // An immutable hashmap is created to store snake heads and tail positions.
    final Map<Integer,Integer> snakes = new HashMap<Integer,Integer>() {
        {
            put(27,5);      // head position of the snake is the key and its tail position is the value
            put(40,3);
            put(43,18);
            put(54,31);
            put(66,45);
            put(76,58);
            put(89,53);
            put(99,41);
        }
    };

    // An immutable hashmap is created to store ladder beginning and ending positions.
    final Map<Integer,Integer> ladders = new HashMap<Integer,Integer>() {
        {
            // put(1,38);      // Ladder beginning position is key and end position is value;
            put(4,25);
            put(13,46);
            put(33,49);
            put(42,63);
            put(62,81);
            put(74,92);
            // put(80,100);
        }
    };
    Random rand;    // An object of class Random is created which is helpful in generating random dice values.
    JButton dice, replay;
    JLabel l1,l2,l3,l4;
    JLabel[] playerList = new JLabel[players.n];
    Icon diceIm;
    Font F = new Font("Futura", Font.BOLD, 15);
    playBox() {
        num = players.n;
        for (int i=0;i<num;i++) {
            playerPos[i] = 0;
        }
        int y = 20;
        for (int i=0;i<num;i++) {
            playerList[i] = new JLabel("player"+Integer.toString(i+1));
            playerList[i].setBounds(20,y,80,20);
            add(playerList[i]);
            y += 30;
        }
        rand = new Random();
        diceIm = new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\dice.gif");  // Importing image of dice for JButton dice icon.
        dice = new JButton(diceIm);
        dice.setBounds(450,30,100,100);
        replay = new JButton("RE-PLAY");
        replay.setBounds(340,30,100,100);
        replay.setVisible(false);
        l1 = new JLabel("ROLL");
        l1.setFont(F);
        l1.setBounds(480,130,100,30);
        l2 = new JLabel();
        l2.setBounds(470,5,100,20);
        l3 = new JLabel("Turn of player 1");
        l3.setBounds(240,20,120,20);
        l4 = new JLabel();
        l4.setBounds(210,60,180,20);
        dice.addActionListener(this);
        replay.addActionListener(this);
        add(dice);
        add(replay);
        add(l1);
        add(l2);
        add(l3);
        add(l4);
        setSize(600,200);
        setLayout(null);
        setVisible(true);
        setLocation(650,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method returns random value in range([1,6]).

    public int getDiceVal() {
        int dv = rand.nextInt(6)+1;
        l2.setText("value : "+dv);
        return dv;
    }

    //  Returns the the turn of the player to roll the dice.

    public int checkTurn() {
        if (turn%num == 0) l4.setText("");
        if (turn%num == 0 && turn!=0) {
            b.labels[playerPos[0]].setIcon(null);
        }
        if (turn%num == 1 && turn!=1) {
            b.labels[playerPos[1]].setIcon(null);
        }
        if (turn%num == 2 && turn!=2) {
            b.labels[playerPos[2]].setIcon(null);
        }
        if (turn%num == 3 && turn!=3) {
            b.labels[playerPos[3]].setIcon(null);
        }
        int chance = (turn+1)%num;
        l3.setText("Turn of player "+(chance+1));   // Displays which player has to roll the dice.
        return turn%num;
    }

    // Method takes the player number and the dice value as input and updates the respective player positions.

    public void updatePlayerPos(int playerNo, int diceVal) {
        playerPos[playerNo] += diceVal;

        // Checks if the player position is on a snake or a ladder and updates player position accordingly.
        if (snakes.containsKey(playerPos[playerNo])) {
            l4.setText("player"+(playerNo+1)+" stepped on a snake");
            playerPos[playerNo] = snakes.get(playerPos[playerNo]);
        }
        if (ladders.containsKey(playerPos[playerNo])) {
            l4.setText("player"+(playerNo+1)+" found a ladder");
            playerPos[playerNo] = ladders.get(playerPos[playerNo]);
        }

        // Checks if the position of the current player has crossed the hundredth mark. If so then displays the winner in a message box.
        if (playerPos[playerNo]>=100) {
            b.labels[100].setIcon(pieces[playerNo]);
            dice.setEnabled(false);
            l2.setVisible(false);
            l3.setVisible(false);
            l4.setVisible(false);
            replay.setVisible(true);
            JOptionPane.showMessageDialog(this,"player"+(playerNo+1)+" is the Winner!");
            return;
        }
        b.labels[playerPos[playerNo]].setIcon(pieces[playerNo]);    // Displsying the game piece of the respective player on the respective player position.
        playerList[playerNo].setText("player"+(playerNo+1)+"-->"+playerPos[playerNo]);  // Displaying player position on JLabel of the playBox.
    }

    public void actionPerformed(ActionEvent e) {
        
        //  When the dice is rolled we get the dicevalue and update the playerpositions accordingly and increment the turn counter by 1.
        if (e.getSource() == dice) {
            int diceVal = getDiceVal();
            updatePlayerPos(checkTurn(), diceVal);
            turn++;
        }

        // The replay button kills all the previous windows sets the turn counter to zero and creates a new play box for a fresh game.
        if (e.getSource() == replay) {
            dispose();
            turn = 0;
            new playBox();
        }
    }
}

// class board displays a snakes and ladders board.

class board extends JFrame {
    JLabel[] labels = new JLabel[101];  /* An array of 101 JLabels is created to fit the tiled snakes and ladders board.
    These labels are used to access each position on the board and display the game piece of each player. */

    int[] sq = new int[] {100,99,98,97,96,95,94,93,92,91,81,82,83,84,85,86,87,88,89,90,80,79,78,77,76,75,74,73,72,71,61,62,63,64,65,66,67,68,69,70,60,59,58,57,56,55,54,53,52,51,41,42,43,44,45,46,47,48,49,50,40,39,38,37,36,35,34,33,32,31,21,22,23,24,25,26,27,28,29,30,20,19,18,17,16,15,14,13,12,11,1,2,3,4,5,6,7,8,9,10};
    Image icon = Toolkit.getDefaultToolkit().getImage("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\snakesAndLaddersIcon.png");
    board() {
        JLabel bg = new JLabel(new ImageIcon("S:\\.projects\\Microproject\\ADV JAVA\\Snakes-and-Ladders-Game-in-Java-Swing-GUI-main\\board1.png"));   // Importing board image.
        add(bg);
        for (int i : sq) {
            labels[i] = new JLabel(Integer.toString(i));
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        }
        for (int i : sq) {
            bg.add(labels[i]);
        }
        setLayout(new FlowLayout());
        bg.setLayout(new GridLayout(10,10));    // All the JLabels in the array labels are displayed in a grid layout to match the actual snakes and ladders board.
        setSize(640,675);
        setTitle("Snakes and Ladders");
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}