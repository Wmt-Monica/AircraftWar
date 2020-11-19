package Edition1.Edition1_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * 版本1.4
 * 功能：
 *      1.解决炮弹不消亡的问题
 *      2.解决坦克出界的问题
 * 步骤：
 *      1.假如控制炮弹的生死的量bLive (Bullet)
 *      2.当炮弹已经死去就不需要对其重画
 *      3.当炮弹飞出边界就死亡
 *      4.当炮弹死亡就从容器中去除
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

    //创建一个方法判断炮弹是否死亡
    public boolean bulletLive(){
        if (x < 0 || x > new TankClient().getScreenWidth() || y < 0 || y > new TankClient().getScreenHeight()){
            return false;  //当炮弹已经出界，则炮弹被定义为已经死亡
        }
        return true;
    }

}
