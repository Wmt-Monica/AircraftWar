package Edition1.Edition1_9.Edition1_9_3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 版本1.9.3
 * 功能：
 *     让主战坦克添加计分功能
 * 步骤：
 *      每一发主战坦克打中的次数来计算得分
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

            /**
             * 迭代器：
             *
             * 当使用 fail-fast iterator 对 Collection 或 Map 进行迭代操作过程中尝试直接修改
             * Collection / Map 的内容时，即使是在单线程下运行，java.util.ConcurrentModificationException 异常也将被抛出。
             * Iterator 是工作在一个独立的线程中，并且拥有一个 mutex 锁。Iterator 被创建之后会
             * 建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会
             * 同步改变，所以当索引指针往后移动的时候就找不到要迭代的对象，所以按照 fail-fast 原
             * 则 Iterator 会马上抛出 java.util.ConcurrentModificationException 异常。
             *
             * 所以 Iterator 在工作的时候是不允许被迭代的对象被改变的。但你可以使用 Iterator 本身的方法 remove() 来删除对象，
             * Iterator.remove() 方法会在删除当前迭代对象的同时维护索引的一致性。
             *
             * 有意思的是如果你的 Collection / Map 对象实际只有一个元素的时候，
             * ConcurrentModificationException 异常并不会被抛出。这也就是为什么在 javadoc
             * 里面指出： it would be wrong to write a program that depended on this exception
             * for its correctness: ConcurrentModificationException should be used only to detect bugs.
             *
             * 修改后: tankIterator.remove();
             */

            // 之前使用foreach()方法来遍历敌方坦克，当敌方坦克被击中的时候，我们会让敌方坦克死亡
            // 并将敌方坦克从敌方坦克容器中去除，但是在遍历的时候，会发生
            // Exception in thread  Thread-0  java.util.ConcurrentModificationException
            // 报错信息，尝试容器中remove()的方法移除已经死亡的坦克在第二次遍历的时候移除操作不会同步
            // 导致会报错的结果


//            int i = 0;
//            for (Tank tank : tankList){
//                if (tank.getBLive()){
//                    tank.draw(g);
//                }else {
//                    tankList.remove(i);  //如果该敌方坦克已经死亡就移除敌方坦克容器中
//                    enemyTankNum--;
//                    i++;
//                }
//            }


            // 为了避免线程中获取tank对象中对该tank对象进行修改进行加锁的操作
//            Iterator<Tank> tankIterator = tankList.iterator();
//            synchronized (tankIterator){
//                while (tankIterator.hasNext()){
//                    Tank tank = tankIterator.next();
//
//                    if (tank.getBLive()){
//                        tank.draw(g);
//                        enemyTankAction(tank);
//                    }else {
//                        tankIterator.remove();  //如果该敌方坦克已经死亡就移除敌方坦克容器中
//                        enemyTankNum--;
//                    }
//                }
//            }

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
        if (enemyTank.time < 100){
            enemyTank.fireTime = 0;
            enemyTank.time ++;
            return enemyTank.bulletDirection;  // 返回原来的方向
        }else {
            enemyTank.time = 0;
            enemyTank.fireTime = 1;
        }

        synchronized (this){
            // 创建移动方向数组
            Tank.DirectionENUM[] enemyDirections = {
                    Tank.DirectionENUM.U, Tank.DirectionENUM.UR, Tank.DirectionENUM.R,
                    Tank.DirectionENUM.RD, Tank.DirectionENUM.D, Tank.DirectionENUM.LD,
                    Tank.DirectionENUM.L, Tank.DirectionENUM.LU, Tank.DirectionENUM.STOP};

            enemyTank.bulletDirection= enemyDirections[new Random().nextInt(9)];  // 随机产生[0,9)之间的方向数组对应的下标的数字
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
                bullet.draw(g);
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
