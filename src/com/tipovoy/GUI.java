package com.tipovoy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GUI extends JFrame {
    private JPanel canva = new GraphicPanel(); //область отображения

    public static int mouseX; //координаты мыши
    public static int mouseY;
    public static boolean isPressed=false;
    public static int selectedBSpline =-1;  // есть ли точка какой-то кривой по координатам где мышь нажали (-1 если нет)

    public static BezierCurve bezierCurve=new BezierCurve();
    public static BSpline BSpline =new BSpline();

    public GUI () {
        super("Б-сплайн и кривая Безье");

        this.setBounds(400, 100, 1024, 768); // координаты и размер окна программы
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); //менять размер окна нельзя

        MyMouseListener mouse = new MyMouseListener();    //обработчик событий мыши
        canva.addMouseListener(mouse);                    //обрабатывает нажатия
        canva.addMouseMotionListener(mouse);              //и движения

        this.setLayout(new BorderLayout());

        this.add(canva, BorderLayout.NORTH);    //верхняя часть - область отображения
        canva.repaint();
    }

    class GraphicPanel extends JPanel {
        public GraphicPanel() {
            super.setPreferredSize(new Dimension(1024, 700));
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g; //g2 - это g-контекст отображения, приведенный к типу для работы с 2д
            g2.setColor(Color.white);       //белый цвет
            g2.fillRect(0, 0, getSize().width - 1, getSize().height - 1); //область белого цвета
            g2.setColor(Color.black);
                                    //вызов метода, который рисует соответствующую кривую
            bezierCurve.draw(g2);    //точки и полигоны
            BSpline.draw(g2);
        }
    }


    public class MyMouseListener implements MouseListener, MouseMotionListener {
        public void mouseClicked(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }

        //при движении мыши идет смена координат выбранной точки и перерисовка,
        //если selectedBSpline(selectedBezier) не =-1
        @Override
        public void mouseDragged(MouseEvent e) {
            if (isPressed == true){
                mouseX = e.getX();
                mouseY = e.getY();

                for (int i = 0; i < BSpline.points.size(); i++) {
                    Point temp = BSpline.points.get(i);
                    if ((Math.abs(temp.x - mouseX) < 5) && (Math.abs((700 - temp.y) - mouseY) < 5)) {
                        selectedBSpline = i;
                        break;
                    }
                }

                if (selectedBSpline >=0){
                    BSpline.points.set(selectedBSpline,new Point(mouseX,700-mouseY));
                }
                canva.repaint();

            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        //при нажатии берутся координаты мыши(где нажато), сравниваются по модулю с точкой двух кривых
        //с погрешностью в 5ед (для удобного нажатия)
        //если найдется совпадение с точкой, то в переменную selectedBSpline(selectedBezier)
        //записывается индекс точки
        public void mousePressed(MouseEvent e) {
            isPressed=true;
            selectedBSpline =-1;

            mouseX=e.getX();
            mouseY=e.getY();

            for (int i = 0; i< BSpline.points.size(); i++){
                Point temp = BSpline.points.get(i);
                if ((Math.abs(temp.x-mouseX)<5)&&(Math.abs((700-temp.y)-mouseY)<5)){
                    selectedBSpline =i;
                    break;
                }
            }
        }

        //сбрасываются индексы точек и флаг нажатия
        public void mouseReleased(MouseEvent e) {
            mouseX=e.getX();
            mouseY=e.getY();
            if (selectedBSpline >=0){
                BSpline.points.set(selectedBSpline,new Point(mouseX,700-mouseY));
            }
            canva.repaint();
            isPressed=false;
            selectedBSpline =-1;
        }
    }
}
