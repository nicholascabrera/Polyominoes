import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Checkers");

        PolyominoBoard polyomino = new PolyominoBoard();
        
        Checkerboard checkerboard = new Checkerboard(true, polyomino);
        checkerboard.setLocation(0, 0);
        checkerboard.setSize(new Dimension(200, 400));

        Checkerboard polyominoPanel = new Checkerboard(false, polyomino);
        polyominoPanel.setLocation(200, 0);
        polyominoPanel.setSize(new Dimension(400, 400));

        polyomino.setPolyominoBoard(polyominoPanel);

        window.setPreferredSize(new Dimension(600, 400));
        window.setLayout(null); // I will do the layout myself.

        window.add(checkerboard);
        window.add(polyominoPanel);

        window.pack();

        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screensize.width - window.getWidth()) / 2,
                (screensize.height - window.getHeight()) / 2);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }
}
