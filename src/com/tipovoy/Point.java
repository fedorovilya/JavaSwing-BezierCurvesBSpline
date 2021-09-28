package com.tipovoy;

import java.awt.*;

public class Point {
    public float x,y;
    // координаты точки

    public Point(float x, float y){
        this.x=x;
        this.y=y;
    }
    //конструктор

    public Point add(Point b){
        return new Point(b.x+this.x,b.y+this.y);
    }
    //сложить точку(вектор) с другим. Используется также для добавления новой в список точек
    public Point multiply(float f){
        return new Point(f*this.x,f*this.y);
    }
    //умножить точку на f

    public void draw(Graphics2D graphics, int s, Color color){
        graphics.setColor(color);
        graphics.drawOval((int)this.x-s,700-(int)(this.y+s),2*s,2*s);
        //метка(окружность)
        graphics.setColor(Color.black);

        graphics.setFont(new Font("Calibri",Font.BOLD, 18));
        graphics.drawString(("("+(int)x+";"+(int)y+")"),(int)this.x+s+2,700-(int)(this.y+2*s));
        //надпись
    }
    //отрисовка точки
}
