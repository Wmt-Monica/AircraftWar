package Edition1.Edition1_8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * 版本1.8
 * 功能：
 *      添加多辆坦克
 *      为所有敌方坦克被击中后添加爆破功能
 * 步骤：
 *      1.用容器来装敌人的Tank
 *      2.向容器中装入多辆敌人的坦克
 *      3.画出来
 */
public class Bullet extends JFrame {
    private int x, y;  // 子弹发射的初始位置
    private int BULLET_SIZE = 15;  // 设置实心圆子弹的直径大小
    private int MOVE_LENGTH = 10;  // 炮弹的最小移动距离
    private boolean bLive = true;  // 炮弹默认状态下是存活状态

    private Tank.DirectionENUM bulletDirection;  // 获得坦克的运动方向

    public Bullet(int x, int y, Tank.DirectionENUM bulletDirection) {
        this.x = x;
        this.y = y;
        this.bulletDirection = bulletDirection;
    }

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Bullet() {

    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getBULLET_SIZE() {
        return this.BULLET_SIZE;
    }

    public void death() {
        bLive = false;
    }

    public void draw(Graphics g) {
        g.fillOval(x, y, BULLET_SIZE, BULLET_SIZE);
        new KeyListener().bulletMove();
    }

    // 创建一个子弹类的自己移动类
    class KeyListener extends KeyAdapter {

        public void bulletMove() {
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
    public boolean bulletLive() {
        if (x < 0 || x > new TankClient().getScreenWidth() || y < 0 || y > new TankClient().getScreenHeight()) {
            bLive = false;  //当炮弹已经出界，则炮弹被定义为已经死亡
        }
        return bLive;
    }

    //创建一个是否打中敌方坦克的方法
    public boolean hitTank(EnemyTank enemyTank) {
        for (Tank enemyTanks : enemyTank.tankList) {
            if (x >= enemyTanks.getX() - enemyTanks.getTANK_SIZE() && x <= enemyTanks.getX() + enemyTanks.getTANK_SIZE() &&
                    y >= enemyTanks.getY() - enemyTanks.getTANK_SIZE() && y <= enemyTanks.getY() + enemyTanks.getTANK_SIZE()) {
                death();
                enemyTanks.death();
                return true;
            }
        }
        return false;
    }

}
