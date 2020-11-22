package Edition1.Edition1_9.Edition1_9_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 版本1.9.2
 * 功能：
 *      让敌方坦克自由动起来
 *      让坦克可以相隔一定时间内发射炮弹
 * 步骤：
 *      随机产生方向数组中的方向对应的方向常量
 */
public class Tank extends JFrame {
    private static int TANK_SIZE = 30;  // 实心圆坦克的直径长
    private int x,y;  // 让坦克的位置x,y设置为变量
    private int MOVE_LENGTH = 5;  // 坦克每次移动的最小距离
    private List<Bullet> bulletList = new ArrayList<>();  //创建子弹容器类对象
    private Bullet bullet = null;
    static int time = 0;  // 设置time来控制敌方坦克转换方向的频率
    static int fireTime = 0;  // 设置fireTime来空着敌方坦克发射炮弹的次数
    private final int ENEMY_TANK_MOVE_LENGTH = 50;  // 敌方坦克的最小移动距离

    private boolean good;
    private boolean bLive = true;

    // 1.添加记录按键状态的布尔值
    private boolean U = false;  // 上
    private boolean R = false;  // 右
    private boolean D = false;  // 下
    private boolean L = false;  // 左


    // 2.添加代表方向的量（使用枚举）
    enum DirectionENUM {U, UR, R, RD, D, LD, L, LU, STOP}
    private static DirectionENUM direction = DirectionENUM.STOP;  // 默认状态下，坦克处于静止
    DirectionENUM bulletDirection = DirectionENUM.R;  //在坦克最初静止状态时，子弹默认发射方向为向右

    public Tank(int x, int y,boolean good){
        this.x = x;
        this.y = y;
        this.good = good;
    }

    // 创建getTankSize()方法获得坦克实心圆的直径
    public static int getTankSize() {
        return TANK_SIZE;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getTANK_SIZE(){
        return TANK_SIZE;
    }

    public boolean getBLive(){
        return bLive;
    }

    //当坦克被打中
    public void death(){
        bLive = false;
    }

    public List<Bullet> getBulletList(){
        return bulletList;
    }

    // 坦克类画出自己的方法
    public void draw(Graphics g){
        if (bLive){  //如果坦克还活着就画出实心坦克
            g.fillOval(x,y,TANK_SIZE,TANK_SIZE);

            Color tankColor = g.getColor();  //提前获取到坦克的颜色
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

            g.setColor(tankColor);  //重新将画笔设置为坦克的颜色

            if (good){  // 如果是主战坦克就用户操控坦克的方向来进行移动
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

        // 3.创建根据按键状态确定Tank方向的方法
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


        // 每移动一次，就将之前的布尔值方向和坦克运动方向恢复为默认的false和STOP
        public void defaultDirection(){
            direction = DirectionENUM.STOP;
            U = false;
            R = false;
            D = false;
            L = false;
        }

        // 重写keyReleased()方法：此是按键放开后的执行操作
        @Override
        public void keyReleased(KeyEvent e) {
            int TankDirection = e.getKeyCode();  // 获取坦克前进的方向
            switch (TankDirection){
                case KeyEvent.VK_CONTROL :
                    bullet = fire();
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
            }
        }

        // 创建发射子弹的方法
        public Bullet fire(){
            Bullet bullet = new Bullet(
                    x+(getTankSize()-new Bullet().getBULLET_SIZE())/2,
                    y+(getTankSize()-new Bullet().getBULLET_SIZE())/2,
                       bulletDirection,good); // 根据创建的坦克类，给该坦克类创建子弹类
            bulletList.add(bullet);
            return bullet;
        }

        // 4.根据方向进行下一步的移动（tankMove）
        public  void tankMove(){
            switch (direction){
                case U:
                    if (crossScreen(x,y - MOVE_LENGTH)){
                        y -= MOVE_LENGTH;
                    }
                    break;
                case UR:
                    if (crossScreen(x + MOVE_LENGTH,y - MOVE_LENGTH)){
                        x +=  MOVE_LENGTH;
                        y -= MOVE_LENGTH;
                    }
                    break;
                case R:
                    if (crossScreen(x + MOVE_LENGTH,y)){
                        x += MOVE_LENGTH;
                    }
                    break;
                case RD:
                    if (crossScreen(x + MOVE_LENGTH, y + MOVE_LENGTH)){
                        x += MOVE_LENGTH;
                        y += MOVE_LENGTH;
                    }
                    break;
                case D:
                    if (crossScreen(x,y + MOVE_LENGTH)){
                        y += MOVE_LENGTH;
                    }
                    break;
                case LD:
                    if (crossScreen(x - MOVE_LENGTH,y + MOVE_LENGTH)){
                        x -=  MOVE_LENGTH;
                        y += MOVE_LENGTH;
                    }
                    break;
                case L:
                    if (crossScreen(x - MOVE_LENGTH,y)){
                        x -= MOVE_LENGTH;
                    }
                    break;
                case LU:
                    if (crossScreen(x - MOVE_LENGTH,y - MOVE_LENGTH)){
                        x -= MOVE_LENGTH;
                        y -= MOVE_LENGTH;
                    }
                    break;
                case STOP:
                    break;
            }
        }


    }

    //创造根据敌方坦克炮筒方向移动的方法
    public void tankMove(Tank.DirectionENUM direction){
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

    // 创建坦克是否越界的方法
    public boolean crossScreen(int nowX,int nowY){
        if (nowX < 10 || nowX > new TankClient().getScreenWidth()-40 || nowY < 30 || nowY > new TankClient().getScreenHeight()-40){
            return false;
        }
        return true;
    }


}
