package Edition0.Edition0_2;

import javax.swing.*;
import java.awt.*;

/**
 * 版本0.2
 * 功能：
 *      画出代表坦克的实心圆
 * 掌握：
 *      如何重写paint方法
 * 注意：
 *      不要改变前景色
 * 回顾：
 *      1.paint(Graphics g)方法，窗口重画时自动调用
 *      2.x轴,y轴的方向
 *      ——————————————————>x
 *      |
 *      |
 *      |y
 */
public class createTank extends JFrame {  //继承Frame类

    //重写paint()方法，将实心圆坦克画出来
    @Override
    public void paint(Graphics g) {
        Color foregroundColor = g.getColor();  //获取窗口前景色
        g.setColor(Color.PINK);  //设置窗口背景色为黑色

        //使用fillOval()方法画出实心圆坦克
        g.fillOval(50,50,30,30);

        //不要改变前景色，最后将之前获取到的颜色设置回去
        g.setColor(foregroundColor);

    }

    public void windowFrame(){  //创建一个游戏窗口对象方法
        this.setBounds(400,200,800,600);  //使用setBounds()方法直接设置窗口坐标位置和窗口大小
        this.setResizable(false);  //设置游戏窗口不可随意改变窗口大小
        this.setTitle("AircraftWar");  //设置窗口标题
        this.setVisible(true);  //设置窗口可见
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  //设置窗口叉掉就停止程序
        this.setBackground(Color.BLACK);

    }

    public static void main(String[] args) {
        createTank window = new createTank();
        window.windowFrame();
    }
}
