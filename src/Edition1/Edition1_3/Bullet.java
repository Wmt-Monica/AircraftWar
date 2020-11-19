package Edition1.Edition1_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * 版本1.3
 */
public class Bullet extends JFrame {
    private int x,y;  // 子弹发射的初始位置
    private int BULLET_SIZE = 15;  // 设置实心圆子弹的直径大小
    private int MOVE_LENGTH = 10;  // 子弹的最小移动距离

    private Tank.DirectionENUM bulletDirection;  // 获得坦克的运动方向

    public Bullet(int x, int y, Tank.DirectionENUM bulletDirection) {
        this.x = x;
        this.y = y;
        this.bulletDirection = bulletDirection;
    }

    public Bullet(){

    }

    public int  getBULLET_SIZE() {
        return this.BULLET_SIZE;
    }

    public void draw(Graphics g){
        g.fillOval(x,y,BULLET_SIZE,BULLET_SIZE);
        new KeyListener().bulletMove();
    }

    // 创建一个子弹类的自己移动类
    class KeyListener extends KeyAdapter{

        public void bulletMove(){
            switch (bulletDirection) {
                case U:
                    y -= MOVE_LENGTH;
                    break;
                case UR:
                    x += MOVE_LENGTH;
                    y -= MOVE_LENGTH;
                    break;
                case R:
                    x += MOVE_LENGTH;
                    break;
                case RD:
                    x += MOVE_LENGTH;
                    y += MOVE_LENGTH;
                    break;
                case D:
                    y += MOVE_LENGTH;
                    break;
                case LD:
                    x -= MOVE_LENGTH;
                    y += MOVE_LENGTH;
                    break;
                case L:
                    x -= MOVE_LENGTH;
                    break;
                case LU:
                    x -= MOVE_LENGTH;
                    y -= MOVE_LENGTH;
                    break;
            }
        }

    }

}
