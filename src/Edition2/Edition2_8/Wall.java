package Edition2.Edition2_8;

import java.awt.*;

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
public class Wall {
    private int x;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    private int y;  // 墙的位置
    private int weight, height;  // 墙的宽高
    private TankClient tankClient;  // 主界面对象

    public Wall(int x, int y, int weight, int height, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.height = height;
        this.tankClient = tankClient;
    }

    public void draw(Graphics g){
        Color startColor = g.getColor();
        g.setColor(Color.WHITE);
        g.fillRect(x,y,weight,height);  // 画出实心长方形的墙
        g.setColor(startColor);
    }
}
