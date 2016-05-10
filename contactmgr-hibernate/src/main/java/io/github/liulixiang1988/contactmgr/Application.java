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
        int id = save(contact);

        //显示列表数据
//        for(Contact c : fetchAllContact()) {
//            System.out.println(c);
//        }
        fetchAllContact().stream().forEach(System.out::println);

        Contact c = findContactById(id);

        c.setEmail("xjtuliulixiang@qq.com");

        update(c);

        fetchAllContact().stream().forEach(System.out::println);

    }

    private static Contact findContactById(int id) {
        //打开会话
        Session session = sessionFactory.openSession();
        //获取对象或者null
        Contact contact = session.get(Contact.class, id);
        //关闭会话
        session.close();
        //返回对象
        return contact;
    }

    private static void update(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(contact);
        session.getTransaction().commit();
        session.close();
    }

    private static void delete(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(contact);
        session.getTransaction().commit();
        session.close();
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


    private static int save(Contact contact) {
        //打开session
        Session session = sessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //使用session来保存对象
        int id = (int)session.save(contact);

        //提交session
        session.getTransaction().commit();

        //关闭session
        session.close();

        return id;
    }
}
