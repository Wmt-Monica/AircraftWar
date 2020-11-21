package Edition1.Edition1_7.Edition1_7_1;

import java.awt.*;

/**
 * 版本1.7.1
 * 功能：
 *      加入爆炸类
 * 步骤：
 *      1.同不同直径的圆模拟爆炸
 *      2.加入live
 *      3.加入位置属性
 *      4.加入draw方法
 */

//爆炸类
public class Explode {
    int x,y;   // 爆炸的x,y坐标位置
    boolean bLive = true;  // 爆炸体是否存活
    int[] ExplodeSize = {2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40};  // 爆炸体实心圆的直径
    int time = 0;  // 爆炸体默认从显示实心圆直径的第一个开始
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

    public boolean getBLive() {
        return bLive;
    }

    //爆炸体绘画自己的方法
    public void draw(Graphics g) {

        while (bLive){  // 如果爆炸体还存活就画出
            // 画出实心圆爆炸类对象
            g.setColor(Color.ORANGE);
            g.fillOval(x + (tank.getX()/2 - ExplodeSize[time]/2),
                       y + (tank.getY()/2 - ExplodeSize[time]/2),
                         ExplodeSize[time],ExplodeSize[time]);
            time = time + 1;

            try {
                Thread.sleep(5);  //爆炸效果的缓冲
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //如果该爆炸体已经全部画完就将其各参数恢复默认，爆炸体已死亡
            if (time == ExplodeSize.length){
                time = 0;
                bLive = false;
                break;
            }
        }

    }
}


