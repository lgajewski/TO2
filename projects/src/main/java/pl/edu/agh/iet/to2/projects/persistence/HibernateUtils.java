package pl.edu.agh.iet.to2.projects.persistence;

import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pl.edu.agh.iet.to2.teams.ITeam;
import pl.edu.agh.iet.to2.projects.model.Project;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration();
        configuration.configure(new File("projects/hibernate.cfg.xml"));

        configuration.addClass(Project.class);
        configuration.addClass(ITeam.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}