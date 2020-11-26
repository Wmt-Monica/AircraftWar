package Edition2.Edition2_5;

import java.awt.*;

/**
 * 版本2.5
 * 功能：
 *   添加"血块",同时主战坦克吃掉血块就增在10单位的生命值，同时血块消失
 *   当血块被吃掉后5秒重生一个血块
 * 步骤：
 *     添加blood类
 *     添加必要的方法
 *     让blood对象固定轨迹运动，并在一定时间后消失
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
