package Edition2.Edition2_9.Tank;

/**
 * 模式：单例模式
 *
 * 功能：用于Tank包下所有需要用到 tankClient.properties 配置文件的类的创建辅助类 Properties 辅助类唯一实例对象
 *
 * 特点：外部类不能 new (创建) 改类的实例对象
 */

import java.io.IOException;
import java.util.Properties;

public class TankClientProperties {

    // 创建一个获取配置文件信息的辅助类(Properties)实例对象
    static Properties properties = new Properties();

    static {
        try {
            properties.load(TankClientProperties.class.getClassLoader().getResourceAsStream("Edition2/Edition2_8/config/tankClient.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TankClientProperties(){}  // 将改类的构造方法私有化，外部的其他类不能创建(new)改类的实例对象

  // 创建getProperties方法传入要获取的常量的字符串名称，返回配置文件中的所对应的数据
    public static String getProperties(String key){
        return properties.getProperty(key);
    }
}
