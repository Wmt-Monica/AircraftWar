package Edition2.Edition2_8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * 版本2.8
 *        配置文件的使用
 *        1.Properties类
 *        2.Singleton模式：
 *                      Singleton  [ˈsɪŋɡltən] 单例模式 在java中是指单例设计模式。
 *                      单例设计模式：
 *                           单例模式可以保证系统中，应用该模式的类一个类只有一个实例。即一个类只有一个对象实例。
 *                           例如：例如jvm运行环境的Runtime类
 *                           具体实现
 *                      需要：
 *                          （1）将构造方法私有化，使其不能在类的外部通过new关键字实例化该类对象。
 *                          （2）在该类内部产生一个唯一的实例化对象，并且将其封装为private static类型。
 *                          （3）定义一个静态方法返回这个唯一对象。
 */
public class Bullet extends JFrame {
    private int x, y;  // 子弹发射的初始位置
    private int BULLET_SIZE = Integer.parseInt(TankClientProperties.getProperties("BULLET_SIZE"));  // 设置实心圆子弹的直径大小
    private int BULLET_MOVE_LENGTH = Integer.parseInt(TankClientProperties.getProperties("BULLET_MOVE_LENGTH"));  // 炮弹的最小移动距离
    private boolean bLive = true;  // 炮弹默认状态下是存活状态
    private static int myTankWinTime = 0;  // 设置主战坦克射中敌方坦克的次数
    private static int enemyTankWinTime = 0;  // 设置敌方坦克射中主战坦克的次数

    private boolean good;  // 判断是是主战还是敌方坦克发射的子弹

    private DirectionENUM bulletDirection;  // 获得坦克的运动方向

    public Bullet(int x, int y, DirectionENUM bulletDirection, boolean good) {
        this.x = x;
        this.y = y;
        this.bulletDirection = bulletDirection;
        this.good = good;
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

    public int addMyTankWinTime() {
        myTankWinTime++;
        return myTankWinTime;
    }

    public int addEnemyTankWinTime() {
        enemyTankWinTime++;
        return enemyTankWinTime;
    }

    public boolean getGood() {
        return good;
    }

    public void death() {
        bLive = false;
    }

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

    // 创建一个子弹类的自己移动类
    class KeyListener extends KeyAdapter {

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

    //创建一个方法判断炮弹是否死亡
    public boolean bulletLive(Wall wall1, Wall wall2) {
        if (x < 0 || x > new TankClient().getScreenWidth() || y < 0 || y > new TankClient().getScreenHeight()
            || (x >= wall1.getX() && x <= wall1.getX()+wall1.getWeight()) && (y >= wall1.getY() && y <= wall1.getY()+wall1.getHeight())
                || (x >= wall2.getX() && x <= wall2.getX()+wall2.getWeight()) && (y >= wall2.getY() && y <= wall2.getY()+wall2.getHeight())) {
            bLive = false;  //当炮弹已经出界，则炮弹被定义为已经死亡
        }
        return bLive;
    }

    //创建一个是否打中敌方坦克的方法
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

    //创建一个是否打中主战坦克的方法
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
