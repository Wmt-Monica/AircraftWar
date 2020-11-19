package Edition0.Edition0_9;

import javax.swing.*;
import java.awt.*;

/**
 * 版本1.0
 */
public class TankClient extends JFrame {

    //游戏界面的一些常量
    private static int SCREEN_WIDTH = 800;  //游戏窗口的宽度
    private static int SCREEN_HEIGHT = 600;  //游戏窗口的高度
    private static int SCREEN_X = 400;  //游戏窗口的位置的X坐标
    private static int SCREEN_Y = 200;  //游戏窗口的位置的Y坐标
    private static int TANK_X = 30;  //实心圆坦克的位置坐标X
    private static int TANK_Y = 30;  //实心圆坦克的位置坐标Y


    Tank tank = new Tank(TANK_X,TANK_Y);  //创建一个坦克类对象实例

    Image backImg = null;  //用于update()方法中实现双缓冲效果的背景图片

    //重写paint()方法，将实心圆坦克画出来
    @Override
    public void paint(Graphics g) {

        Color foregroundColor = g.getColor();  //获取窗口前景色
        g.setColor(Color.PINK);  //设置窗口背景色为黑色

        tank.draw(g);  //给坦克画笔自己调用方法画出自己

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
        this.addKeyListener(tank.new KeyListener());  //给游戏界面调用坦克类自己的监听动作方法
        new Thread(new rePaintThread()).start();
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

    public static void main(String[] args) {
        TankClient tankClient = new TankClient();
        tankClient.windowFrame();
    }
}
