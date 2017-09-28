package com.wangshuai.tankwar.entity;

import com.wangshuai.tankwar.client.TankClient;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	private int x, y, w, h ;
	TankClient tc = null;
	
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
}
