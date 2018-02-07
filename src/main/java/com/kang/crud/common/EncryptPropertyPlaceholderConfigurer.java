package com.kang.crud.common;

import com.github.pagehelper.util.StringUtil;
import com.kang.crud.utils.AesUtil;
import java.util.Properties;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Spring PropertyPlaceholderConfigurer读取配置文件
 * @Author:kangjunyu
 * @Description:
 * @Date Create in 17:40 2018/2/6
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

  //第二种方式

  protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
    try {
      String key = "AkxfGVoMiKDxUiJM";
      String iv = "1841611841611010";

      String userKey = "jdbc.user";
      String passwordkey = "jdbc.password";

      String user = props.getProperty(userKey);
      String password = props.getProperty(passwordkey);

      if (StringUtil.isNotEmpty(user)) {
        props.setProperty(userKey, AesUtil.decrypt(user, key, iv));//解密user
      }

      if (StringUtil.isNotEmpty(password)) {
        props.setProperty(passwordkey, AesUtil.decrypt(password, key, iv));
      }
      super.processProperties(beanFactory, props);
    } catch (Exception e) {
      throw new BeanInitializationException(e.getMessage());
    }

  }
}