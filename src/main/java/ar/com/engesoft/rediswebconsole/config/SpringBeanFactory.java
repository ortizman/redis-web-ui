package ar.com.engesoft.rediswebconsole.config;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
public class SpringBeanFactory implements BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public RedisConnectionFactory registryBeanConnectionFactory(RedisConnectionFactory bean) {
        Objects.requireNonNull(bean);
        GenericBeanDefinition gbd = new GenericBeanDefinition();
        gbd.setBeanClass(bean.getClass());
        final String beanName = bean.getClass().getCanonicalName() + "_" + counter.incrementAndGet();
        this.beanFactory.registerBeanDefinition(beanName, gbd);
        return this.beanFactory.getBean(beanName, RedisConnectionFactory.class);
    }

    public StringRedisTemplate registryBeanRestTemplate(StringRedisTemplate bean, RedisConnectionFactory cf) {
        Objects.requireNonNull(bean);
        GenericBeanDefinition gbd = new GenericBeanDefinition();
        gbd.setBeanClass(bean.getClass());

        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("connectionFactory", cf);
        gbd.setPropertyValues(mpv);

        final String beanName = bean.getClass().getCanonicalName() + "_" + counter.incrementAndGet();
        this.beanFactory.registerBeanDefinition(beanName, gbd);
        return this.beanFactory.getBean(beanName, StringRedisTemplate.class);
    }

    public <T> T getBean(String beanName, Class<T> beanClass) {
        return this.beanFactory.getBean(beanName, beanClass);
    }
}
