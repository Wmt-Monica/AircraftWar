package Edition1.Edition1_0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * 版本1.0
 * 创建一个子弹类
 * 功能：
 *      根据坦克的移动方向来确定子弹发射方向并且，发射出的子弹的方向会沿着发射方向一直前进
 *      不随着坦克方向影响
 */
public class Bullet extends JFrame {
    private int x,y;  //子弹发射的初始位置
    private int BULLET_SIZE = 20;  //设置实心圆子弹的直径大小
    private int MOVE_LENGTH = 10;  //子弹的最小移动距离
    Tank tank;  //创建坦克类对象
    private boolean isMove = false;  //判断子弹是否运动

    private Tank.Direction bulletDirection = null;  //获得坦克的运动方向

    public Bullet(Tank tank) {
        this.tank = tank;
        this.x = this.tank.getX()+(this.tank.getTankSize()/2);
        this.y = this.tank.getY()+(this.tank.getTankSize()/5);
        System.out.println("this.x: "+this.x+"\tthis.y: "+this.y);
    }

    public void draw(Graphics g){
        g.fillOval(x,y,BULLET_SIZE,BULLET_SIZE);
        if (!isMove){
            this.bulletDirection = this.tank.getDirection();
        }
        new KeyListener().bulletMove();
    }

    //创建一个子弹类的自己移动类
    class KeyListener extends KeyAdapter{

        public void bulletMove(){
            switch (bulletDirection) {
                case U:
                    y -= MOVE_LENGTH;
                    isMove = true;
                    break;
                case UR:
                    x += MOVE_LENGTH;
                    y -= MOVE_LENGTH;
                    isMove = true;
                    break;
                case R:
                    x += MOVE_LENGTH;
                    isMove = true;
                    break;
                case RD:
                    x += MOVE_LENGTH;
                    y += MOVE_LENGTH;
                    isMove = true;
                    break;
                case D:
                    y += MOVE_LENGTH;
                    isMove = true;
                    break;
                case LD:
                    x -= MOVE_LENGTH;
                    y += MOVE_LENGTH;
                    isMove = true;
                    break;
                case L:
                    x -= MOVE_LENGTH;
                    isMove = true;
                    break;
                case LU:
                    x -= MOVE_LENGTH;
                    y -= MOVE_LENGTH;
                    isMove = true;
                    break;
            }
        }

    }

}
