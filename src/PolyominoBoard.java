import java.awt.Point;

public class PolyominoBoard {
        private int board[][];
        private int polyominoOrderMap[][];
        private Checkerboard polyominoBoard;
        private boolean ready;
        private Polyomino polyomino;

        static final int EMPTY = 0,
                    RED = 1,
                    RED_KING = 2,
                    BLACK = 3,
                    BLACK_KING = 4;

        public PolyominoBoard(){
            polyominoOrderMap = new int[8][8];
            this.ready = false;
        }

        public void setPolyominoBoard(Checkerboard polyominoBoard, int order){
            this.polyominoBoard = polyominoBoard;
            this.board = this.polyominoBoard.getBoard();
            this.ready = false;
            this.getMaxOrderPolyomino(order);
        }

        public void updatePolyominoBoard(int board[][], int order){
            this.polyominoBoard.setBoard(board);
            this.polyominoBoard.repaint();
            this.board = this.polyominoBoard.getBoard();
            this.ready = false;
            this.getMaxOrderPolyomino(order);
        }

        public void getMaxOrderPolyomino(int order){
            polyominoOrderMap = new int[8][8];
            polyomino = new Polyomino(polyominoOrderMap, order);
            for(int row = 0; row < this.board.length; row++){
                for (int col = 0; col < this.board[row].length; col++) {
                    if (this.board[row][col] != EMPTY) {
                        polyominoOrderMap[row][col] = EMPTY;
                    } else {
                        if(row == 0){       //checks to see if the current position is on the top edge
                            if(col == 0){   //checks to see if the current position is on the far left
                                if(this.board[row][col+1] == EMPTY){        //the point directly to the right
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col+1));
                                }
                                if(this.board[row+1][col] == EMPTY){        //the point directly below
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row+1, col));
                                }
                                
                                //no empty adjacent squares
                                if((this.board[row][col+1] != EMPTY)&&(this.board[row+1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            } else if (col == this.board[row].length-1){    //checks to see if the position is on the far right
                                if(this.board[row][col-1] == EMPTY){        //the point directly to the left
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col-1));
                                }
                                if(this.board[row+1][col] == EMPTY){        //the point directly below
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row+1, col));
                                }
                                
                                //no empty adjacent squares
                                if((this.board[row][col-1] != EMPTY)&&(this.board[row+1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            } else {        //the current position is on the top edge but not on the far right or left 
                                if(this.board[row][col+1] == EMPTY){        //the point directly to the right
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col+1));
                                }
                                if(this.board[row][col-1] == EMPTY){        //the point directly to the left
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col-1));
                                }
                                if(this.board[row+1][col] == EMPTY){        //the point directly below
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row+1, col));
                                }

                                //no empty adjacent squares
                                if((this.board[row][col+1] != EMPTY)&&(this.board[row][col-1] != EMPTY)&&(this.board[row+1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            }
                        } else if (row == this.board.length - 1){           //checks to see if the position is on the bottom edge
                            if(col == 0){   //checks to see if the current position is on the far left
                                if(this.board[row][col+1] == EMPTY){        //the point directly to the right
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col+1));
                                }
                                if(this.board[row-1][col] == EMPTY){        //the point directly above
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row-1, col));
                                }

                                //no empty adjacent squares
                                if((this.board[row][col+1] != EMPTY)&&(this.board[row-1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            } else if (col == this.board[row].length-1){    //checks to see if the position is on the far right
                                if(this.board[row][col-1] == EMPTY){        //the point directly to the left
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col-1));
                                }
                                if(this.board[row-1][col] == EMPTY){        //the point directly above
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row-1, col));
                                }

                                //no empty adjacent squares
                                if((this.board[row][col-1] != EMPTY)&&(this.board[row-1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            } else {        //the current position is on the bottom edge but not on the far right or left
                                if(this.board[row][col+1] == EMPTY){        //the point directly to the right
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col+1));
                                }
                                if(this.board[row][col-1] == EMPTY){        //the point directly to the left
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col-1));
                                }
                                if(this.board[row-1][col] == EMPTY){        //the point directly above
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row-1, col));
                                }

                                //no empty adjacent squares
                                if((this.board[row][col+1] != EMPTY)&&(this.board[row][col-1] != EMPTY)&&(this.board[row-1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            }
                        } else {            //the current position is not on the top or bottom edge
                            if(col == 0){   //checks to see if the current position is on the far left
                                if(this.board[row][col+1] == EMPTY){        //the point directly to the right
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col+1));
                                }
                                if(this.board[row-1][col] == EMPTY){        //the point directly above
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row-1, col));
                                }
                                if(this.board[row+1][col] == EMPTY){        //the point directly below
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row+1, col));
                                }
                                //no empty adjacent squares
                                if((this.board[row][col+1] != EMPTY)&&(this.board[row-1][col] != EMPTY)&&(this.board[row+1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            } else if (col == this.board[row].length-1){    //checks to see if the position is on the far right
                                if(this.board[row][col-1] == EMPTY){        //the point directly to the left
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col-1));
                                }
                                if(this.board[row-1][col] == EMPTY){        //the point directly above
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row-1, col));
                                }
                                if(this.board[row+1][col] == EMPTY){        //the point directly below
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row+1, col));
                                }
                                //no empty adjacent squares
                                if ((this.board[row][col-1] != EMPTY)&&(this.board[row-1][col] != EMPTY)&&(this.board[row+1][col] != EMPTY)){
                                    polyomino.addElement(new Point(row, col));
                                }
                            } else {
                                /**
                                 * the current position is not on the far right or left, nor is it on the top or bottom edge,
                                 * this is the ideal case
                                 */
                                if(this.board[row][col+1] == EMPTY){        //the point directly to the right
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col+1));
                                }
                                if(this.board[row][col-1] == EMPTY){        //the point directly to the left
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row, col-1));
                                }
                                if(this.board[row-1][col] == EMPTY){        //the point directly above
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row-1, col));
                                }
                                if(this.board[row+1][col] == EMPTY){        //the point directly below
                                    polyomino.addToPolyomino(new Point(row, col), new Point(row+1, col));
                                }
                                
                                //no empty adjacent squares
                                if((this.board[row][col+1] != EMPTY)&&(this.board[row][col-1] != EMPTY)&&(this.board[row-1][col] != EMPTY)&&(this.board[row+1][col] != EMPTY)) {
                                    polyomino.addElement(new Point(row, col));
                                }
                            }
                        }
                    }
                }
            }
            this.ready = true;
        }

        public int getPolyominoAt(int row, int col){
            while(this.ready == false){}
            return polyominoOrderMap[row][col];
        }

        /**
         * As of now, this function just prints out the current state of the board.
         * @param b
         * @param order
         */
        public void showBoard(int b[][], int order){
            System.out.println("Greatest Polyomino Order: " + order);

            this.board = b.clone();
            for(int row = 0; row < this.board.length; row++){
                for(int col = 0; col < this.board[row].length; col++){
                    System.out.printf(" %d ", board[row][col]);
                }
                System.out.println();
            }
            System.out.println();
        }

        public Polyomino getPolyomino(){
            return polyomino;
        }

        public int[][] getPolyominoMap(){
            return polyominoOrderMap;
        }

        /**
         * Determines whether or not there is a piece at a specific place on the board.
         * @param row
         * @param col
         * @return
         */
        // private boolean isPieceAt(int row, int col){
        //     int type = this.board[row][col];
        //     return (type == EMPTY);
        // }
    }