package com.wangshuai.tankwar.entity;

import com.wangshuai.tankwar.client.TankClient;
import com.wangshuai.tankwar.enums.Direction;
import com.wangshuai.tankwar.utils.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
    private static final int XSPEED = 15;
    private static final int YSPEED = 15;

    private int x, y;
    private Direction dir;

    private boolean good;
    private boolean live = true;
    private TankClient tc;

    private static BufferedImage[] missileImgs = null;
    private static Map<String, BufferedImage> imgs = new HashMap<String, BufferedImage>();

    static {
        try {
            missileImgs = new BufferedImage[]{
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileL.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileLU.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileU.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileRU.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileR.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileRD.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileD.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/missileLD.gif"))
            };

            imgs.put("L", missileImgs[0]);
            imgs.put("LU", missileImgs[1]);
            imgs.put("U", missileImgs[2]);
            imgs.put("RU", missileImgs[3]);
            imgs.put("R", missileImgs[4]);
            imgs.put("RD", missileImgs[5]);
            imgs.put("D", missileImgs[6]);
            imgs.put("LD", missileImgs[7]);

        } catch (IOException e) {
            System.out.println("子弹图片加载错误！");
        }
    }

    static final int WIDTH = missileImgs[0].getWidth();
    static final int HEIGHT = missileImgs[0].getHeight();

    public Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, dir);
        this.tc = tc;
        this.good = good;
    }

    public boolean isLive() {
        return live;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.removeMissile(this);
            return;
        }

        switch (dir) {
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
//		Color c = g.getColor();
//		if(good) {
//			g.setColor(Color.BLUE);
//		}
//		else g.setColor(Color.BLACK);
//		
//		g.fillOval(x, y, WIDTH, HEIGHT);
//		g.setColor(c);
//		
        move();
    }

    private void move() {
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
            default:
                System.out.println("未明确的方向");
        }

        if (x < 0 || y < 0 || x > TankClient.WIDTH || y > TankClient.HEIGHT) {
            live = false;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitTank(Tank t) {

        if (this.live && getRect().intersects(t.getRect()) && t.isLive() && good != t.isGood()) {
            if (t.isGood()) {
                t.setLife(t.getLife() - 20);
                if (t.getLife() <= 0) t.setLive(false);
            } else {
                t.setLive(false);
            }
            live = false;
            Explode e = new Explode(x, y, tc);
            tc.addExlode(e);
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tanks) {
        for (Tank tank : tanks) {
            if (hitTank(tank)) {
                return true;
            }
        }
        return false;
    }

    public boolean hitWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            return true;
        }
        return false;
    }
}
