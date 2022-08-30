import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Color.*;

public class GamePanel extends JPanel {

    static final int WIDTH = 10;
    static final int HEIGHT = 10;
    static final int SQUARE_SIZE = 50;
    static final int INDENT = 100;
    int playing = -1;
    int time=0;
    boolean changed;
    MyField field;
    JButton restart = new JButton("Restart");
    Timer timer = new Timer(1000, e->repaint());

    GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH*SQUARE_SIZE+2*INDENT,HEIGHT*SQUARE_SIZE+2*INDENT));
        this.setBackground(white);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());
        startGame();
    }

    public void startGame(){
        field = new MyField(HEIGHT,WIDTH);
        playing=0;
        time=-1;
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) throws IOException {

        for(int i=0; i<=HEIGHT;i++){
            g.drawLine(INDENT,(i*SQUARE_SIZE)+INDENT,(WIDTH*SQUARE_SIZE)+INDENT,(i*SQUARE_SIZE)+INDENT);
        }
        for(int i=0; i<=WIDTH;i++){
            g.drawLine((i*SQUARE_SIZE)+INDENT,INDENT,(i*SQUARE_SIZE)+INDENT,(HEIGHT*SQUARE_SIZE)+INDENT);
        }

        for(int i=0; i<HEIGHT; i++){
            for(int j=0; j<WIDTH; j++){
                switch (field.getSecondByIndex(i,j)){
                    case -2:
                        Image imgboomb = ImageIO.read(new File("src/image/bomb.png"));
                        g.drawImage(imgboomb,j*SQUARE_SIZE+INDENT+4,i*SQUARE_SIZE+INDENT+4,SQUARE_SIZE-6,SQUARE_SIZE-6,this);
                        break;
                    case -1:
                        if(field.getFirstByIndex(i,j) == -1){
                            Image imgboombs = ImageIO.read(new File("src/image/bomb2.png"));
                            g.drawImage(imgboombs,j*SQUARE_SIZE+INDENT+4,i*SQUARE_SIZE+INDENT+4,SQUARE_SIZE-6,SQUARE_SIZE-6,this);
                            break;
                        }
                        g.setColor(black);
                        g.setFont(new Font("Ink Free", Font.BOLD,25));
                        g.drawString(String.valueOf(field.getFirstByIndex(i,j)),(j)*SQUARE_SIZE+INDENT+SQUARE_SIZE/3,(i+1)*SQUARE_SIZE+INDENT-SQUARE_SIZE/3);
                        break;
                    case 1:
                        g.setColor(black);
                        g.setFont(new Font("Ink Free", Font.BOLD,25));
                        g.drawString("F",(j)*SQUARE_SIZE+INDENT+SQUARE_SIZE/3,(i+1)*SQUARE_SIZE+INDENT-SQUARE_SIZE/3);
                        break;
                }
            }
        }

        if(playing == -1){
            g.setColor(black);
            g.setFont(new Font("Ink Free", Font.BOLD,35));
            g.drawString("YOU LOSE",WIDTH*SQUARE_SIZE/5, HEIGHT*SQUARE_SIZE+INDENT*5/3);
        }

        if(playing == 1){
            g.setColor(black);
            g.setFont(new Font("Ink Free", Font.BOLD,35));
            g.drawString("YOU WIN",WIDTH*SQUARE_SIZE/5, HEIGHT*SQUARE_SIZE+INDENT*5/3);
        }

        drawRestartButton(g);

        drawTimer(g);

    }

    public void drawTimer(Graphics g){

        if(!changed && playing==0) {
            time++;
        }else {
            changed = false;
        }

        if(time%60 < 10 && time/60 < 10){
            g.setColor(black);
            g.setFont(new Font("Ink Free", Font.BOLD,35));
            g.drawString("0"+time/60 + ":0" + time%60,WIDTH*SQUARE_SIZE/2+46, INDENT/2+10);
        } else {
            if(time%60 < 10 && time/60 > 10) {
                g.setColor(black);
                g.setFont(new Font("Ink Free", Font.BOLD, 35));
                g.drawString("0" + time / 60 + ":" + time % 60, WIDTH*SQUARE_SIZE/2+46, INDENT/2+10);
            } else {
                g.setColor(black);
                g.setFont(new Font("Ink Free", Font.BOLD, 35));
                g.drawString(time / 60 + ":" + time % 60, WIDTH*SQUARE_SIZE/2+46, INDENT/2+10);
            }
        }
    }

    public void drawRestartButton(Graphics g){
        restart.setBounds(WIDTH*SQUARE_SIZE*4/5, HEIGHT*SQUARE_SIZE+INDENT*4/3, 2*INDENT,INDENT/2);
        restart.addActionListener( e-> this.restart());
        this.add(restart);
    }

    public void restart(){
        startGame();
        repaint();
        timer.restart();
    }

    public class MyMouseAdapter extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent me){
            int y=me.getX()/SQUARE_SIZE - INDENT/SQUARE_SIZE;
            int x=me.getY()/SQUARE_SIZE - INDENT/SQUARE_SIZE;
            System.out.println(x + " : " + y + me.getButton());
            System.out.println(me.getButton());
            if(playing==0) {
                if (me.getButton() == 1) {
                    playing = field.openSquare(x, y);
                    if(playing != 0){
                        timer.stop();
                    }
                }
                if (me.getButton() == 3) {
                    field.setFlag(x, y);
                }
                changed=true;
                repaint();
            }
        }
    }
}
