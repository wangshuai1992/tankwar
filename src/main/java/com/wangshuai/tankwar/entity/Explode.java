package com.wangshuai.tankwar.entity;

import com.wangshuai.tankwar.client.TankClient;
import com.wangshuai.tankwar.utils.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Explode {
    /**
     * 位置
     */
    private int x, y;

    private TankClient tc;

    private boolean live = true;

    private int step = 0;

    private static BufferedImage[] imgs = null;

    static {
        try {
            imgs = new BufferedImage[]{
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/0.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/1.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/2.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/3.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/4.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/5.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/6.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/7.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/8.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/9.gif")),
                    ImageIO.read(ResourceHelper.getResourceInputStream("images/10.gif")),
            };
        } catch (IOException e) {
            System.out.println("爆炸图片加载错误！");
        }
    }

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.removeExlode(this);
            return;
        }

        if (step == imgs.length) {
            live = false;
            step = 0;
            return;
        }

        g.drawImage(imgs[step], x, y, null);
        step++;
    }

}
