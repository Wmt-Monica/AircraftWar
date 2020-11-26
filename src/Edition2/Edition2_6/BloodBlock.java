package Edition2.Edition2_6;

import java.awt.*;

/**
 * 版本2.6
 * 功能：
 *      当主战坦克死亡后，按下2开始
 */
public class BloodBlock {
    int x,y;  // 血块的x,y坐标位置
    final int BLOCK_BLOCK_SIZE = 10;  // 血块正方形的边长大小
    boolean live = true;  // 血块默认事存活状态
    int[][] trail = {  // 血块移动轨迹的各个位置坐标
            {400,350},{410,350},{420,350},{430,350},{440,350},{450,350},{460,350},{470,350},{480,350},{490,350},{500,350},
            {500,360},{500,370},{500,380},{500,390},{500,400},{500,410},{500,420},{500,430},{500,440},{500,450},
            {510,450},{520,450},{530,450},{540,450},{550,450},{560,450},{570,450},{580,450},{590,450},{600,450},
            {600,460},{600,470},{600,480},{600,490},{600,500},{600,510},{600,520},{600,530},{600,540},{600,550},
            {590,550},{580,550},{570,550},{560,550},{550,550},{540,550},{530,550},{520,550},{510,550},{500,550},
            {500,540},{500,530},{500,520},{500,510},{500,500},{500,490},{500,480},{500,470},{500,460},{500,450},
            {490,450},{480,450},{470,450},{460,450},{450,450},{440,450},{430,450},{420,450},{410,450},{400,450},
            {400,440},{400,430},{400,420},{400,410},{400,400},{400,390},{400,380},{400,370},{400,360},{400,350}
    };
    int step = 0;  // 设置血块从位置轨迹的第一步开始移动
    TankClient tankClient;
    Tank tank;

    public BloodBlock(TankClient tankClient) {
        // 设置血块默认的位置
        x = trail[0][0];
        y = trail[0][1];
        this.tankClient = tankClient;
        this.tank = this.tankClient.getTank();
    }

    // 血块的绘画方法
    public void draw (Graphics g){
        if (live){
            Color startColor = g.getColor();
            g.setColor(Color.MAGENTA);
            g.fillRect(x,y,BLOCK_BLOCK_SIZE,BLOCK_BLOCK_SIZE);
            g.setColor(startColor);
            move();
        }
    }

    // 血块的移动方法
    public void move(){
        if (step < trail.length){
            x = trail[step][0];
            y = trail[step][1];
            step ++;
        }else {
            step = 0;  // 当移动到重点轨迹时候就回到起点位置
        }
        new Thread(new eat()).start();
    }

    // 创建血块是否被主战坦克吃掉的方法
    class eat extends Thread{
        @Override
        public void run() {
            eat();
        }
        public void eat(){
            if (x+BLOCK_BLOCK_SIZE >= tank.getX() && x <= tank.getX()+tank.getTANK_SIZE()
                    && y+BLOCK_BLOCK_SIZE >= tank.getY() && y <= tank.getY()+tank.getTANK_SIZE() && live){
                tank.addLife();  // 坦克吃到血块就使用addLife()方法，增加血量
                live = false;  // 血块被坦克吃掉已经死亡

                try {
                    Thread.sleep(5000);  // 当血块被吃掉后就过5秒之后才会有新的血块出现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                live = true;  // 当5秒之后，将血块设置为存活状态
            }
        }
    }

}
