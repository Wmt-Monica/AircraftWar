package Edition1.Edition1_9;

import java.awt.*;

/**
 * 版本1.9
 * 功能：
 *      设置新创建的敌方坦克的位置进行约束：设置距离主战坦克 50 距离
 */

//爆炸类
public class Explode {
    int x,y;   // 爆炸的x,y坐标位置
    private static boolean bLive = true;  // 爆炸体是否存活
    int[] ExplodeSize = {2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,38,36,34,32,30,28,26,
    24,22,20,18,16,14,12,10,8,6,4,2};  // 爆炸体实心圆的直径
    private int time = 0;  // 爆炸体默认从显示实心圆直径的第一个开始
    Tank tank;  // 爆炸的坦克对象

    // 有参构造器
    public Explode(int x, int y, Tank tank) {
        this.x = x;
        this.y = y;
        this.tank = tank;
    }

    // 无参构造器
    public Explode(){

    }

    //爆炸体绘画自己的方法
    public void draw(Graphics g) {

        while (bLive){  // 如果爆炸体还存活就画出
            Color tankColor = g.getColor();
            // 画出实心圆爆炸类对象
            g.setColor(Color.ORANGE);
            g.fillOval(x, y , ExplodeSize[time],ExplodeSize[time]);
            g.setColor(tankColor);
            time++;
                try {
                    Thread.sleep(5);  //爆炸效果的缓冲
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            //如果该爆炸体已经全部画完就将其各参数恢复默认，爆炸体已死亡
            if (time == ExplodeSize.length-1){
                bLive = false;
            }
        }
        bLive = true;
    }
}


