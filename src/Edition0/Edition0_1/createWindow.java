package Edition0.Edition0_1;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 版本0.1
 * 1.创建一个固定大小的窗口
 * 2.使窗口位置再电脑显示屏中居中
 * 3.设置窗口标题
 * 4.设置窗口叉掉的动作监听，使之程序停止
 */
public class createWindow extends Frame {  //继承Frame类

    public void windowFrame(){  //创建一个游戏窗口对象方法
        this.setLocation(400,120);  //设置游戏窗口出现的位置
        this.setSize(800,600);  //设置游戏窗口的大小
        this.setResizable(false);  //设置游戏窗口不可随意改变窗口大小
        this.setTitle("AircraftWar");  //设置窗口标题
        this.addWindowListener(new WindowAdapter() {  //设置窗口监听事件
            @Override
            public void windowClosed(WindowEvent e) {  //当窗口关闭时就将程序停止
                System.exit(0);
            }
        });
        setVisible(true);  //设置窗口可见
    }

    public static void main(String[] args) {
       createWindow window = new createWindow();
       window.windowFrame();
    }
}
