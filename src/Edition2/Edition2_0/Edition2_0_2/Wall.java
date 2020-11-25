package Edition2.Edition2_0.Edition2_0_2;

import java.awt.*;
/**
 * 版本2.0.2
 * 功能：
 *     添加wall(墙)的类：子弹不能穿过，坦克也不能穿过
 *     同时新坦克的创建不能在墙里面出现
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
        g.fillRect(x,y,weight,height);  // 画出实心长方形的墙
    }
}
