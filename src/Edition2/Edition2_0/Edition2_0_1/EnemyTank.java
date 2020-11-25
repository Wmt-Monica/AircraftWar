package Edition2.Edition2_0.Edition2_0_1;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 版本2.0.1
 * 功能：
 *     让敌方坦克在移动随机步步数之后才改变方向发射
 */

// 敌方坦克容器类对象
public class EnemyTank implements Runnable {
    static int enemyTankNum = 0;
    List<Tank> tankList = Collections.synchronizedList(new ArrayList<>());  // 创建一个存放敌方坦克的容器
    private List<Bullet> bulletList = new ArrayList<>();  //创建子弹容器类对象
    Tank myTank = null;  // 创建主战坦克对象
    private static int SCREEN_WIDTH = 800;  // 游戏窗口的宽度
    private static int SCREEN_HEIGHT = 600;  // 游戏窗口的高度

    // 构造器
    public EnemyTank(Tank tank){
        this.myTank = tank;
    }

    public List<Bullet> getBulletList() {
        return bulletList;
    }

    @Override
    public void run() {
        creatEnemyTank();
    }

    //批量创建敌方坦克的方法
    public void creatEnemyTank(){

        int x;
        int y;
        if (enemyTankNum < 10){  //如果游戏界面上敌方坦克的个数少于10个就创建新的敌方坦克
            x = new Random().nextInt(SCREEN_WIDTH - 40 - 10 + 1)+10;
            y = new Random().nextInt(SCREEN_HEIGHT - 40 - 30 + 1)+30;
            //随机获取敌方坦克的位置
            while (!(Math.abs(x - myTank.getX()) <= 200) && !(Math.abs(y - myTank.getY()) <= 200)){
                 x = new Random().nextInt(SCREEN_WIDTH - 40 - 10 + 1)+10;
                 y = new Random().nextInt(SCREEN_HEIGHT - 40 - 30 + 1)+30;
            }
            synchronized (tankList){
                tankList.add(new Tank(x,y,false));
            }
            enemyTankNum++;
        }

    }

    // 创建批量画出敌方坦克的方法
    public void drawTanks(Graphics g){
        if (enemyTankNum > 0){
            new ergodicEnemyTankList(tankList,g).start();
            new removeEnemyTank(tankList).start();
        }
    }

    // 遍历敌方坦克容器的类
    public class ergodicEnemyTankList extends Thread {

        Graphics g;

        List<Tank> tankList;
        public ergodicEnemyTankList(List<Tank> tankList, Graphics g){
            this.tankList = tankList;
            this.g = g;
        }

        @Override
        public void run() {
            synchronized (tankList){
                if (enemyTankNum < 10){
                    creatEnemyTank();
                }
                int i = 0;
                for (Tank tank : tankList){
                    i ++;
                    if (tank.getBLive()){
                        enemyTankAction(tank);
                        new fire(tank,g).start();
                        Color bulletColor = g.getColor();
                        g.setColor(Color.BLUE);
                        synchronized (tank){
                            if (i < 10){
                                tank.OldX = tank.getX();
                                tank.OldY = tank.getY();
                                tank.draw(g);
                            }else {
                                tankList.remove(tank);
                            }
                        }
                        g.setColor(bulletColor);
                    }
                }
            }
        }
    }


    // 移除已经死亡的坦克
    public class removeEnemyTank extends Thread{

        List<Tank> tankList;
        public removeEnemyTank(List<Tank> tankList){
            this.tankList = tankList;
        }

        @Override
        public void run() {
            for (Tank tank : tankList){
                if (!tank.getBLive()){
                    tankList.remove(tank);
                    enemyTankNum--;
                }
            }
        }
    }

    // 创造敌方坦克转换方向的方法
    public Tank.DirectionENUM enemyTankAction(Tank enemyTank){

        // 设置该坦克的最少移动多少步数才尝试改变移动方向
        if (enemyTank.time < 100){
            enemyTank.fireTime = 0;
            enemyTank.time ++;
            return enemyTank.bulletDirection;  // 返回原来的方向
        }else {
            enemyTank.time = 0;
            enemyTank.fireTime = 1;
        }

        enemyTank.step = new Random().nextInt(20)+5;
        synchronized (this){
            // 创建移动方向数组
            Tank.DirectionENUM[] enemyDirections = {
                    Tank.DirectionENUM.U, Tank.DirectionENUM.UR, Tank.DirectionENUM.R,
                    Tank.DirectionENUM.RD, Tank.DirectionENUM.D, Tank.DirectionENUM.LD,
                    Tank.DirectionENUM.L, Tank.DirectionENUM.LU, Tank.DirectionENUM.STOP};

            enemyTank.bulletDirection= enemyDirections[new Random().nextInt(9)];  // 随机产生[0,9)之间的方向数组对应的下标的数字
            enemyTank.tankMove(enemyTank.bulletDirection);
        }
        if (enemyTank.step == 0 ){
            enemyTank.step = new Random().nextInt(20)+5;
        }else {
            enemyTank.step--;
            enemyTank.tankMove(enemyTank.bulletDirection);
        }
        return enemyTank.bulletDirection;
    }

    // 创建坦克是否越界的方法
    public boolean crossScreen(int nowX,int nowY){
        if (nowX < 10 || nowX > new TankClient().getScreenWidth()-40 || nowY < 30 || nowY > new TankClient().getScreenHeight()-40){
            return false;
        }
        return true;
    }

    // 创建发射子弹的方法
    class fire extends Thread{
        Tank tank = null;
        Graphics g;
        public fire(Tank tank, Graphics g){
            this.tank = tank;
            this.g = g;
        }
        @Override
        public void run() {
            if (tank.fireTime == 1){
                Bullet bullet = enemyTankFire();
                if (bulletList.contains(tank)){
                    bullet.draw(g);
                }
            }
        }

        public Bullet enemyTankFire(){
            Bullet bullet = new Bullet(
                    tank.getX()+(tank.getTankSize()-new Bullet().getBULLET_SIZE())/2,
                    tank.getY()+(tank.getTankSize()-new Bullet().getBULLET_SIZE())/2,
                    tank.bulletDirection,false); // 根据创建的坦克类，给该坦克类创建子弹类
            bulletList.add(bullet);
            return bullet;
        }
    }


}
