package com.tipovoy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    public static List<Point> points = new ArrayList<>();
    public static List<Point> points1 = new ArrayList<>();
    public static List<Point> points2 = new ArrayList<>();


    private Point start, start_temp;
    private Point mid, mid_temp;
    private Point end, end_temp;
    //temp - точки, на которые опирается дуга (не лежат на ребрах треугольника)

    private float R, r, A;
    private Point S, S1, S2;

    private float w0 = 1f, w2 = 1f ,w1 = 0.5f;
    //веса точек

    private int accuracy = 1000;
    // точность отрисовки

    public BezierCurve() {
        mid = new Point(500, 500);
        r = 150f; R = 2*r;
        A = R*(float)Math.sqrt(3);  //сторона вписанного равностороннего треугольника
        S = new Point(mid.x, mid.y-r);
        start = new Point(mid.x- A/2f,mid.y-r-r/2);
        end = new Point(mid.x+ A/2f,mid.y-r-r/2);
        S = new Point(mid.x, mid.y-R);
        start = new Point(mid.x- A/2f,mid.y-R-r);
        end = new Point(mid.x+ A/2f,mid.y-R-r);

        start_temp = new Point(start.x+A/4f, start.y+r/2f);
        mid_temp = new Point(mid.x, mid.y-r);
        end_temp = new Point(end.x-A/4f, end.y+r/2f);

        points.add(start);
        points.add(mid);
        points.add(end);

        S1 = new Point(S.x-(r*(float)Math.sqrt(3)), S.y+r);
        S2 = new Point(S.x+(r*(float)Math.sqrt(3)), S.y+r);

        points1.add(start_temp);
        points1.add(S1);
        points1.add(mid_temp);

        points2.add(end_temp);
        points2.add(S2);
        points2.add(mid_temp);
    }

    public Point pointCalc(float t, List <Point> points){
        Point p = new Point(0,0);

        float divider = (1-t)*(1-t)*w0 + 2*t*(1-t)*w1 + t*t*w2;
        p= p.add(points.get(0).multiply((1-t)*(1-t)*w0)).
                add(points.get(1).multiply(2*t*(1-t)*w1)).
                add(points.get(2).multiply(t*t*w2));
        p = p.multiply(1/divider);
        return p;
    }

    //отрисовка кривой
    public void draw(Graphics2D graphics){
        graphics.setStroke(new BasicStroke(1));

        //отрисовка полигона составной кривой
        for (int i=0;i<points.size()-1;i++) {
            graphics.drawLine((int) (points.get(i).x), (int) (700 - points.get(i).y),
                    (int) (points.get(i + 1).x), (int) (700 - points.get(i + 1).y));
        }

        S.draw(graphics,5,Color.magenta);
        S1.draw(graphics,5,Color.magenta);
        S2.draw(graphics,5,Color.magenta);

        start_temp.draw(graphics,5,Color.BLACK);
        mid_temp.draw(graphics,5,Color.BLACK);
        end_temp.draw(graphics,5,Color.BLACK);


        //отрисовка кривой
        graphics.setColor(Color.blue);
        graphics.setStroke(new BasicStroke(3));

        Point current; //текущая точка
        Point temp = pointCalc(1f/ accuracy, points1); //предыдущая точка, в цикле меняется на текущую и тд
        for (int i = 1; i< accuracy; i++)
        {
            current = pointCalc(i/((float)(accuracy)), points1);
            graphics.drawLine((int)(current.x),(int)(700-current.y), (int)(temp.x),(int)(700-temp.y));
            temp = current;
        }

        temp = pointCalc(1f/ accuracy, points2); //предыдущая точка, в цикле меняется на текущую и тд
        for (int i = 1; i< accuracy; i++)
        {
            current = pointCalc(i/((float)(accuracy)), points2);
            graphics.drawLine((int)(current.x),(int)(700-current.y), (int)(temp.x),(int)(700-temp.y));
            temp = current;
        }


        graphics.setColor(Color.black);
    }

}
