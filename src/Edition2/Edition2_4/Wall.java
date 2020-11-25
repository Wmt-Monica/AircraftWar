package Edition2.Edition2_4;

import java.awt.*;
/**
 * 版本2.4
 * 功能：
 *   添加主战坦克的生命血条
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
