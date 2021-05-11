import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private final int borderX = 300;
    private final int borderY = 300;
    private final int dotSize = 10;
    private final int field = 900;
    private final int randPosition = 29;
    private final int delay = 140;

    private final int x[] = new int[field];
    private final int y[] = new int[field];

    private int dots;
    private int score;
    private int apple_x;
    private int apple_y;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inField = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(borderX, borderY));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }

    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateApple();

        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inField) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String gameOverMsg = "Game Over!";
        String scoreMsg = String.format("Your score is %d", score);
        Font small = new Font("Halvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.red);
        g.setFont(small);
        g.drawString(gameOverMsg, (borderX - metr.stringWidth(gameOverMsg)) / 2, borderY / 2);
        g.drawString(scoreMsg, (borderX - metr.stringWidth(scoreMsg)) / 2, borderY / 2 + 20);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (left) {
            x[0] -= dotSize;
        }
        if (right) {
            x[0] += dotSize;
        }
        if (up) {
            y[0] -= dotSize;
        }
        if (down) {
            y[0] += dotSize;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inField = false;
            }
        }
        if (y[0] >= borderY) {
            inField = false;
        }
        if (y[0] < 0) {
            inField = false;
        }
        if (x[0] >= borderX) {
            inField = false;
        }
        if (x[0] < 0) {
            inField = false;
        }
        if (!inField) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * randPosition);
        apple_x = ((r * dotSize));

        r = (int) (Math.random() * randPosition);
        apple_y = ((r * dotSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inField) {

            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                left = false;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                left = false;
                right = false;
                down = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                left = false;
                right = false;
                up = false;
            }
        }
    }
}
