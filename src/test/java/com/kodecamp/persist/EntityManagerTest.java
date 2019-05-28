/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodecamp.persist;

import com.kodecamp.persist.entities.CourseV1;
import com.kodecamp.persist.entities.CourseV3;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kodecamp
 */
public class EntityManagerTest {
  private static final String PERSISTENCE_UNIT_NAME = "CollegeApp";
  private static EntityManagerFactory factory;
  private static EntityManager em;

  @BeforeAll
  public static void setUp() {
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
  }

  @BeforeEach
  public void createEntityManager() {
    em = factory.createEntityManager();
  }

  @Test
  public void persist_getReference_update() {
    System.out.println("--------------------- PERSIST -> GETREFERECE -> UPDATE --------------------");

    em.getTransaction().begin();
    System.out.println("Entity Manager  = " + em);
    CourseV1 course = new CourseV1("Test001", "Test Course 1", 5);
    em.persist(course);

//        em.getTransaction().commit();      
//      em.getTransaction().begin();
    CourseV1 managedCourseEntity = em.getReference(CourseV1.class, "Test001");
    managedCourseEntity.setName("Updated Test Course");
    System.out.println("course == managedCourseEntity : " + (course == managedCourseEntity));
    System.out.println("Managed Entity : " + managedCourseEntity);
    em.getTransaction().commit();

  }

  @Test
  public void persist_jpqlselect_update() {
    System.out.println("--------------------- PERSIST -> JPQL_SELECT -> UPDATE --------------------");
    em.getTransaction().begin();
    System.out.println("Entity Manager  = " + em);
    CourseV1 course = new CourseV1("Test002", "Test Course 2", 5);
    em.persist(course);
    CourseV1 managedCourseEntity = em.createQuery("SELECT c FROM CourseV1 c where c.code=:code", CourseV1.class)
        .setParameter("code", "Test001").getSingleResult();
    managedCourseEntity.setName("Updated Test Course");
    System.out.println("course == managedCourseEntity : " + (course == managedCourseEntity));
    System.out.println("Managed Entity : " + managedCourseEntity);
    em.getTransaction().commit();

  }

  @AfterEach
  public void testCleanUp() {
    em.close();
  }

  @AfterAll
  public static void setUpCleanUp() {
    System.out.println("======================== Setup CleanUp =========================");
    EntityManager em = factory.createEntityManager();
    System.out.println("Entity Manager  = " + em);
    em.getTransaction().begin();
    em.remove(em.getReference(CourseV1.class, "Test001"));
    em.remove(em.getReference(CourseV1.class, "Test002"));
    em.getTransaction().commit();
    em.close();
    factory.close();
  }
}
