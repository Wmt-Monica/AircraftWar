package Edition2.Edition2_9.Tank;
/**
 * 用于坦克发射炮弹的炮弹类
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

public class Bullet extends JFrame {
    private int x, y;  // 子弹发射的初始位置
    private int BULLET_SIZE = Integer.parseInt(TankClientProperties.getProperties("BULLET_SIZE"));  // 设置实心圆子弹的直径大小
    private int BULLET_MOVE_LENGTH = Integer.parseInt(TankClientProperties.getProperties("BULLET_MOVE_LENGTH"));  // 炮弹的最小移动距离
    private boolean bLive = true;  // 炮弹默认状态下是存活状态
    private static int myTankWinTime = 0;  // 设置主战坦克射中敌方坦克的次数
    private static int enemyTankWinTime = 0;  // 设置敌方坦克射中主战坦克的次数

    private boolean good;  // 判断是是主战还是敌方坦克发射的子弹

    private DirectionENUM bulletDirection;  // 获得坦克的运动方向

    /**
     *
     * @param x 炮弹的发射x坐标
     * @param y 炮弹的发射y坐标
     * @param bulletDirection 炮弹的发射方向
     * @param good 判断是否为主战坦克发射的炮弹对象
     */
    public Bullet(int x, int y, DirectionENUM bulletDirection, boolean good) {
        this.x = x;
        this.y = y;
        this.bulletDirection = bulletDirection;
        this.good = good;
    }

    /**
     *
     * @param x 炮弹的发射x坐标
     * @param y 炮弹的发射y坐标
     */
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 炮弹类的无参构造方法
     */
    public Bullet() {

    }

    /**
     *
     * @return 返回炮弹所处的x坐标
     */
    public int getX(){
        return x;
    }

    /**
     *
     * @return 返回炮弹所处的y坐标
     */
    public int getY(){
        return y;
    }

    /**
     *
     * @return 返回实心圆炮弹直径大小
     */
    public int getBULLET_SIZE() {
        return this.BULLET_SIZE;
    }

    /**
     *
     * @return 主战坦克击毁敌方坦克的次数加一，同时返回主战坦克总共击毁敌方坦克的次数
     */
    public int addMyTankWinTime() {
        myTankWinTime++;
        return myTankWinTime;
    }

    /**
     *
     * @return 敌方坦克射中主战坦克的次数加一，同时返回敌方坦克总共射中主战坦克的次数
     */
    public int addEnemyTankWinTime() {
        enemyTankWinTime++;
        return enemyTankWinTime;
    }

    /**
     *
     * @return 返回该坦克对象是否为主战坦克对象
     */
    public boolean getGood() {
        return good;
    }

    /**
     * 将该坦克对象的 bLive 属性设置为 false ,将该坦克对象设置为已死亡状态
     */
    public void death() {
        bLive = false;
    }

    /**
     * 炮弹类的绘画出自己实体的方法，同时调用 KeyListener 对象的 bulletMove 坦克移动方法
     *
     * @param g 绘画出炮弹对象的 Graphics 画笔对象
     */
    public void draw(Graphics g) {
        Color startColor = g.getColor();
        if (good){
            g.setColor(Color.CYAN);
        }else {
            g.setColor(Color.GREEN);
        }
        g.fillOval(x, y, BULLET_SIZE, BULLET_SIZE);
        g.setColor(startColor);
        new KeyListener().bulletMove();
    }

    /**
     * 炮弹的移动类
     */
    class KeyListener extends KeyAdapter {

        /**
         * 根据炮弹的初始被坦克发射的方向来进行移动方法
         */
        public void bulletMove() {
            switch (bulletDirection) {
                case U:
                    y -= BULLET_MOVE_LENGTH;
                    break;
                case UR:
                    x += BULLET_MOVE_LENGTH;
                    y -= BULLET_MOVE_LENGTH;
                    break;
                case R:
                    x += BULLET_MOVE_LENGTH;
                    break;
                case RD:
                    x += BULLET_MOVE_LENGTH;
                    y += BULLET_MOVE_LENGTH;
                    break;
                case D:
                    y += BULLET_MOVE_LENGTH;
                    break;
                case LD:
                    x -= BULLET_MOVE_LENGTH;
                    y += BULLET_MOVE_LENGTH;
                    break;
                case L:
                    x -= BULLET_MOVE_LENGTH;
                    break;
                case LU:
                    x -= BULLET_MOVE_LENGTH;
                    y -= BULLET_MOVE_LENGTH;
                    break;
            }
        }

    }

    /**
     * 判断坦克的存活状态，当超过主窗口范围或者撞击到炮弹都会设置为死亡状态
     * @param wall1 墙对象 wall1,坦克不能穿过
     * @param wall2 墙对象 wall2，坦克不能穿过
     * @return 返回炮弹的存活状态
     */
    public boolean bulletLive(Wall wall1, Wall wall2) {
        if (x < 0 || x > new TankClient().getScreenWidth() || y < 0 || y > new TankClient().getScreenHeight()
            || (x >= wall1.getX() && x <= wall1.getX()+wall1.getWeight()) && (y >= wall1.getY() && y <= wall1.getY()+wall1.getHeight())
                || (x >= wall2.getX() && x <= wall2.getX()+wall2.getWeight()) && (y >= wall2.getY() && y <= wall2.getY()+wall2.getHeight())) {
            bLive = false;  //当炮弹已经出界，则炮弹被定义为已经死亡
        }
        return bLive;
    }

    /**
     * 根据传入的敌方坦克集合对象，判断该炮弹对象是否打中敌方坦克集合中的任意敌方坦克对象，如果打中，炮弹和被打中的敌方坦克对象都设置为死亡状态
     *
     * @param enemyTank 敌方坦克集合对象
     * @return 返回该炮弹是否打中传入的敌方坦克对象
     */
    public boolean hitTank(EnemyTank enemyTank) {
        for (Tank enemyTanks : enemyTank.getTankList()) {
            if (x >= enemyTanks.getX() - enemyTanks.getTANK_SIZE() && x <= enemyTanks.getX() + enemyTanks.getTANK_SIZE() &&
                    y >= enemyTanks.getY() - enemyTanks.getTANK_SIZE() && y <= enemyTanks.getY() + enemyTanks.getTANK_SIZE()) {
                death();
                enemyTanks.death();
                return true;
            }
        }
        return false;
    }

    /**
     * 根据传入的主战坦克对象，判断该炮弹对象是否打中主战坦克对象，如果打中，炮弹和被打中的主战坦克对象都设置为死亡状态
     * @param tank 主战坦克对象
     * @return 返回该炮弹是否打中主战坦克对象
     */
    public boolean hitTank(Tank tank) {
        if (x >= tank.getX() - tank.getTANK_SIZE() && x <= tank.getX() + tank.getTANK_SIZE() &&
                y >= tank.getY() - tank.getTANK_SIZE() && y <= tank.getY() + tank.getTANK_SIZE()) {
            death();
            if (tank.getLife() != 0){
                tank.reduceLife();
            }else {
                tank.death();
            }
            return true;
        }
        return false;
    }

}
