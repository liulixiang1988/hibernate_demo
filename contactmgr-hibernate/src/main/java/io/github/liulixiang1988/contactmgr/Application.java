package io.github.liulixiang1988.contactmgr;

import io.github.liulixiang1988.contactmgr.model.Contact;
import io.github.liulixiang1988.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    //保存一个SessionFactory(我们也只需要一个)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        //创建StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Liu","Lixiang")
                .withEmail("550488300@qq.com")
                .withPhone(7735556666L)
                .build();
        save(contact);

        //显示列表数据
//        for(Contact c : fetchAllContact()) {
//            System.out.println(c);
//        }
        fetchAllContact().stream().forEach(System.out::println);

    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContact() {
        Session session = sessionFactory.openSession();
        //创建Criteria
        Criteria criteria = session.createCriteria(Contact.class);

        //从Criteria中获取数据
        List<Contact> contacts = criteria.list();

        //关闭会话
        session.close();
        return contacts;
    }


    private static void save(Contact contact) {
        //打开session
        Session session = sessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //使用session来保存对象
        session.save(contact);

        //提交session
        session.getTransaction().commit();

        //关闭session
        session.close();
    }
}
