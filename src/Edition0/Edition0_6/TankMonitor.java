package Edition0.Edition0_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 版本0.6
 * 功能：
 *      根据用户的键盘输入来对坦克的位置进行控制
 *          1.在窗口类中添加监听类
 *          2.在整个游戏界面类中添加键盘监听类
 * 注意：根据用户键盘的输入信息的内容使用switch/case来进行相应的坦克移动最后要添加break语句
 */
public class TankMonitor extends JFrame {  //继承Frame类

    //游戏界面的一些常量
    private static int SCREEN_WIDTH = 800;  //游戏窗口的宽度
    private static int SCREEN_HEIGHT = 600;  //游戏窗口的高度
    private static int SCREEN_X = 400;  //游戏窗口的位置的X坐标
    private static int SCREEN_Y = 200;  //游戏窗口的位置的Y坐标
    private static int TANK_SIZE = 30;  //实心圆坦克的直径长

    int x = 50, y = 50;  //让坦克的位置x,y设置为变量
    Image backImg = null;  //用于update()方法中实现双缓冲效果的背景图片


    //重写paint()方法，将实心圆坦克画出来
    @Override
    public void paint(Graphics g) {

        Color foregroundColor = g.getColor();  //获取窗口前景色
        g.setColor(Color.PINK);  //设置窗口背景色为黑色

        //使用fillOval()方法画出实心圆坦克
        g.fillOval(x,y,TANK_SIZE,TANK_SIZE);

        //不要改变前景色，最后将之前获取到的颜色设置回去
        g.setColor(foregroundColor);
    }

    //重写update()方法，窗口在重画时，先加载的时update()方法，我们可以在
    //update()方法中提前画好一张图片，然后再显示出来，这样就实现了双缓冲
    @Override
    public void update(Graphics g) {
        //假如图片为null,就创建一个图片大小设置为800px,600px和游戏窗口大小一致
        if (backImg == null){
            backImg = this.createImage(SCREEN_WIDTH,SCREEN_HEIGHT);
        }

        Graphics gBackImg = backImg.getGraphics();  //因为我们要获得backImg的画笔来对图片进行绘画

        //解决游戏窗口背景刷新更新不上
        Color tankColor = gBackImg.getColor();
        gBackImg.setColor(Color.BLACK);
        gBackImg.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        gBackImg.setColor(tankColor);

        paint(gBackImg);  //使用涂片的画笔将游戏窗口的样式提前绘画好再图片上
        g.drawImage(backImg,0,0,null);  //将整个提前画好的图片绘画在窗口里
    }

    //创建一个游戏窗口对象方法
    public void windowFrame(){
        this.setBounds(SCREEN_X,SCREEN_Y,SCREEN_WIDTH,SCREEN_HEIGHT);  //使用setBounds()方法直接设置窗口坐标位置和窗口大小
        this.setResizable(false);  //设置游戏窗口不可随意改变窗口大小
        this.setTitle("AircraftWar");  //设置窗口标题
        this.setVisible(true);  //设置窗口可见
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  //设置窗口叉掉就停止程序
        this.setBackground(Color.BLACK);
        this.addKeyListener(new KeyListener());
        new Thread(new TankMonitor.rePaintThread()).start();
    }

    //创建一个线程类，使用线程不断重画
    class rePaintThread implements Runnable{
        @Override
        public void run() {
            while (true){
                repaint();  //使用repaint()方法来实现paint()方法的重画
                update(getGraphics());  //使用update使用双缓冲不断地刷新游戏窗口

                //为了更好的看到效果，让线程每重画一次就休息500ms
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建一个键盘监听类
     * 注意：这里使用继承KeyAdapter类而不是实现KeyListener的优点
     * 继承KeyAdapter已经帮你实现了空的keyTyped(),keyPressed()和keyReleased()方法，我们需要使用那个方法自主重写即可
     * 但是实现keyListener接口，我们即使不需要实现某些方法，仍要将空方法写出来
     */
    class KeyListener extends KeyAdapter{

        //键盘信息三种方法的区别
        //keydown：当用户按下键盘上的任意键时触发，如果按住不放的话，会重复触发此事件；
        //keypress：当用户按下键盘上的字符键时触发，如果按住不让的话，会重复触发此事件；
        //keyup：当用户释放键盘上的字符键时触发。

        //重写keyPressed()方法
        @Override
        public void keyPressed(KeyEvent e) {

            //getKeyCode():键盘上每一个按钮都有对应码(Code),可用来查知用户按了什么键，返回当前按钮的数值
            int TankDirection = e.getKeyCode();  //获取坦克前进的方向
            switch (TankDirection){
                case KeyEvent.VK_LEFT : //按下向左键盘
                    x -= 5;
                    break;
                case KeyEvent.VK_RIGHT :
                    x += 5;
                    break;
                case KeyEvent.VK_UP :
                    y -= 5;
                    break;
                case KeyEvent.VK_DOWN :
                    y += 5;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        TankMonitor window = new TankMonitor();
        window.windowFrame();
    }

}