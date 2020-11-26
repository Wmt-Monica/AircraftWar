package Edition2.Edition2_8;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
public class TankClient extends JFrame {

    // 游戏界面的一些常量
    private static int SCREEN_WIDTH = Integer.parseInt(TankClientProperties.getProperties("SCREEN_WIDTH"));  // 游戏窗口的宽度
    private static int SCREEN_HEIGHT = Integer.parseInt(TankClientProperties.getProperties("SCREEN_HEIGHT"));  // 游戏窗口的高度
    private static int SCREEN_X = Integer.parseInt(TankClientProperties.getProperties("SCREEN_X"));  // 游戏窗口的位置的X坐标
    private static int SCREEN_Y = Integer.parseInt(TankClientProperties.getProperties("SCREEN_Y"));  // 游戏窗口的位置的Y坐标
    private static int TANK_X = Integer.parseInt(TankClientProperties.getProperties("TANK_X"));  // 实心圆坦克的位置坐标X
    private static int TANK_Y = Integer.parseInt(TankClientProperties.getProperties("TANK_Y"));  // 实心圆坦克的位置坐标Y
    private static boolean ME_GOOD = true;  //创建一个属于自己的坦克的good值
    private int myTankWinTime = 0;  // 创建主战坦克打中敌方坦克的次数
    private int enemyTankWinTime = 0;  // 创建敌方坦克打中主战坦克的次数
    private Wall wall1 = new Wall(250,80,30,350,this);  // 创造一个实心墙wall1对象
    private Wall wall2 = new Wall(450,200,300,30,this);  // 创造一个实心墙wall1对象
    private Tank tank = new Tank(TANK_X,TANK_Y,ME_GOOD,this);  // 创建一个坦克类对象实例
    private EnemyTank enemyTanks = new EnemyTank(tank,this);  // 创建一个敌方坦克容器类对象
    private List<Tank> allTanks = new ArrayList<>();

    Bullet bullet = null;
    List<Bullet> bulletList = new ArrayList<>();  //创建子弹容器类对象
    List<Bullet> enemyTankBulletList = enemyTanks.getBulletList();  //创建子弹容器类对象
    BloodBlock bloodBlock = new BloodBlock(this);  // 创建血块对象

    Image backImg = null;  // 用于update()方法中实现双缓冲效果的背景图片

    public int getScreenWidth(){
        return SCREEN_WIDTH;
    }

    public int getScreenHeight(){
        return SCREEN_HEIGHT;
    }

    public Wall getWall1() {
        return wall1;
    }

    public Wall getWall2() {
        return wall2;
    }

    public List<Tank> getAllTanks() {
        return allTanks;
    }

    public Tank getTank() {
        return tank;
    }

    // 游戏重新开始
    public void restart(){
        myTankWinTime = 0;
        enemyTankWinTime = 0;
    }

    public TankClient(){
        allTanks.addAll(enemyTanks.getTankList());
        allTanks.add(tank);
    }

    // 重写paint()方法，将实心圆坦克画出来
    @Override
    public void paint(Graphics g){
        Color foregroundColor = g.getColor();  // 获取窗口前景色
        g.setColor(Color.WHITE);
        bulletList = tank.getBulletList();  // 获取坦克的炮弹容器
        // 将字符串画在游戏窗口上，前面时要绘画的字符，后面两个参数是，字符串的坐标X，和坐标Y
        g.drawString("存活子弹个数："+bulletList.size(),10,50);
        g.drawString("存活敌方坦克个数："+enemyTanks.enemyTankNum,10,80);
        g.drawString("得分："+myTankWinTime,10,110);
        g.drawString("被打次数："+enemyTankWinTime,10,140);
        g.drawString("主战坦克生命值："+tank.getLife(),10,170);

        tank.draw(g);  // 给坦克画笔自己调用方法画出自己

        if (enemyTanks.enemyTankNum < 10){
            new Thread(enemyTanks).start();
        }
        enemyTanks.drawTanks(g);  //给敌方坦克画笔调用自己的draw()方法画出自己

        wall1.draw(g);
        wall2.draw(g);

        g.setColor(Color.CYAN);  // 将画笔设置为水绿色绘画出子弹
        viewBulletList(bulletList,g);
        viewBulletList(enemyTankBulletList,g);

        bloodBlock.draw(g);  // 绘画出血块对象

        // 不要改变前景色，最后将之前获取到的颜色设置回去
        g.setColor(foregroundColor);
    }

    // 创建主战坦克和敌方坦克的炮弹容器中所有炮弹的显示方法
    public void viewBulletList(List<Bullet> bulletList, Graphics g){
        for (int i = 0; i < bulletList.size(); i++){
            boolean myFlag = false;
            boolean enemyFlag = false;
            bullet = bulletList.get(i);  // 容器下标是从0开始
            // 如果用户按了ctrl键，就将给子弹对象画笔画出自己
            if (bullet != null){  // 给子弹画笔调用自身类的draw()方法画出自己
                if (bullet.getGood()){
                    myFlag = bullet.hitTank(enemyTanks);  //判断炮弹是否打中敌方坦克
                    if (myFlag){

                        myTankWinTime = bullet.addMyTankWinTime();
                        Explode explode = new Explode(bullet.getX(),bullet.getY(),tank);
                        explode.draw(g);
                    }
                }else {
                    enemyFlag = bullet.hitTank(tank);
                    if (enemyFlag){
                        enemyTankWinTime = bullet.addEnemyTankWinTime();
                    }
                }
                if (bullet.bulletLive(wall1,wall2)){
                    bullet.draw(g);
                }else {
                    bulletList.remove(bullet);  // 如果炮弹已经出界导致死亡就将该炮弹从炮弹容器中去除
                }
            }
        }
    }

    // 重写update()方法，窗口在重画时，先加载的时update()方法，我们可以在
    // update()方法中提前画好一张图片，然后再显示出来，这样就实现了双缓冲
    @Override
    public void update(Graphics g) {
        // 假如图片为null,就创建一个图片大小设置为800px,600px和游戏窗口大小一致
        if (backImg == null){
            backImg = this.createImage(SCREEN_WIDTH,SCREEN_HEIGHT);
        }

        Graphics gBackImg = backImg.getGraphics();  // 因为我们要获得backImg的画笔来对图片进行绘画

        // 解决游戏窗口背景刷新更新不上
        Color tankColor = gBackImg.getColor();
        gBackImg.setColor(Color.BLACK);
        gBackImg.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        gBackImg.setColor(tankColor);

        paint(gBackImg);  // 使用涂片的画笔将游戏窗口的样式提前绘画好再图片上
        g.drawImage(backImg,0,0,null);  // 将整个提前画好的图片绘画在窗口里
    }

    //创建一个游戏窗口对象方法
    public void windowFrame(){
        this.setBounds(SCREEN_X,SCREEN_Y,SCREEN_WIDTH,SCREEN_HEIGHT);  // 使用setBounds()方法直接设置窗口坐标位置和窗口大小
        this.setResizable(false);  // 设置游戏窗口不可随意改变窗口大小
        this.setTitle("AircraftWar");  // 设置窗口标题
        this.setVisible(true);  // 设置窗口可见
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  // 设置窗口叉掉就停止程序
        this.setBackground(Color.BLACK);
        this.addKeyListener(tank.new KeyListener());  // 给游戏界面调用坦克类自己的监听动作方法
        if (bullet != null){
            this.addKeyListener(bullet.new KeyListener());
        }
        new Thread(new rePaintThread()).start();
    }

    // 创建一个线程类，使用线程不断重画
    class rePaintThread implements Runnable{
        @Override
        public void run() {
            while (true){
                repaint();  // 使用repaint()方法来实现paint()方法的重画
                update(getGraphics());  // 使用update使用双缓冲不断地刷新游戏窗口

                // 让线程每重画一次就休息20ms
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        TankClient tankClient = new TankClient();
        tankClient.windowFrame();
    }
}
