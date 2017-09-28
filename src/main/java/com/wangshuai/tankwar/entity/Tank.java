package com.wangshuai.tankwar.entity;

import com.wangshuai.tankwar.client.TankClient;
import com.wangshuai.tankwar.enums.Direction;
import com.wangshuai.tankwar.utils.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tank {
    public static final int XSPEED = 10;
    public static final int YSPEED = 10;

    public static final int MAX_LIFE = 100;

    TankClient tc = null;
    private BloodBar bb = new BloodBar();

    private boolean good;

    private boolean live = true;

    private int life = MAX_LIFE;

    private int x, y;
    private int oldX, oldY;

    private static Random r = new Random();

    private boolean bL = false, bR = false, bU = false, bD = false;

    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    private int step = 0;

    private static BufferedImage[] tankImgs = null;
    private static Map<String, BufferedImage> imgs = new HashMap<String, BufferedImage>();

    static {
        try {
            tankImgs = new BufferedImage[]{
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankL.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankLU.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankU.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankRU.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankR.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankRD.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankD.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/tankLD.gif"))
            };

            imgs.put("L", tankImgs[0]);
            imgs.put("LU", tankImgs[1]);
            imgs.put("U", tankImgs[2]);
            imgs.put("RU", tankImgs[3]);
            imgs.put("R", tankImgs[4]);
            imgs.put("RD", tankImgs[5]);
            imgs.put("D", tankImgs[6]);
            imgs.put("LD", tankImgs[7]);

        } catch (IOException e) {
            System.out.println("坦克图片加载错误！");
        }
    }

    public static final int WIDTH = tankImgs[0].getWidth();
    public static final int HEIGHT = tankImgs[0].getHeight();

    public Tank(int x, int y, int oldX, int oldY, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, TankClient tc) {
        this(x, y, x, y, good);
        this.tc = tc;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, good, tc);
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void draw(Graphics g) {
        if (!live) {
            if (!good) {
                tc.removeTank(this);
            }
            return;
        }

        if (good) {
            bb.draw(g);
        }

        switch (ptDir) {
            case L:
                g.drawImage(imgs.get("L"), x, y, null);
                break;
            case LU:
                g.drawImage(imgs.get("LU"), x, y, null);
                break;
            case U:
                g.drawImage(imgs.get("U"), x, y, null);
                break;
            case RU:
                g.drawImage(imgs.get("RU"), x, y, null);
                break;
            case R:
                g.drawImage(imgs.get("R"), x, y, null);
                break;
            case RD:
                g.drawImage(imgs.get("RD"), x, y, null);
                break;
            case D:
                g.drawImage(imgs.get("D"), x, y, null);
                break;
            case LD:
                g.drawImage(imgs.get("LD"), x, y, null);
                break;
            default:
                System.out.println("未明确的方向");
        }

        move();
    }

    private void move() {

        this.oldX = x;
        this.oldY = y;

        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= XSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
            default:
                System.out.println("未明确的方向");
        }

        if (this.dir != Direction.STOP) {
            this.ptDir = dir;
        }

        if (x < 0) x = 0;
        if (y < 30) y = 30;
        if (x + WIDTH > TankClient.WIDTH) x = TankClient.WIDTH - WIDTH;
        if (y + HEIGHT > TankClient.HEIGHT) y = TankClient.HEIGHT - HEIGHT;

        if (!good) {
            if (step == 0) {
                step = r.nextInt(12) + 3;

                Direction[] dirs = Direction.values();
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }
            step--;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
            case KeyEvent.VK_F2:
                if (!tc.getMyTank().isLive()) {
                    tc.init();
                }
                break;
            default:
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_CONTROL:
                if (live) {
                    tc.addMissile(fire());
                }
                break;
            case KeyEvent.VK_A:
                if (live) {
                    superFire();
                }
                break;
            default:
        }
        locateDirection();
    }

    /**
     * 根据按键计算前进方向
     */
    private void locateDirection() {
        if (bL & !bU & !bR & !bD) dir = Direction.L;
        else if (bL & bU & !bR & !bD) dir = Direction.LU;
        else if (!bL & bU & !bR & !bD) dir = Direction.U;
        else if (!bL & bU & bR & !bD) dir = Direction.RU;
        else if (!bL & !bU & bR & !bD) dir = Direction.R;
        else if (!bL & !bU & bR & bD) dir = Direction.RD;
        else if (!bL & !bU & !bR & bD) dir = Direction.D;
        else if (bL & !bU & !bR & bD) dir = Direction.LD;
        else if (!bL & !bU & !bR & !bD) dir = Direction.STOP;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public Missile fire() {
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, good, ptDir, tc);
        return m;
    }

    public Missile fire(Direction dir) {
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, good, dir, tc);
        return m;
    }

    public boolean hitWall(Wall wall) {
        if (this.live && this.getRect().intersects(wall.getRect())) {
            x = oldX;
            y = oldY;
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if (t == this) continue;
            if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                x = oldX;
                y = oldY;
                t.x = t.oldX;
                t.y = t.oldY;
                return true;
            }
        }
        return false;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        for (Direction dir1 : dirs) {
            if (dir1 == Direction.STOP) continue;
            tc.addMissile(fire(dir1));
        }
    }

    public boolean eat(Blood b) {
        if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
            this.life = MAX_LIFE;
            b.setLive(false);
            return true;
        }
        return false;
    }

    public class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(x, y - 12, WIDTH, 10);
            int w = WIDTH * life / 100;
            g.fillRect(x, y - 12, w, 10);
            g.setColor(c);
        }
    }

}
