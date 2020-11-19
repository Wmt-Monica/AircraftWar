package Edition0.Edition0_4;

import javax.swing.*;
import java.awt.*;

/**
 *  版本0.4
 *  功能：
 *      使用双缓冲消除闪烁现象
 *  原因：
 *      刷新重画频率太快，paint()方法还没完成
 *      背景也未即使刷新，出现多个坦克逐个显示
 *  解决方法：
 *      将所有东西画在虚拟图片上，一次性实现出来
 */
public class doubleBufferingPaint extends JFrame {  //继承Frame类

    int x = 50, y = 50;  //让坦克的位置x,y设置为变量
    Image backImg = null;  //用于update()方法中实现双缓冲效果的背景图片


    //重写paint()方法，将实心圆坦克画出来
    @Override
    public void paint(Graphics g) {

        Color foregroundColor = g.getColor();  //获取窗口前景色
        g.setColor(Color.PINK);  //设置窗口背景色为黑色

        //使用fillOval()方法画出实心圆坦克
        g.fillOval(x,y,30,30);

        //不要改变前景色，最后将之前获取到的颜色设置回去
        g.setColor(foregroundColor);

        x += 5;
    }

    //重写update()方法，窗口在重画时，先加载的时update()方法，我们可以在
    //update()方法中提前画好一张图片，然后再显示出来，这样就实现了双缓冲
    @Override
    public void update(Graphics g) {
        //假如图片为null,就创建一个图片大小设置为800px,600px和游戏窗口大小一致
        if (backImg == null){
            backImg = this.createImage(800,600);
        }

        Graphics gBackImg = backImg.getGraphics();  //因为我们要获得backImg的画笔来对图片进行绘画

        //解决游戏窗口背景刷新更新不上
        Color tankColor = gBackImg.getColor();
        gBackImg.setColor(Color.BLACK);
        gBackImg.fillRect(0,0,800,600);
        gBackImg.setColor(tankColor);

        paint(gBackImg);  //使用涂片的画笔将游戏窗口的样式提前绘画好再图片上
        g.drawImage(backImg,0,0,null);  //将整个提前画好的图片绘画在窗口里
    }

    //创建一个游戏窗口对象方法
    public void windowFrame(){
        this.setBounds(400,200,800,600);  //使用setBounds()方法直接设置窗口坐标位置和窗口大小
        this.setResizable(false);  //设置游戏窗口不可随意改变窗口大小
        this.setTitle("AircraftWar");  //设置窗口标题
        this.setVisible(true);  //设置窗口可见
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  //设置窗口叉掉就停止程序
        this.setBackground(Color.BLACK);
        new Thread(new doubleBufferingPaint.rePaintThread()).start();
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
        doubleBufferingPaint window = new doubleBufferingPaint();
        window.windowFrame();
    }

}

