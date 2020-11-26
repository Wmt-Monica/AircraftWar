package Edition2.Edition2_9.Tank;

/**
 * 墙面类
 */

import java.awt.*;

public class Wall {
    private int x;

    /**
     *
     * @return 返回墙面对象的 x 坐标
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return 返回墙面对象的 y 坐标
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return 返回墙面对象的宽度 weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     *
     * @return 返回墙面对象的高度 height
     */
    public int getHeight() {
        return height;
    }

    private int y;  // 墙的位置
    private int weight, height;  // 墙的宽高
    private TankClient tankClient;  // 主界面对象

    /**
     * 功能：实例化一个墙对象
     *
     * @param x 墙对象的 x 坐标
     * @param y 墙对象的 y 坐标
     * @param weight 墙对象的宽度 weight
     * @param height 墙对象的高度 height
     * @param tankClient 主窗口对象
     */
    public Wall(int x, int y, int weight, int height, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.height = height;
        this.tankClient = tankClient;
    }

    /**
     * 功能：墙对象画出自己 (实心长方形) 的方法
     *
     * @param g 墙对象画出自己的 Graphics 画笔对象
     */
    public void draw(Graphics g){
        Color startColor = g.getColor();
        g.setColor(Color.WHITE);
        g.fillRect(x,y,weight,height);  // 画出实心长方形的墙
        g.setColor(startColor);
    }
}
