package Edition1.Edition1_8;

import java.awt.*;
import java.util.*;
import java.util.List;

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

// 敌方坦克容器类对象
public class EnemyTank implements Runnable {
    int enemyTankNum = 0;
    List<Tank> tankList = new ArrayList<>();  // 创建一个存放敌方坦克的容器
    private static int SCREEN_WIDTH = 800;  // 游戏窗口的宽度
    private static int SCREEN_HEIGHT = 600;  // 游戏窗口的高度

    public EnemyTank(){
    }

    @Override
    public void run() {
        creatEnemyTank();
    }

    //批量创建敌方坦克的方法
    public void creatEnemyTank(){

        //新增敌方坦克来个缓冲
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (enemyTankNum < 10){  //如果游戏界面上敌方坦克的个数少于10个就创建新的敌方坦克
            //随机获取敌方坦克的位置
            int x = new Random().nextInt(SCREEN_WIDTH - 30 + 1)+10;
            int y = new Random().nextInt(SCREEN_HEIGHT - 30 + 1)+10;
            tankList.add(new Tank(x,y,false));
            enemyTankNum++;
        }
    }

    // 创建批量画出敌方坦克的方法
    public void drawTanks(Graphics g){
        if (enemyTankNum > 0){
            int i = 1;

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

            Iterator<Tank> tankIterator = tankList.iterator();
            while (tankIterator.hasNext()){
                Tank tank = tankIterator.next();

                // 为了避免线程中获取tank对象中对该tank对象进行修改，为tank该坦克加上锁
                synchronized (tank){
                    if (tank.getBLive()){
                        tank.draw(g);
                    }else {
                        tankIterator.remove();  //如果该敌方坦克已经死亡就移除敌方坦克容器中
                        enemyTankNum--;
                    }
                }
                i++;
            }
        }
    }
}
