package Edition2.Edition2_8;

import java.io.IOException;
import java.util.Properties;

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
public class TankClientProperties {

    // 创建一个获取配置文件信息的辅助类(Properties)实例对象
    static Properties properties = new Properties();

    static {
        try {
            properties.load(TankClientProperties.class.getClassLoader().getResourceAsStream("config/tankClient.properties"));
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
