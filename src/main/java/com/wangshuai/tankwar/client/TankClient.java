package com.wangshuai.tankwar.client;

import com.wangshuai.tankwar.entity.*;
import com.wangshuai.tankwar.enums.Direction;
import com.wangshuai.tankwar.utils.PropertyMgr;
import com.wangshuai.tankwar.utils.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankClient extends Frame {

    private static final long serialVersionUID = 5966106568450438381L;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Random rand = null;

    private int timeCount;
    private Image offScreenImage = null;

    private static BufferedImage background;

    static {
        try {
            background = ImageIO.read(ResourceHelper.getResourceInputStream("images/background.jpg"));
        } catch (IOException e) {
            System.out.println("背景图片加载错误！");
        }
    }

    private Blood b = new Blood();

    private Tank myTank = new Tank(50, 50, true, this);
    private Wall w1 = new Wall(200, 200, 250, 40, this);
    private Wall w2 = new Wall(400, 200, 20, 400, this);

    private List<Explode> explodes = new ArrayList<>();
    private List<Missile> missiles = new ArrayList<>();
    private List<Tank> tanks = new ArrayList<>();

    public Tank getMyTank() {
        return myTank;
    }

    public boolean addExlode(Explode explode) {
        return explodes.add(explode);
    }

    public boolean removeExlode(Explode explode) {
        return explodes.remove(explode);
    }

    public boolean addMissile(Missile missile) {
        return missiles.add(missile);
    }

    public boolean removeMissile(Missile missile) {
        return missiles.remove(missile);
    }

    public boolean removeTank(Tank tank) {
        return tanks.remove(tank);
    }


    public void launchFrame() {
        rand = new Random();

        initEnemy();

        this.setTitle("TankWar");
//		this.setLocation(400,200);
        this.setLocation(200, 100);
        this.setSize(WIDTH, HEIGHT);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.LIGHT_GRAY);

        this.addKeyListener(new KeyMonitor());

        setVisible(true);

        new Thread(new PaintThread()).start();
    }

    @Override
    public void paint(Graphics g) {
        if (tanks.size() == 0) {
            initEnemy();
        }

        myTank.draw(g);

        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            t.hitWall(w1);
            t.hitWall(w2);
            t.collidesWithTanks(tanks);
            if (timeCount % (rand.nextInt(1) + 5) == 0) {
                missiles.add(t.fire());
            }
            t.draw(g);
        }

        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitWall(w1);
            m.hitWall(w2);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.draw(g);
        }

        for (int i=0; i<explodes.size(); i++) {
            explodes.get(i).draw(g);
        }

        w1.draw(g);
        w2.draw(g);
        myTank.eat(b);
        b.draw(g);

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, g.getFont().getSize()));
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("Missiles count:" + missiles.size(), 10, 40);
        g.drawString("Explodes count:" + explodes.size(), 10, 60);
        g.drawString("Tanks count:" + tanks.size(), 10, 80);
        g.drawString("Mytank life:" + myTank.getLife(), 10, 100);
        g.setColor(c);

    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(WIDTH, HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        gOffScreen.drawImage(background, 0, 0, null);

        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void initEnemy() {

        int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));

        tanks = new ArrayList<Tank>();
        for (int i = 0; i < initTankCount; i++) {
            Tank t = new Tank(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), false, Direction.STOP, this);
            if (t.hitWall(w1) || t.hitWall(w2) || t.collidesWithTanks(tanks)) {
                i--;
                continue;
            }

            tanks.add(t);
        }
    }

    public void init() {
        initEnemy();
        myTank = new Tank(50, 50, true, this);
        b = new Blood();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();

    }

    private class PaintThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                repaint();
                timeCount++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
