package Edition0.Edition0_7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 版本0.7
 * 使用面向对象的思维，将坦克兑现包装成一个类
 * 1.自己有实现画出自身的方法
 * 2.自己有对用户方向操控的监听方法
 */
public class Tank extends JFrame {
    private static int TANK_SIZE = 30;  //实心圆坦克的直径长
    private int x,y;  //让坦克的位置x,y设置为变量

    public Tank(int x, int y){
        this.x = x;
        this.y = y;
    }

    //坦克类画出自己的方法
    public void draw(Graphics g){
        g.fillOval(x,y,TANK_SIZE,TANK_SIZE);
    }

    /**
     * 创建一个键盘监听类
     * 注意：这里使用继承KeyAdapter类而不是实现KeyListener的优点
     * 继承KeyAdapter已经帮你实现了空的keyTyped(),keyPressed()和keyReleased()方法，我们需要使用那个方法自主重写即可
     * 但是实现keyListener接口，我们即使不需要实现某些方法，仍要将空方法写出来
     */
    //坦克类自己运行时的监听方法
    class KeyListener extends KeyAdapter {

        //键盘信息三种方法的区别
        //keydown：当用户按下键盘上的任意键时触发，如果按住不放的话，会重复触发此事件；
        //keypress：当用户按下键盘上的字符键时触发，如果按住不让的话，会重复触发此事件；
        //keyup：当用户释放键盘上的字符键时触发。

        //重写keyPressed()方法
        @Override
        public void keyPressed(KeyEvent e) {

            //getKeyCode():键盘上每一个按钮都有对应码(Code),可用来查知用户按了什么键，返回当前按钮的数值
            int TankDirection = e.getKeyCode();  //获取坦克前进的方向
            switch (TankDirection){
                case KeyEvent.VK_LEFT : //按下向左键盘
                    x -= 5;
                    break;
                case KeyEvent.VK_RIGHT :
                    x += 5;
                    break;
                case KeyEvent.VK_UP :
                    y -= 5;
                    break;
                case KeyEvent.VK_DOWN :
                    y += 5;
                    break;
            }
        }
    }
}
