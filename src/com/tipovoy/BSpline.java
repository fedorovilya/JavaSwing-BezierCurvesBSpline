package com.tipovoy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BSpline {

    public static List<Point> points = new ArrayList<>();  //cписок точек (можно добавить кнопкой)
    public int q=4;                 //порядок кривой+1
    public float[] vectParam;       //вектор параметризации

    public Point start;
    public Point end;
    //обращение к концу и началу

    public int accuracy = 1000;
    //точность отрисовки

    public BSpline() {
        points.add(BezierCurve.points.get(0));
        points.add(new Point(200,325));
        points.add(new Point(300,450));
        points.add(new Point(375,325));
        points.add(new Point(400,450));
        points.add(new Point(500,450));
        points.add(new Point(550,325));
        points.add(new Point(600,450));
        points.add(new Point(700,450));
        points.add(new Point(750,325));
        points.add(BezierCurve.points.get(BezierCurve.points.size()-1));
        vect();
    }

    public void vect() {
        //инициализация вектора параметризации (открытого равномерного)
        vectParam = new float[points.size() + q]; // (accuracy+1) опорных точек, (p+1) порядок. (accuracy+p+2)

        for (int i = 0; i < points.size() - q; i++) {
            vectParam[q + i] = (1f + i) / (1f + points.size() - q); //существенные интервалы
        }

        for (int i = 0; i < q; i++) {
            vectParam[i] = 0.0000001f * i;
            vectParam[vectParam.length - 1 - i] = 1 - 0.0000001f * i;
            // 0 от 0 до q+1, 1 от m до m-(q+1)
        }

        for (int i = 0; i < vectParam.length; i++) {
            System.out.print(vectParam[i] + " ");
        }
        System.out.println();
    }

    //функция добавления точки и пересчёт вектора параметризации
    public void addPoint(Point point){
        points.add(BSpline.points.size()-1, point);
        vect();
    }

    //рекурсивная функция расчета базисных функций
    //q=порядок+1, ti - номер точки, t - параметр
    public float N(int q, int ti, float t){
        if (q==1)
        {
            if ((t> vectParam[ti])&&(t<= vectParam[ti+1])) return 1;
            else return 0;
        }
        else
            {
                return (((t- vectParam[ti])/(vectParam[ti+q-1]- vectParam[ti]))*N(q-1,ti,t)+
                    (((vectParam[ti+q]-t)/(vectParam[ti+q]- vectParam[ti+1]))*N(q-1,ti+1,t)));
            }

        // Рекуррентная формула Кокса-Де Бура
        //N(i,Ќ)=(t-t_i)/(t(i+р)-t(i))*N(i,(p-1)(t))  +  (t(i+p+1)-t)/(t(i+p+1)-t(i+1))*N(i+1,p-1)(t).
    }

    //расчет точки кривой для параметра t
    public Point pointCalc(float t){
        Point p = new Point(0f,0f);
        for (int i=0; i<points.size(); i++){
            p=p.add(points.get(i).multiply(N(q,i,t)));
        }
        return p;
    }

    //метод отрисовки
    public void draw(Graphics2D graphics){

        points.set(0,BezierCurve.points.get(0));
        points.set(points.size()-1,BezierCurve.points.get(BezierCurve.points.size()-1));

        start = points.get(0);
        end = points.get(points.size()-1);


        //отрисовка полигона и всех точек кривой
        for (int i = 0; i < points.size()-1; i++)
        {
            graphics.setStroke(new BasicStroke(1));
            graphics.drawLine
                    ((int) (points.get(i).x), (int) (700 - points.get(i).y),
                    (int) (points.get(i + 1).x), (int) (700 - points.get(i + 1).y));
            graphics.setStroke(new BasicStroke(3));
            points.get(i).draw(graphics, 5, Color.green);
        }

        for (int i = 0; i < BezierCurve.points.size()-1; i++) {
            BezierCurve.points.get(i).draw(graphics, 5, Color.blue);
        }

        start.draw(graphics,5,Color.green);
        end.draw(graphics,5,Color.green);

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(3)); //толщина линии

        //отрисовка кривой
        Point current; //текущая точка
        Point temp = pointCalc(1f/ accuracy); //предыдущая точка, в цикле меняется на текущую и тд
        for (int i = 1; i< accuracy; i++)
            {
                current = pointCalc(i/((float)(accuracy)));
                graphics.drawLine((int)(current.x),(int)(700-current.y), (int)(temp.x),(int)(700-temp.y));
                temp = current;
            }

        graphics.setColor(Color.BLACK);
        }
    }
