package Edition1.Edition1_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * 版本1.6
 * 功能：
 *      将敌人坦克击毙
 * 步骤：
 *      1.Bullet中加入hitTank(Tank)方法，返回布尔类型
 *      2.碰撞检测的辅助类Rectangle
 *      3.为Tank和Bullet都加入getRect()方法
 *      4.当击中敌人的坦克时，坦克被打死，子弹也死亡
 *      5.增加控制Tank死亡的量bLive
 *      6.如果死去就不画了
 */
public class Bullet extends JFrame {
    private int x,y;  // 子弹发射的初始位置
    private int BULLET_SIZE = 15;  // 设置实心圆子弹的直径大小
    private int MOVE_LENGTH = 10;  // 子弹的最小移动距离
    private boolean bLive = true;

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

    public boolean getBLive(){
        return bLive;
    }

    public void death(){
        bLive = false;
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
            bLive = false;  //当炮弹已经出界，则炮弹被定义为已经死亡
        }
        return bLive;
    }

    //创建一个是否打中敌方坦克的方法
    public boolean hitTank(Tank enemyTank){
        if (x >= enemyTank.getX() && x <= enemyTank.getX()+enemyTank.getTANK_SIZE() &&
            y >= enemyTank.getY() && y <= enemyTank.getY()+enemyTank.getTANK_SIZE()){
                death();
                enemyTank.death();
                return true;
        }
        return false;
    }

}
