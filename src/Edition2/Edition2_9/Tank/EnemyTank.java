package Edition2.Edition2_9.Tank;
/**
 * 敌方坦克对象
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// 敌方坦克容器类对象
public class EnemyTank implements Runnable {
    static int enemyTankNum = 0; // 敌方坦克总数量
    private List<Tank> tankList = Collections.synchronizedList(new ArrayList<>());  // 创建一个存放敌方坦克的容器
    private List<Bullet> bulletList = new ArrayList<>();  //创建子弹容器类对象
    private Tank myTank;  // 创建主战坦克对象
    private static int SCREEN_WIDTH = Integer.parseInt(TankClientProperties.getProperties("SCREEN_WIDTH"));  // 游戏窗口的宽度
    private static int SCREEN_HEIGHT = Integer.parseInt(TankClientProperties.getProperties("SCREEN_HEIGHT"));  // 游戏窗口的高度
    private TankClient tankClient;
    private int Enemy_Tank_Num = Integer.parseInt(TankClientProperties.getProperties("Enemy_Tank_Num"));
    private int Enemy_Tank_Chang_Direction_Time = Integer.parseInt(TankClientProperties.getProperties("Enemy_Tank_Chang_Direction_Time"));

    /**
     *
     * @param tank 主战坦克对象
     * @param tankClient 敌方坦克对象
     */
    public EnemyTank(Tank tank, TankClient tankClient){
        this.myTank = tank;
        this.tankClient = tankClient;
    }

    /**
     *
     * @return 返回敌方发射的炮弹的集合 List 容器
     */
    public List<Bullet> getBulletList() {
        return bulletList;
    }

    /**
     *
     * @return 返回敌方坦克 List 集合对象
     */
    public List<Tank> getTankList() {
        return tankList;
    }

    /**
     *
     * @return 返回总敌方坦克对象个数
     */
    public static int getEnemyTankNum() {
        return enemyTankNum;
    }

    /**
     * 生成新的敌方坦克线程run()方法
     */
    @Override
    public void run() {
        creatEnemyTank();
    }

    /**
     * 创建敌方坦克的方法：
     * 步骤：
     *      1.随机生成新敌方坦克的x,y坐标，
     *      2.位置限制 ：不在墙里面，不超过主窗口界面
     *      3.将新创建好的敌方坦克装入敌方坦克容器中
     */
    public void creatEnemyTank(){

        int x;
        int y;
        if (enemyTankNum < 10){  //如果游戏界面上敌方坦克的个数少于10个就创建新的敌方坦克
            x = new Random().nextInt(SCREEN_WIDTH - 40 - 10 + 1)+10;
            y = new Random().nextInt(SCREEN_HEIGHT - 40 - 30 + 1)+30;
            //随机获取敌方坦克的位置
            while (!(Math.abs(x - myTank.getX()) <= 200) && !(Math.abs(y - myTank.getY()) <= 200)
                || (x + myTank.getTANK_SIZE() >= tankClient.getWall1().getX() && x <= tankClient.getWall1().getX()+tankClient.getWall1().getWeight()
                && y + myTank.getTANK_SIZE() >= tankClient.getWall1().getY() && y <= tankClient.getWall1().getY()+tankClient.getWall1().getHeight())
                    || (x + myTank.getTANK_SIZE() >= tankClient.getWall2().getX() && x <= tankClient.getWall2().getX()+tankClient.getWall2().getWeight()
                    && y + myTank.getTANK_SIZE() >= tankClient.getWall2().getY() && y <= tankClient.getWall2().getY()+tankClient.getWall2().getHeight())){
                 x = new Random().nextInt(SCREEN_WIDTH - 40 - 10 + 1)+10;
                 y = new Random().nextInt(SCREEN_HEIGHT - 40 - 30 + 1)+30;
            }
            synchronized (tankList){
                tankList.add(new Tank(x,y,false,tankClient));
            }
            enemyTankNum++;
        }

    }

    /**
     * 将敌方坦克容器中所有的敌方坦克对象批量绘画出，同时调用坦克的移动线程 (removeEnemyTank)
     * @param g 绘画坦克的 Graphics 画笔对象
     */
    // 创建批量画出敌方坦克的方法
    public void drawTanks(Graphics g){
        if (enemyTankNum > 0){
            new ergodicEnemyTankList(tankList,g).start();
            new removeEnemyTank(tankList).start();
        }
    }

    /**
     * 遍历敌方坦克容器的继承 Thread 的类
     */
    public class ergodicEnemyTankList extends Thread {

        Graphics g;
        List<Tank> tankList;

        /**
         *
         * @param tankList 敌方坦克集合容器对象
         * @param g  画出敌方坦克的 Graphics 画笔对象
         */
        public ergodicEnemyTankList(List<Tank> tankList, Graphics g){
            this.tankList = tankList;
            this.g = g;
        }

        /**
         * 敌方坦克容器中所有敌方坦克的活动 run() 方法
         */
        @Override
        public void run() {
            synchronized (tankList){
                if (enemyTankNum < Enemy_Tank_Num){
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
                            if (i < Enemy_Tank_Num){
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

    /**
     * 功能：从敌方坦克容器中移除已经处于死亡状态的敌方坦克
     * 继承了 Thread 的类
     */
    public class removeEnemyTank extends Thread{

        List<Tank> tankList;

        /**
         *
         * @param tankList 敌方坦克 List 容器
         */
        public removeEnemyTank(List<Tank> tankList){
            this.tankList = tankList;
        }

        /**
         * 该 removeEnemyTank 类继承了线程类，在 run() 方法中遍历敌方坦克容器移除已经处于死亡状态的敌方坦克对象
         */
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

    /**
     * 用于敌方坦克转换方向的方法
     * 功能：1.获取该坦克的step属性值，每移动一次敌方坦克对象，step属性值就减一，同时调用敌方坦克的移动方法
     *      2.当 step 等于 0 之后，随机获取新的移动方向
     *      3.当 step 等于 0 之后，使用 Random 类 在方向枚举数组中获取新的敌方坦克方向，并将新的方向当参数传给敌方坦克移动方法 tankMove()
     *
     * @param enemyTank 敌方坦克对象
     * @return 返回该敌方坦克的移动方向
     */
    public DirectionENUM enemyTankAction(Tank enemyTank){

        // 设置该坦克的最少移动多少步数才尝试改变移动方向
        if (enemyTank.time < Enemy_Tank_Chang_Direction_Time){
            enemyTank.fireTime = 0;
            enemyTank.time ++;
            return enemyTank.bulletDirection;  // 返回原来的方向
        }else {
            enemyTank.time = 0;
            enemyTank.fireTime = 1;
        }

        enemyTank.step = enemyTank.getStep();
        synchronized (this){
            // 创建移动方向数组
            DirectionENUM[] enemyDirections = {
                    DirectionENUM.U, DirectionENUM.UR, DirectionENUM.R,
                    DirectionENUM.RD, DirectionENUM.D, DirectionENUM.LD,
                    DirectionENUM.L, DirectionENUM.LU, DirectionENUM.STOP};

            enemyTank.bulletDirection= enemyDirections[new Random().nextInt(9)];  // 随机产生[0,9)之间的方向数组对应的下标的数字
            enemyTank.tankMove(enemyTank.bulletDirection);
        }
        if (enemyTank.step == 0 ){
            enemyTank.step = new Random().nextInt(  // 随机在 TANK_MAX_STEP 和 TANK_MIN_STEP 范围内重新给该敌方坦克对象设置 step 属性
                    Integer.parseInt(TankClientProperties.getProperties("TANK_MAX_STEP")) +
                            Integer.parseInt(TankClientProperties.getProperties("TANK_MIN_STEP")));
        }else {
            enemyTank.step--;
            enemyTank.tankMove(enemyTank.bulletDirection);
        }
        return enemyTank.bulletDirection;
    }


    /**
     * 功能：用于判断该坦克是否超出主窗口可视范围
     *
     * @param nowX 坦克现在所处的 x 坐标
     * @param nowY 坦克现在所处的 y 坐标
     * @return 返回坦克是否越界，（不超过主窗口的显示范围）
     */
    public boolean crossScreen(int nowX,int nowY){
        if (nowX < 10 || nowX > new TankClient().getScreenWidth()-40 || nowY < 30 || nowY > new TankClient().getScreenHeight()-40){
            return false;
        }
        return true;
    }

    /**
     * 继承了 Thread 的用于坦克发射炮弹的类
     */
    class fire extends Thread{
        Tank tank = null;
        Graphics g;

        /**
         *
         * @param tank 坦克对象
         * @param g 用于画出炮弹的 Graphics 画笔
         */
        public fire(Tank tank, Graphics g){
            this.tank = tank;
            this.g = g;
        }

        /**
         * 该继承了 Thread 类的 run() 方法
         * 功能：当坦克的 fireTime 属性值等于 1 时，该坦克对象就发射炮弹
         */
        @Override
        public void run() {
            if (tank.fireTime == 1){
                Bullet bullet = enemyTankFire();
                if (bulletList.contains(tank)){
                    bullet.draw(g);
                }
            }
        }

        /**
         * 根据调用的坦克对象的所在x,y坐标来创建的炮弹对象，并将该新创建的炮弹对象添加进炮弹 List 容器中
         *
         * @return 返回敌方坦克发射创建的炮弹对象
         */
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
