package Edition2.Edition2_9.Tank;

/**
 * 坦克对象类
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tank extends JFrame {
    private static int TANK_SIZE = Integer.parseInt(TankClientProperties.getProperties("TANK_SIZE"));  // 实心圆坦克的直径长
    private int x,y;  // 让坦克的位置x,y设置为变量
    private int TANK_MOVE_LENGTH = Integer.parseInt(TankClientProperties.getProperties("TANK_MOVE_LENGTH"));  // 坦克每次移动的最小距离
    private List<Bullet> bulletList = new ArrayList<>();  //创建子弹容器类对象
    private Bullet bullet = null;
    static int step = new Random().nextInt(Integer.parseInt(TankClientProperties.getProperties("TANK_MAX_STEP")))
            +Integer.parseInt(TankClientProperties.getProperties("TANK_MIN_STEP"));  // 设置step(步数)来控制敌方坦克转换方向的频率
    static int time = 0;  // 设置time控制敌方坦克发射炮弹的间隔时间
    static int fireTime = 0;  // 设置fireTime来空着敌方坦克发射炮弹的次数
    private final int ENEMY_TANK_MOVE_LENGTH = Integer.parseInt(TankClientProperties.getProperties("ENEMY_TANK_MOVE_LENGTH"));  // 敌方坦克的最小移动距离
    int OldX = 0;
    int OldY = 0;
    private TankClient tankClient;
    private int life = Integer.parseInt(TankClientProperties.getProperties("TANK_LIFE"));  // 设置主战坦克的生命值起始位100
    bloodStick blood = new bloodStick();
    private int TANK_ADD_BLOOD = Integer.parseInt(TankClientProperties.getProperties("TANK_ADD_BLOOD"));
    private int TANK_DEL_BLOOD = Integer.parseInt(TankClientProperties.getProperties("TANK_DEL_BLOOD"));
    private int TANK_LIFE = Integer.parseInt(TankClientProperties.getProperties("TANK_LIFE"));

    private boolean good;  // 用于判断是否为主战坦克对象
    private boolean bLive = true;  // 坦克是否存活的属性，默认状态为 true

    // 1.添加记录按键状态的布尔值
    private boolean U = false;  // 上
    private boolean R = false;  // 右
    private boolean D = false;  // 下
    private boolean L = false;  // 左


    private static DirectionENUM direction = DirectionENUM.STOP;  // 默认状态下，坦克处于静止
    DirectionENUM bulletDirection = DirectionENUM.R;  //在坦克最初静止状态时，子弹默认发射方向为向右

    /**
     *
     * @param x 创建 Tank 坦克对象时，该坦克对象的 x 坐标属性
     * @param y 创建 Tank 坦克对象时，该坦克对象的 y 坐标属性
     * @param good 创建 Tank 坦克对象时，该坦克对象是否为主战坦克的 good 属性
     * @param tankClient 主窗口对象
     */
    public Tank(int x, int y, boolean good, TankClient tankClient){
        this.x = x;
        this.y = y;
        this.good = good;
        this.tankClient = tankClient;
    }

    /**
     *
     * @return 返回该实心圆坦克的直径大小
     */
    public static int getTankSize() {
        return TANK_SIZE;
    }

    /**
     *
     * @return 返回该坦克对象所处的 x 坐标
     */
    public int getX(){
        return x;
    }

    /**
     *
     * @return 返回该坦克对象所处的 y 坐标
     */
    public int getY(){
        return y;
    }

    /**
     * 功能：将传入的 x 值来设置坦克所处的 x 坐标
     *
     * @param x 坦克的 x 坐标位置
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     * 功能：将传入的 y 值来设置坦克所处的 y 坐标
     *
     * @param y 坦克的 y 坐标位置
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     *
     * @return 返回该实心圆坦克的直径大小
     */
    public int getTANK_SIZE(){
        return TANK_SIZE;
    }

    /**
     *
     * @return 返回该坦克的是否存活的 bLive 属性值
     */
    public boolean getBLive(){
        return bLive;
    }

    /**
     *
     * @return 返回主战坦克的生命值
     */
    public int getLife() {
        return life;
    }

    /**
     * 功能：将主战坦克的属性值 life (生命值) 减去 TANK_DEL_BLOOD (主战坦克被敌方坦克击中后的失血量)
     */
    public void reduceLife() {
        life -= TANK_DEL_BLOOD;
    }

    /**
     * 功能：主战坦克吃掉血块后的增加 life (生命值)，直至生命值达到最大 TANK_LIFE
     */
    public void addLife(){
        if (life < TANK_LIFE){
            life += TANK_ADD_BLOOD;
        }
    }

    /**
     *
     * @return 返回敌方坦克移动的 step (步数) 属性
     */
    public static int getStep() {
        return step;
    }

    /**
     * 功能：将该坦克对象的 bLive 属性设置为 false (死亡状态)
     */
    public void death(){
        bLive = false;
    }

    /**
     *
     * @return 返回炮弹 List 容器
     */
    public List<Bullet> getBulletList(){
        return bulletList;
    }

    /**
     * 功能：坦克类画出自己 (实心圆) 的方法
     *
     * @param g 绘画坦克对象的 Graphics 画笔对象
     */
    public void draw(Graphics g){
        if (bLive){  //如果坦克还活着就画出实心坦克
            Color startColor = g.getColor();
            if (good){
                g.setColor(Color.PINK);
            }else {
                g.setColor(Color.BLUE);
            }
            g.fillOval(x,y,TANK_SIZE,TANK_SIZE);

            g.setColor(Color.YELLOW);  //设置绘画炮筒的颜色

            switch (bulletDirection) {
                case U:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x+TANK_SIZE/2,y);
                    break;
                case UR:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x+TANK_SIZE,y);
                    break;
                case R:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x+TANK_SIZE,y+TANK_SIZE/2);
                    break;
                case RD:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x+TANK_SIZE,y+TANK_SIZE);
                    break;
                case D:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x+TANK_SIZE/2,y+TANK_SIZE);
                    break;
                case LD:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x,y+TANK_SIZE);
                    break;
                case L:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x,y+TANK_SIZE/2);
                    break;
                case LU:
                    g.drawLine(x+TANK_SIZE/2,y+TANK_SIZE/2,x,y);
                    break;
            }

            g.setColor(startColor);  //重新将画笔设置为坦克的颜色

            if (good){  // 如果是主战坦克且处于存活状态就用户操控坦克的方向来进行移动
                blood.draw(g);
                new KeyListener().tankMove();
            }
        }

    }


    /**
     * 创建一个键盘监听类
     * 注意：这里使用继承KeyAdapter类而不是实现KeyListener的优点
     * 继承KeyAdapter已经帮你实现了空的keyTyped(),keyPressed()和keyReleased()方法，我们需要使用那个方法自主重写即可
     * 但是实现keyListener接口，我们即使不需要实现某些方法，仍要将空方法写出来
     */
    // 坦克类自己运行时的监听方法
    class KeyListener extends KeyAdapter {

        // 键盘信息三种方法的区别
        // keyType：当用户按下键盘上的任意键时触发，如果按住不放的话，会重复触发此事件；
        // keyPressed：当用户按下键盘上的字符键时触发，如果按住不让的话，会重复触发此事件；
        // keyRelease：当用户释放键盘上的字符键时触发。

        // 重写keyPressed()方法

        /**
         * 功能：根据用户的按键操作来设置布尔类型的方向判定，最后调用 judgeDirection() 方法来判定坦克的移动方向
         *
         * @param e 操控着的按键信息
         */
        @Override
        public void keyPressed(KeyEvent e) {

            // getKeyCode():键盘上每一个按钮都有对应码(Code),可用来查知用户按了什么键，返回当前按钮的数值
            int TankDirection = e.getKeyCode();  //获取坦克前进的方向
            switch (TankDirection){
                case KeyEvent.VK_LEFT :
                    L = true;
                    break;
                case KeyEvent.VK_RIGHT :
                    R = true;
                    break;
                case KeyEvent.VK_UP :
                    U = true;
                    break;
                case KeyEvent.VK_DOWN :
                    D = true;
                    break;
            }

            judgeDirection();
        }

        /**
         * 功能：根据用户的按键操作，给该坦克对象 direction (移动方向属性) 设置方向
         */
        public void judgeDirection(){
            //判断坦克移动方向
            if (U && !R && !D && !L)
               bulletDirection = direction = DirectionENUM.U;  //向上
            else if (U && R && !D && !L)
                bulletDirection = direction = DirectionENUM.UR;  //向右上
            else if (!U && R && !D && !L)
                bulletDirection = direction = DirectionENUM.R;  //向右
            else if (!U && R && D && !L)
                bulletDirection = direction = DirectionENUM.RD;  //向右下
            else if (!U && !R && D && !L)
                bulletDirection = direction = DirectionENUM.D;  //向下
            else if (!U && !R && D && L)
                bulletDirection = direction = DirectionENUM.LD;  //向左下
            else if (!U && !R && !D && L)
                bulletDirection = direction = DirectionENUM.L;  //向左
            else if (U && !R && !D && L)
                bulletDirection = direction = DirectionENUM.LU;  //向左上
            else if (!U && !R && !D && !L)
                direction = DirectionENUM.STOP;  //停止

        }

        /**
         * 功能：每当坦克根据 direction 属性值移动一次之后，就将确定坦克方向的属性恢复初始状态
         */
        public void defaultDirection(){
            direction = DirectionENUM.STOP;
            U = false;
            R = false;
            D = false;
            L = false;
        }

        /**
         * 重写keyReleased()方法：此是按键放开后的执行操作
         *
         * 功能：
         *      1.根据用户放下 control 键发射炮弹
         *      2.根据用户放下各个方向键调用 defaultDirection() 方法,初始化控制坦克方向的各个属性
         *      3.根据用户按下 1 键主战坦克发射超级炮弹
         *      4.根据用户按下 2 键，当主战坦克已经死亡状态，重新开始游戏
         *
         * @param e 用户放下的按键信息
         */
        @Override
        public void keyReleased(KeyEvent e) {
            int TankDirection = e.getKeyCode();  // 获取坦克前进的方向
            switch (TankDirection){
                case KeyEvent.VK_CONTROL :
                    if (bLive){  // 如果坦克处于存活状态按下可以通过按键发射炮弹
                        bullet = fire();
                    }
                    break;
                case KeyEvent.VK_LEFT : // 放下向左键盘
                    defaultDirection();
                    break;
                case KeyEvent.VK_RIGHT :  // 放下向右键盘
                    defaultDirection();
                    break;
                case KeyEvent.VK_UP :  // 放下向上键盘
                    defaultDirection();
                    break;
                case KeyEvent.VK_DOWN :  // 放下向下键盘
                    defaultDirection();
                    break;
                case KeyEvent.VK_1 :  // 放下1键打出超级炮弹
                    superFire();
                    break;
                case KeyEvent.VK_2 :  // 当放下F2键时候，如果主战坦克已经死亡就重新开始
                    if (!bLive){
                        bLive = true;
                        life = TANK_LIFE;
                        tankClient.restart();  // 得分情况全部重0开始
                    }
                    break;
            }
        }

        /**
         * 功能：根据该坦克的 x,y坐标创建一个新的炮弹对象，并将该主战炮弹对象加入炮弹 List 集合中，并将该炮弹对象返回
         *
         * @return 返回根据该主战坦克对象的 x,y 坐标创建的炮弹对象
         */
        public Bullet fire(){
            Bullet bullet = new Bullet(
                    x+(getTankSize()-new Bullet().getBULLET_SIZE())/2,
                    y+(getTankSize()-new Bullet().getBULLET_SIZE())/2,
                       bulletDirection,good); // 根据创建的坦克类，给该坦克类创建子弹类
            bulletList.add(bullet);
            return bullet;
        }

        /**
         * 功能：根据主战坦克的 x,y 坐标位置创建炮弹数组，该炮弹数组各个方向都创建一个超级炮弹，向八个方向发射炮弹
         */
        public void superFire(){
            DirectionENUM[] directions = {DirectionENUM.U, DirectionENUM.UR, DirectionENUM.R,
                    DirectionENUM.RD, DirectionENUM.D, DirectionENUM.LD, DirectionENUM.L, DirectionENUM.LU};
            for (DirectionENUM d : directions){
                Bullet bullet = new Bullet(
                        x+(getTankSize()-new Bullet().getBULLET_SIZE())/2,
                        y+(getTankSize()-new Bullet().getBULLET_SIZE())/2,
                        d,good); // 根据创建的坦克类，给该坦克类创建子弹类
                bulletList.add(bullet);
            }
        }

        /**
         * 功能：根据主战坦克的 direction (方向属性) 进行坦克的移动
         */
        public  void tankMove(){
            switch (direction){
                case U:
                    if (crossScreen(x,y - TANK_MOVE_LENGTH)){
                        y -= TANK_MOVE_LENGTH;
                    }
                    break;
                case UR:
                    if (crossScreen(x + TANK_MOVE_LENGTH,y - TANK_MOVE_LENGTH)){
                        x += TANK_MOVE_LENGTH;
                        y -= TANK_MOVE_LENGTH;
                    }
                    break;
                case R:
                    if (crossScreen(x + TANK_MOVE_LENGTH,y)){
                        x += TANK_MOVE_LENGTH;
                    }
                    break;
                case RD:
                    if (crossScreen(x + TANK_MOVE_LENGTH, y + TANK_MOVE_LENGTH)){
                        x += TANK_MOVE_LENGTH;
                        y += TANK_MOVE_LENGTH;
                    }
                    break;
                case D:
                    if (crossScreen(x,y + TANK_MOVE_LENGTH)){
                        y += TANK_MOVE_LENGTH;
                    }
                    break;
                case LD:
                    if (crossScreen(x - TANK_MOVE_LENGTH,y + TANK_MOVE_LENGTH)){
                        x -= TANK_MOVE_LENGTH;
                        y += TANK_MOVE_LENGTH;
                    }
                    break;
                case L:
                    if (crossScreen(x - TANK_MOVE_LENGTH,y)){
                        x -= TANK_MOVE_LENGTH;
                    }
                    break;
                case LU:
                    if (crossScreen(x - TANK_MOVE_LENGTH,y - TANK_MOVE_LENGTH)){
                        x -= TANK_MOVE_LENGTH;
                        y -= TANK_MOVE_LENGTH;
                    }
                    break;
                case STOP:
                    break;
            }
        }


    }

    /**
     * 功能：根据敌方坦克的 direction (移动方向)来移动敌方坦克的位置
     *
     * @param direction 敌方坦克对象的 direction (移动方向)
     */
    public void tankMove(DirectionENUM direction){
        switch (direction){
            case U:
                if (crossScreen(x,y - ENEMY_TANK_MOVE_LENGTH)){
                    y -= ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case UR:
                if (crossScreen(x + ENEMY_TANK_MOVE_LENGTH,y - ENEMY_TANK_MOVE_LENGTH)){
                    x +=  ENEMY_TANK_MOVE_LENGTH;
                    y -= ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case R:
                if (crossScreen(x + ENEMY_TANK_MOVE_LENGTH,y)){
                    x += ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case RD:
                if (crossScreen(x + ENEMY_TANK_MOVE_LENGTH, y + ENEMY_TANK_MOVE_LENGTH)){
                    x += ENEMY_TANK_MOVE_LENGTH;
                    y += ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case D:
                if (crossScreen(x,y + ENEMY_TANK_MOVE_LENGTH)){
                    y += ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case LD:
                if (crossScreen(x - ENEMY_TANK_MOVE_LENGTH,y + ENEMY_TANK_MOVE_LENGTH)){
                    x -=  ENEMY_TANK_MOVE_LENGTH;
                    y += ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case L:
                if (crossScreen(x - ENEMY_TANK_MOVE_LENGTH,y)){
                    x -= ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case LU:
                if (crossScreen(x - ENEMY_TANK_MOVE_LENGTH,y - ENEMY_TANK_MOVE_LENGTH)){
                    x -= ENEMY_TANK_MOVE_LENGTH;
                    y -= ENEMY_TANK_MOVE_LENGTH;
                }
                break;
            case STOP:
                break;
        }
    }

    /**
     *
     * @param nowX 该坦克现处的 x 坐标位置
     * @param nowY 该坦克现处的 y 坐标位置
     * @return 返回该坦克是否超出主窗口的可视范围和是否穿过墙面对象
     */
    public boolean crossScreen(int nowX,int nowY){
        if (nowX < 10 || nowX > new TankClient().getScreenWidth()-40 || nowY < 30 || nowY > new TankClient().getScreenHeight()-40){
            return false;
        }
        if ((nowX+TANK_SIZE >= tankClient.getWall1().getX() && nowX <=tankClient.getWall1().getX()+tankClient.getWall1().getWeight()
                && nowY+TANK_SIZE >= tankClient.getWall1().getY() && nowY <= tankClient.getWall1().getY()+tankClient.getWall1().getHeight())
                || (nowX+TANK_SIZE >= tankClient.getWall2().getX() && nowX <= tankClient.getWall2().getX()+tankClient.getWall2().getWeight()
                && nowY+TANK_SIZE >= tankClient.getWall2().getY() && nowY <= tankClient.getWall2().getY()+tankClient.getWall2().getHeight())){
            return false;
        }
        for (Tank t1 : tankClient.getAllTanks()){
            for (Tank t2 : tankClient.getAllTanks()){
                if (t2 != t1){   // 如果遍历的这个坦克不是本身
                    System.out.println("nowX = "+nowX+"\tnowY = "+nowY+"\totherTank.X = "+t2.getX()+"\totherTank.Y = "+t2.getY());
                    if (nowX + TANK_SIZE >= t2.getX() && nowX <= t2.getX() +TANK_SIZE && nowY + TANK_SIZE >= t2.getY() && nowY <= t2.getY() + TANK_SIZE){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 功能：在坦克类中创建一个 bloodStick 内部类对象 (主战坦克的生命血条类)
     */
    class bloodStick{

        /**
         * 功能：画出主战坦克的生命血条
         *
         * @param g 画出主战坦克的生命血条的 Graphics 画笔对象
         */
        // 创建draw()方法画出自己
        public void draw(Graphics g){
            Color startColor = g.getColor();
            g.setColor(Color.RED);
            // 先画出血条框
            g.drawRect(x-5,y-15,TANK_SIZE+10,10);
            g.fillRect(x-5,y-15,TANK_SIZE*life/TANK_LIFE+10,10);
            g.setColor(startColor);
        }
    }


}
