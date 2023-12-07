/**
 * THIS IS NOT MY CODE - it is from San Diego State University.
 * I will be using this code as a crutch for my project. I do
 * not intend to take any credit for this code, nor is it required
 * that I create my own code, as seen in the prompt: "Given a portion
 * of a checkerboard, look for tilings of this checkerboard with
 * various types of polyominoes, including dominoes, the two types of
 * triominoes, and larger polyominoes." It very clearly states:
 *              "Given a portion of a checkerboard"
 * As no checkerboard was given, I must create my own, and as this is
 * not a coding class but instead a math class, I do not believe coding
 * a checkerboard game is within the scope of the project. As such, I
 * believe it is not only acceptable to use this code, but a necessity.
 * 
 * Now that I can get off my soap box, I must admit that I changed a
 * bit of this code, like the colors, for instance. They were very ugly
 * before, so I fixed them. They are also in this strange nested class
 * structure that I don't particularly enjoy, but its just a style choice
 * the original developer decided on that I'll have to live with, as I
 * lack the motivation to refactor it all. I have what I came for, a
 * dynamic checker board game that I can analyze and derive polyominoes
 * from. 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This panel lets two users play checkers against each other.
 * Red always starts the game. If a player can jump an opponent's
 * piece, then the player must jump. When a player can make no more
 * moves, the game ends.
 */
public class Checkerboard extends JPanel {

    // ---------------------------------------------------------------------

    private JButton upButton; // Button for increasing the order of the polyominoes.

    private JButton downButton; // Button that a player can use to decrease the order of the polyominoes.

    private JLabel message; // Label for displaying messages to the user.

    private int boardWidth, boardHeight;

    private Board board;

    private PolyominoBoard polyominoBoard;

    /**
     * The constructor creates the Board (which in turn creates and manages
     * the buttons and message label), adds all the components, and sets
     * the bounds of the components. A null layout is used. (This is
     * the only thing that is done in the main Checkers class.)
     */
    /**
     * CLASS BY NICO CABRERA
     */
    public Checkerboard(boolean bool, PolyominoBoard polyominoObject) {

        setLayout(null); // I will do the layout myself.

        if(bool){
            boardWidth = 164;
            boardHeight = boardWidth;
        }

        this.polyominoBoard = polyominoObject;

        /* Create the components and add them to the panel. */
        board = new Board(this.polyominoBoard, this); // Note: The constructor for the
                                   // board also creates the buttons
                                   // and label.
        add(board);
        if(bool){
            add(upButton);
            add(downButton);
            add(message);
        } else {
            boardWidth = 164*2;
            boardHeight = boardWidth;
        }

        Font text = new Font("Arial", Font.PLAIN, 10);

        /*
         * Set the position and size of each component by calling
         * its setBounds() method.
         */
        board.setBounds(20, 20, boardWidth, boardHeight); // Note: size MUST be 164-by-164 !
        if(bool){
            upButton.setBounds(20, 190, boardWidth, 30);
            upButton.setFont(text);
            downButton.setBounds(20, 225, boardWidth, 30);
            downButton.setFont(text);
            message.setBounds(20, 260, boardWidth, 30);
        }

    } // end constructor

    public Board getBoardObject(){
        return board;
    }

    public int[][] getBoard(){
        return board.board.getBoard();
    }

    public void setBoard(int board[][]){
        this.board.board.setBoard(board);
    }

    // -------------------- Nested Classes -------------------------------

    /**
     * A CheckersMove object represents a move in the game of Checkers.
     * It holds the row and column of the piece that is to be moved
     * and the row and column of the square to which it is to be moved.
     * (This class makes no guarantee that the move is legal.)
     */
    private static class CheckersMove {
        int fromRow, fromCol; // Position of piece to be moved.
        int toRow, toCol; // Square it is to move to.

        CheckersMove(int r1, int c1, int r2, int c2) {
            // Constructor. Just set the values of the instance variables.
            fromRow = r1;
            fromCol = c1;
            toRow = r2;
            toCol = c2;
        }

        boolean isJump() {
            // Test whether this move is a jump. It is assumed that
            // the move is legal. In a jump, the piece moves two
            // rows. (In a regular move, it only moves one row.)
            return (fromRow - toRow == 2 || fromRow - toRow == -2);
        }
    } // end class CheckersMove.

    /**
     * This panel displays a 160-by-160 checkerboard pattern with
     * a 2-pixel black border. It is assumed that the size of the
     * panel is set to exactly 164-by-164 pixels. This class does
     * the work of letting the users play checkers, and it displays
     * the checkerboard.
     */
    class Board extends JPanel implements ActionListener, MouseListener {

        CheckersData board; // The data for the checkers board is kept here.
                            // This board is also responsible for generating
                            // lists of legal moves.
        int order = 4;

        boolean gameInProgress; // Is a game currently in progress?

        /* The next three variables are valid only when the game is in progress. */

        int currentPlayer; // Whose turn is it now? The possible values
                           // are CheckersData.RED and CheckersData.BLACK.

        int selectedRow, selectedCol; // If the current player has selected a piece to
                                      // move, these give the row and column
                                      // containing that piece. If no piece is
                                      // yet selected, then selectedRow is -1.

        CheckersMove[] legalMoves; // An array containing the legal moves for the
                                   // current player.

        /**
         * Constructor. Create the buttons and label. Listens for mouse
         * clicks and for clicks on the buttons. Create the board and
         * start the first game.
         */
        Board(PolyominoBoard polyominoBoard, Checkerboard thisCheckerboard) {
            setBackground(Color.BLACK);
            addMouseListener(this);
            downButton = new JButton("Resign");
            downButton.addActionListener(this);
            upButton = new JButton("New Game");
            upButton.addActionListener(this);
            message = new JLabel("", JLabel.CENTER);
            message.setFont(new Font("Serif", Font.BOLD, 10));
            message.setForeground(Color.BLACK);
            board = new CheckersData();
            if (boardWidth == 164) { 
                polyominoBoard.showBoard(board.getBoard(), order);
                doNewGame(); 
            }
        }

        /**
         * Respond to user's click on one of the two buttons.
         */
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == upButton)
                doNewGame();
            else if (src == downButton)
                doResign();
        }

        public int getOrder() {
            return order;
        }

        /**
         * Start a new game
         */
        void doNewGame() {
            if (gameInProgress == true) {
                    // This should not be possible, but it doesn't hurt to check.
                message.setText("<html>Finish the current game first!</html>");
                return;
            }
            board.setUpGame();   // Set up the pieces.
            currentPlayer = CheckersData.RED;   // RED moves first.
            legalMoves = board.getLegalMoves(CheckersData.RED);  // Get RED's legal moves.
            selectedRow = -1;   // RED has not yet selected a piece to move.
            message.setText("<html>Red:  Make your move.</html>");
            gameInProgress = true;
            upButton.setEnabled(false);
            downButton.setEnabled(true);
            repaint();
        }

        /**
         * The game ends.  The parameter, str, is displayed as a message
         * to the user.  The states of the buttons are adjusted so players
         * can start a new game.  This method is called when the game
         * ends at any point in this class.
         */
        void gameOver(String str) {
            message.setText(str);
            upButton.setEnabled(true);
            downButton.setEnabled(false);
            gameInProgress = false;
        }

        /**
         * This is called by mousePressed() when a player clicks on the
         * square in the specified row and col. It has already been checked
         * that a game is, in fact, in progress.
         */
        void doClickSquare(int row, int col) {

            /*
             * If the player clicked on one of the pieces that the player
             * can move, mark this row and col as selected and return. (This
             * might change a previous selection.) Reset the message, in
             * case it was previously displaying an error message.
             */

            for (int i = 0; i < legalMoves.length; i++){
                if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                    selectedRow = row;
                    selectedCol = col;
                    if (currentPlayer == CheckersData.RED)
                        message.setText("<html>RED:  Make your move.</html>");
                    else
                        message.setText("<html>BLACK:  Make your move.</html>");
                    repaint();
                    return;
                }
            }

            /*
             * If no piece has been selected to be moved, the user must first
             * select a piece. Show an error message and return.
             */

            if (selectedRow < 0) {
                message.setText("<html>Click the piece you want to move.</html>");
                return;
            }

            /*
             * If the user clicked on a square where the selected piece can be
             * legally moved, then make the move and return.
             */

            for (int i = 0; i < legalMoves.length; i++){
                if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                        && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                    doMakeMove(legalMoves[i]);
                    return;
                }
            }

            /*
             * If we get to this point, there is a piece selected, and the square where
             * the user just clicked is not one where that piece can be legally moved.
             * Show an error message.
             */
            message.setText("<html>Not a legal move.</html>");


            /**         NICO NOTE
             * There is a slight logical error in this code that I dislike. When I click off
             * the piece, but it isn't a legal move, I want the selected piece to be
             * unselected. I accomplish this by reseting the state of the set, and repainting.
             */
            selectedRow = -1;       
            repaint();

        } // end doClickSquare()

        /**
         * This is called when the current player has chosen the specified
         * move. Make the move, and then either end or continue the game
         * appropriately.
         */
        void doMakeMove(CheckersMove move) {

            board.makeMove(move);
            polyominoBoard.showBoard(board.getBoard(), order);
            polyominoBoard.updatePolyominoBoard(board.getBoard(), order);

            /*
             * If the move was a jump, it's possible that the player has another
             * jump. Check for legal jumps starting from the square that the player
             * just moved to. If there are any, the player must jump. The same
             * player continues moving.
             */

            if (move.isJump()) {
                legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
                if (legalMoves != null) {
                    if (currentPlayer == CheckersData.RED)
                        message.setText("<html>RED:  You must continue jumping.</html>");
                    else
                        message.setText("<html>BLACK:  You must continue jumping.</html>");
                    selectedRow = move.toRow; // Since only one piece can be moved, select it.
                    selectedCol = move.toCol;
                    repaint();
                    return;
                }
            }

            /*
             * The current player's turn is ended, so change to the other player.
             * Get that player's legal moves. If the player has no legal moves,
             * then the game ends.
             */

            if (currentPlayer == CheckersData.RED) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("<html>BLACK has no moves.  RED wins.</html>");
                else if (legalMoves[0].isJump())
                    message.setText("<html>BLACK:  Make your move.  You must jump.</html>");
                else
                    message.setText("<html>BLACK:  Make your move.</html>");
            } else {
                currentPlayer = CheckersData.RED;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("<html>RED has no moves.  BLACK wins.</html>");
                else if (legalMoves[0].isJump())
                    message.setText("<html>RED:  Make your move.  You must jump.</html>");
                else
                    message.setText("<html>RED:  Make your move.</html>");
            }

            /*
             * Set selectedRow = -1 to record that the player has not yet selected
             * a piece to move.
             */
            selectedRow = -1;

            /*
             * As a courtesy to the user, if all legal moves use the same piece, then
             * select that piece automatically so the user won't have to click on it
             * to select it.
             */

            if (legalMoves != null) {
                boolean sameStartSquare = true;
                for (int i = 1; i < legalMoves.length; i++)
                    if (legalMoves[i].fromRow != legalMoves[0].fromRow
                            || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                        sameStartSquare = false;
                        break;
                    }
                if (sameStartSquare) {
                    selectedRow = legalMoves[0].fromRow;
                    selectedCol = legalMoves[0].fromCol;
                }
            }

            /* Make sure the board is redrawn in its new state. */
            repaint();
        } // end doMakeMove();

        /**
         * Current player resigns.  Game ends.  Opponent wins.
         */
        void doResign() {
            if (gameInProgress == false) {  // Should be impossible.
                message.setText("<html>There is no game in progress!</html>");
                return;
            }
            if (currentPlayer == CheckersData.RED)
                gameOver("<html>RED resigns.  BLACK wins.</html>");
            else
                gameOver("<html>BLACK resigns.  RED wins.</html>");
        }

        /**
         * Draw a checkerboard pattern in gray and lightGray. Draw the
         * checkers. If a game is in progress, hilite the legal moves.
         */
        public void paintComponent(Graphics g) {

            /* Turn on antialiasing to get nicer ovals. */

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            /* Draw a two-pixel black border around the edges of the canvas. */

            g.setColor(Color.BLACK);
            
            g.fillRect(0, 0, boardWidth, boardHeight);

            /* Draw the squares of the checkerboard and the checkers. */

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if(boardWidth == 164){
                        if (row % 2 == col % 2)
                            g.setColor(Color.LIGHT_GRAY);
                        else
                            g.setColor(Color.GRAY);
                        g.fillRect(2 + col * 20, 2 + row * 20, 20, 20);
                        switch (board.pieceAt(row, col)) {
                            case CheckersData.RED:
                                g.setColor(Color.RED);
                                g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                                break;
                            case CheckersData.BLACK:
                                g.setColor(Color.BLACK);
                                g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                                break;
                            case CheckersData.RED_KING:
                                g.setColor(Color.RED);
                                g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                                g.setColor(Color.WHITE);
                                g.drawString("K", 7 + col * 20, 16 + row * 20);
                                break;
                            case CheckersData.BLACK_KING:
                                g.setColor(Color.BLACK);
                                g.fillOval(4 + col * 20, 4 + row * 20, 15, 15);
                                g.setColor(Color.WHITE);
                                g.drawString("K", 7 + col * 20, 16 + row * 20);
                                break;
                        }
                    } else if(boardWidth == 164*2){         // NICO CODE HERE
                        if (row % 2 == col % 2) {
                            g.setColor(Color.LIGHT_GRAY);
                        } else{
                            g.setColor(Color.GRAY);
                        }
                        
                        g.fillRect((2 + col * 20)*2, (2 + row * 20)*2, 40, 40);
                        switch (board.pieceAt(row, col)) {
                            case CheckersData.RED:
                                g.setColor(Color.RED);
                                g.fillOval((4 + col * 20)*2, (4 + row * 20)*2, 30, 30);
                                break;
                            case CheckersData.BLACK:
                                g.setColor(Color.BLACK);
                                g.fillOval((4 + col * 20)*2, (4 + row * 20)*2, 30, 30);
                                break;
                            case CheckersData.RED_KING:
                                g.setColor(Color.RED);
                                g.fillOval((4 + col * 20)*2, (4 + row * 20)*2, 30, 30);
                                g.setColor(Color.WHITE);
                                g.drawString("K", ((7 + col * 20)*2)+5, ((16 + row * 20)*2)-5);
                                break;
                            case CheckersData.BLACK_KING:
                                g.setColor(Color.BLACK);
                                g.fillOval((4 + col * 20)*2, (4 + row * 20)*2, 30, 30);
                                g.setColor(Color.WHITE);
                                g.drawString("K", ((7 + col * 20)*2)+5, ((16 + row * 20)*2)-5);
                                break;
                        }
                    }
                }
            }

            if(boardWidth == 164*2){
                Polyomino polyomino = polyominoBoard.getPolyomino();
                for(ArrayList<Point> points : polyomino.getElements()){
                    g.setColor(Color.ORANGE);
                    int row = (int)points.get(0).getX();
                    int col = (int)points.get(0).getY();
                    if(points.size() == 1){
                        g.fillRect(((2 + col * 20) * 2) + 15, ((2 + row * 20) * 2) + 15, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawString(Integer.toString(polyominoBoard.getPolyominoAt(row, col)), ((7 + col * 20) * 2) + 6, ((16 + row * 20) * 2) - 4);
                    } else if (points.size() > 1){
                        Random random = new Random();
                        float red = random.nextFloat();
                        float green = random.nextFloat();
                        float blue = random.nextFloat();

                        g.setColor(new Color(red, green, blue));

                        double sumRow = 0;
                        double sumCol = 0;

                        for(Point point : points){
                            row = (int)point.getX();
                            col = (int)point.getY();

                            sumRow += row;
                            sumCol += col;

                            int x1 = (2 + col * 20)*2;
                            int y1 = (2 + row * 20)*2;

                            //check the points to the right, left, above, and below of the point.
                                // if the adjacent point is not a member of the polyomino, then a line is drawn.
                            if(!points.contains(new Point(row-1, col))){      //check above
                                g.fillRect(x1, y1, 40, 2);          //rectangles of thickness 2 are drawn to simulate thicker lines
                            }

                            if(!points.contains(new Point(row+1, col))){      //check below
                                g.fillRect(x1, y1+40, 40, 2);
                            }

                            if(!points.contains(new Point(row, col+1))){      //check right
                                g.fillRect(x1+39, y1, 2, 42);
                            }

                            if(!points.contains(new Point(row, col-1))){      //check left
                                g.fillRect(x1-1, y1, 2, 42);
                            }
                        }

                        double centerRow = sumRow/points.size();
                        double centerCol = sumCol/points.size();
                        
                        int magnitude = polyominoBoard.getPolyominoAt(row, col);
                        if(magnitude < 10){
                            g.fillRect((int)((2 + centerCol * 20) * 2) + 15, (int)((2 + centerRow * 20) * 2) + 15, 10, 10);
                        } else {
                            g.fillRect((int)((2 + centerCol * 20) * 2) + 15, (int)((2 + centerRow * 20) * 2) + 15, 16, 10);
                        }

                        g.setColor(Color.BLACK);
                        red*=255;
                        green*=255;
                        blue*=255;
                        float brightness = (red-green-blue);
                        if((blue < 75 || green < 50 || red < 75) && brightness < 50){
                            g.setColor(Color.WHITE);
                        }
                        g.drawString(Integer.toString(magnitude), (int)((7 + centerCol * 20)*2)+6, (int)((16 + centerRow * 20)*2)-4);
                    }
                }
            }
            

            // END NICO CODE

            /*
             * If a game is in progress, hilite the legal moves. Note that legalMoves
             * is never null while a game is in progress.
             */

            if (gameInProgress) {
                /* First, draw a 2-pixel cyan border around the pieces that can be moved. */
                g.setColor(Color.cyan);
                for (int i = 0; i < legalMoves.length; i++) {
                    g.drawRect(2 + legalMoves[i].fromCol * 20, 2 + legalMoves[i].fromRow * 20, 19, 19);
                    g.drawRect(3 + legalMoves[i].fromCol * 20, 3 + legalMoves[i].fromRow * 20, 17, 17);
                }
                /*
                 * If a piece is selected for moving (i.e. if selectedRow >= 0), then
                 * draw a 2-pixel white border around that piece and draw green borders
                 * around each square that that piece can be moved to.
                 */

                 //HERE
                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(2 + selectedCol * 20, 2 + selectedRow * 20, 19, 19);
                    g.drawRect(3 + selectedCol * 20, 3 + selectedRow * 20, 17, 17);
                    g.setColor(Color.green);
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.drawRect(2 + legalMoves[i].toCol * 20, 2 + legalMoves[i].toRow * 20, 19, 19);
                            g.drawRect(3 + legalMoves[i].toCol * 20, 3 + legalMoves[i].toRow * 20, 17, 17);
                        }
                    }
                }
            }

        } // end paintComponent()

        /**
         * Respond to a user click on the board. If no game is in progress, show
         * an error message. Otherwise, find the row and column that the user
         * clicked and call doClickSquare() to handle it.
         */
        public void mousePressed(MouseEvent evt) {
            if (gameInProgress == false)
                message.setText("<html>Click \"New Game\" to start a new game.</html>");
            else {
                int col = (evt.getX() - 2) / 20;
                int row = (evt.getY() - 2) / 20;
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    doClickSquare(row, col);
            }
        }

        public void mouseReleased(MouseEvent evt) {
        }

        public void mouseClicked(MouseEvent evt) {
        }

        public void mouseEntered(MouseEvent evt) {
        }

        public void mouseExited(MouseEvent evt) {
        }

    } // end class Board

    /**
     * An object of this class holds data about a game of checkers.
     * It knows what kind of piece is on each square of the checkerboard.
     * Note that RED moves "up" the board (i.e. row number decreases)
     * while BLACK moves "down" the board (i.e. row number increases).
     * Methods are provided to return lists of available legal moves.
     */
    private static class CheckersData {

        /*
         * The following constants represent the possible contents of a square
         * on the board. The constants RED and BLACK also represent players
         * in the game.
         */

        static final int EMPTY = 0,
                RED = 1,
                RED_KING = 2,
                BLACK = 3,
                BLACK_KING = 4;

        int[][] board; // board[r][c] is the contents of row r, column c.

        /**
         * Constructor. Create the board and set it up for a new game.
         */
        CheckersData() {
            board = new int[8][8];
            setUpGame();
        }

        /**         NICO CLASS
         * Returns an instance of the board.
         */
        int[][] getBoard(){
            return board;
        }

        void setBoard(int board[][]){
            this.board = board;
        }

        /**
         * Set up the board with checkers in position for the beginning
         * of a game. Note that checkers can only be found in squares
         * that satisfy row % 2 == col % 2. At the start of the game,
         * all such squares in the first three rows contain black squares
         * and all such squares in the last three rows contain red squares.
         */
        void setUpGame() {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2) {
                        if (row < 3)
                            board[row][col] = BLACK;
                        else if (row > 4)
                            board[row][col] = RED;
                        else
                            board[row][col] = EMPTY;
                    } else {
                        board[row][col] = EMPTY;
                    }
                }
            }
        } // end setUpGame()

        /**
         * Return the contents of the square in the specified row and column.
         */
        int pieceAt(int row, int col) {
            return board[row][col];
        }

        /**
         * Make the specified move. It is assumed that move
         * is non-null and that the move it represents is legal.
         */
        void makeMove(CheckersMove move) {
            makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
        }

        /**
         * Make the move from (fromRow,fromCol) to (toRow,toCol). It is
         * assumed that this move is legal. If the move is a jump, the
         * jumped piece is removed from the board. If a piece moves to
         * the last row on the opponent's side of the board, the
         * piece becomes a king.
         */
        void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
            board[toRow][toCol] = board[fromRow][fromCol];
            board[fromRow][fromCol] = EMPTY;
            if (fromRow - toRow == 2 || fromRow - toRow == -2) {
                // The move is a jump. Remove the jumped piece from the board.
                int jumpRow = (fromRow + toRow) / 2; // Row of the jumped piece.
                int jumpCol = (fromCol + toCol) / 2; // Column of the jumped piece.
                board[jumpRow][jumpCol] = EMPTY;
            }
            if (toRow == 0 && board[toRow][toCol] == RED)
                board[toRow][toCol] = RED_KING;
            if (toRow == 7 && board[toRow][toCol] == BLACK)
                board[toRow][toCol] = BLACK_KING;
        }

        /**
         * Return an array containing all the legal CheckersMoves
         * for the specified player on the current board. If the player
         * has no legal moves, null is returned. The value of player
         * should be one of the constants RED or BLACK; if not, null
         * is returned. If the returned value is non-null, it consists
         * entirely of jump moves or entirely of regular moves, since
         * if the player can jump, only jumps are legal moves.
         */
        CheckersMove[] getLegalMoves(int player) {

            if (player != RED && player != BLACK)
                return null;

            int playerKing; // The constant representing a King belonging to player.
            if (player == RED)
                playerKing = RED_KING;
            else
                playerKing = BLACK_KING;

            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>(); // Moves will be stored in this list.

            /*
             * First, check for any possible jumps. Look at each square on the board.
             * If that square contains one of the player's pieces, look at a possible
             * jump in each of the four directions from that square. If there is
             * a legal jump in that direction, put it in the moves ArrayList.
             */

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player || board[row][col] == playerKing) {
                        if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                            moves.add(new CheckersMove(row, col, row + 2, col + 2));
                        if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                            moves.add(new CheckersMove(row, col, row - 2, col + 2));
                        if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                            moves.add(new CheckersMove(row, col, row + 2, col - 2));
                        if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                            moves.add(new CheckersMove(row, col, row - 2, col - 2));
                    }
                }
            }

            /*
             * If any jump moves were found, then the user must jump, so we don't
             * add any regular moves. However, if no jumps were found, check for
             * any legal regular moves. Look at each square on the board.
             * If that square contains one of the player's pieces, look at a possible
             * move in each of the four directions from that square. If there is
             * a legal move in that direction, put it in the moves ArrayList.
             */

            if (moves.size() == 0) {
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board[row][col] == player || board[row][col] == playerKing) {
                            if (canMove(player, row, col, row + 1, col + 1))
                                moves.add(new CheckersMove(row, col, row + 1, col + 1));
                            if (canMove(player, row, col, row - 1, col + 1))
                                moves.add(new CheckersMove(row, col, row - 1, col + 1));
                            if (canMove(player, row, col, row + 1, col - 1))
                                moves.add(new CheckersMove(row, col, row + 1, col - 1));
                            if (canMove(player, row, col, row - 1, col - 1))
                                moves.add(new CheckersMove(row, col, row - 1, col - 1));
                        }
                    }
                }
            }

            /*
             * If no legal moves have been found, return null. Otherwise, create
             * an array just big enough to hold all the legal moves, copy the
             * legal moves from the ArrayList into the array, and return the array.
             */

            if (moves.size() == 0)
                return null;
            else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++)
                    moveArray[i] = moves.get(i);
                return moveArray;
            }

        } // end getLegalMoves

        /**
         * Return a list of the legal jumps that the specified player can
         * make starting from the specified row and column. If no such
         * jumps are possible, null is returned. The logic is similar
         * to the logic of the getLegalMoves() method.
         */
        CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
            if (player != RED && player != BLACK)
                return null;
            int playerKing; // The constant representing a King belonging to player.
            if (player == RED)
                playerKing = RED_KING;
            else
                playerKing = BLACK_KING;
            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>(); // The legal jumps will be stored in this
                                                                           // list.
            if (board[row][col] == player || board[row][col] == playerKing) {
                if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                    moves.add(new CheckersMove(row, col, row + 2, col + 2));
                if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                    moves.add(new CheckersMove(row, col, row - 2, col + 2));
                if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                    moves.add(new CheckersMove(row, col, row + 2, col - 2));
                if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                    moves.add(new CheckersMove(row, col, row - 2, col - 2));
            }
            if (moves.size() == 0)
                return null;
            else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++)
                    moveArray[i] = moves.get(i);
                return moveArray;
            }
        } // end getLegalMovesFrom()

        /**
         * This is called by the two previous methods to check whether the
         * player can legally jump from (r1,c1) to (r3,c3). It is assumed
         * that the player has a piece at (r1,c1), that (r3,c3) is a position
         * that is 2 rows and 2 columns distant from (r1,c1) and that
         * (r2,c2) is the square between (r1,c1) and (r3,c3).
         */
        private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

            if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
                return false; // (r3,c3) is off the board.

            if (board[r3][c3] != EMPTY)
                return false; // (r3,c3) already contains a piece.

            if (player == RED) {
                if (board[r1][c1] == RED && r3 > r1)
                    return false; // Regular red piece can only move up.
                if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
                    return false; // There is no black piece to jump.
                return true; // The jump is legal.
            } else {
                if (board[r1][c1] == BLACK && r3 < r1)
                    return false; // Regular black piece can only move downn.
                if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
                    return false; // There is no red piece to jump.
                return true; // The jump is legal.
            }

        } // end canJump()

        /**
         * This is called by the getLegalMoves() method to determine whether
         * the player can legally move from (r1,c1) to (r2,c2). It is
         * assumed that (r1,r2) contains one of the player's pieces and
         * that (r2,c2) is a neighboring square.
         */
        private boolean canMove(int player, int r1, int c1, int r2, int c2) {

            if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
                return false; // (r2,c2) is off the board.

            if (board[r2][c2] != EMPTY)
                return false; // (r2,c2) already contains a piece.

            if (player == RED) {
                if (board[r1][c1] == RED && r2 > r1)
                    return false; // Regular red piece can only move down.
                return true; // The move is legal.
            } else {
                if (board[r1][c1] == BLACK && r2 < r1)
                    return false; // Regular black piece can only move up.
                return true; // The move is legal.
            }

        } // end canMove()

    } // end class CheckersData

} // end class Checkers