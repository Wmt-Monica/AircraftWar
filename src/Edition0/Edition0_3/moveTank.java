package Edition0.Edition0_3;

import javax.swing.*;
import java.awt.*;

/**
 * 版本0.3
 * 功能：
 *      1.让坦克运动起来
 *      2.步骤：
 *          1）将位置改变为变量
 *          2）启动线程不断重画
 *              使用线程重画相对于按键重画的好处：
 *                  线程重画更均匀，更能控制重画的速度
 *                  按键重画不能解决子弹自动飞行的问题
 *          3）每次重画改变Tank的位置
 * 掌握：
 *      内部类的使用
 *          内部类的好处：可以方便的访问包装类的方法，不方便公开的
 *          什么时候使用内部类：只为包装类服务的类应当定义为内部类
 *
 */
public class moveTank extends JFrame {  //继承Frame类

    int x = 50, y = 50;  //让坦克的位置x,y设置为变量

    //重写paint()方法，将实心圆坦克画出来
    @Override
    public void paint(Graphics g) {

        Color foregroundColor = g.getColor();  //获取窗口前景色
        g.setColor(Color.PINK);  //设置窗口背景色为黑色

        //使用fillOval()方法画出实心圆坦克
        g.fillOval(x,y,30,30);

        //不要改变前景色，最后将之前获取到的颜色设置回去
        g.setColor(foregroundColor);

        x += 20;

    }

    //创建一个游戏窗口对象方法
    public void windowFrame(){
        this.setBounds(400,200,800,600);  //使用setBounds()方法直接设置窗口坐标位置和窗口大小
        this.setResizable(false);  //设置游戏窗口不可随意改变窗口大小
        this.setTitle("AircraftWar");  //设置窗口标题
        this.setVisible(true);  //设置窗口可见
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  //设置窗口叉掉就停止程序
        this.setBackground(Color.BLACK);
        new Thread(new rePaintThread()).start();
    }

    public static void main(String[] args) {
        moveTank window = new moveTank();
        window.windowFrame();
    }

    //创建一个线程类，使用线程不断重画
    class rePaintThread implements Runnable{
        @Override
        public void run() {
            while (true){
                repaint();  //使用repaint()方法来实现paint()方法的重画

                //为了更好的看到效果，让线程每重画一次就休息500ms
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
