package com.kodecamp.persist;

import com.kodecamp.persist.entities.*;
import java.util.*;
import javax.persistence.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@Disabled
public class UniDirectionalManyToOneTest {

  private static final String PERSISTENCE_UNIT_NAME = "CollegeApp";
  private static EntityManagerFactory factory;
  private static EntityManager em;

  @BeforeAll
  public static void setUp() {
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    em = factory.createEntityManager();
//    createCourses();
//    createStudents();

  }

  private static void createCourses() {
    System.out.println("------------------ START : CREATING COURSES -----------------------");
    // read the existing entries and write to console
    em.getTransaction().begin();
    Collection<CourseV1> courses = courses();
    courses
      .stream()
      .forEach((courseObj) -> em.persist(courseObj)); // 4 insert queries

    courses.forEach(System.out::println);

    em.getTransaction().commit();
    System.out.println("------------------ END : CREATING COURSES -----------------------");
  }

  private static Collection<CourseV1> courses() {
    return Arrays.asList(
      new CourseV1("CSC001", "Computer Science", 4),
      new CourseV1("ME001", "Mechanical Engineering", 4),
      new CourseV1("BCA001", "Bachelor Of Computer Application", 4),
      new CourseV1("MCA001", "Master Of Computer Application", 4),
      new CourseV1("EE001", "Electrical Engineering", 4),
      new CourseV1("EC001", "Electronics Engineering", 4),
      new CourseV1("DEC001", "Diploma in Electronics Engineering", 4)
    );

  }

  private static void createStudents() {
    System.out.println("------------------ START : CREATING STUDENT -----------------------");
    // read the existing entries and write to console
    em.getTransaction().begin();
    Collection<StudentV1> newStudents = studentsWithNullIds();
    newStudents.forEach((studentObj) -> em.persist(studentObj));

    em.getTransaction().commit();

    // makes all entities detached : Changes are not managed and monitored
    em.clear();
    em.getTransaction().begin();
    System.out.println("PRINTING STUDENTS ---------");
    newStudents.forEach((student) -> {
      student.setName("Updated Name"); // state modification
      System.out.println(student.getName());
    }
    );

    em.getTransaction().commit();
//    em.getTransaction().commit();
    System.out.println("------------------ END : CREATING STUDENT -----------------------");

    // case 1 : If entity is present in database
    // case 2 : If entity is not already database
  }

  private static Collection<StudentV1> studentsWithNullIds() {

    CourseV1 mca = em.find(CourseV1.class, "MCA001");
    CourseV1 bca = em.find(CourseV1.class, "BCA001");
    CourseV1 csc = em.find(CourseV1.class, "CSC001");

    StudentV1 stu1 = new StudentV1(null, "Name 1", "Address 1", "ROLLNO 1", "College Code 1",mca);

    StudentV1 stu2 = new StudentV1(null, "Name 2", "Address 2", "ROLLNO 2", "College Code 2",mca);

    StudentV1 stu3 = new StudentV1(null, "Name 3", "Address 3", "ROLLNO 3", "College Code 3",bca);

    StudentV1 stu4 = new StudentV1(null, "Name 4", "Address 4", "ROLLNO 4", "College Code 4",bca);

    StudentV1 stu5 = new StudentV1(null, "Name 5", "Address 5", "ROLLNO 5", "College Code 5",csc);
    return Arrays.asList(stu1, stu2, stu3, stu4, stu5);

  }

  @Disabled
  @DisplayName("Get Student With Course : When Entity is present")
  @Test
  public void testGetStudent_JoinFetch() {
    System.out.println("------------------ START : GETTING STUDENT(JOIN FETCH) -----------------------");
    em.getTransaction().begin();

    // new instance BUT represents the existing record (has valid id )
//    Student stuWithCourse = em.getReference(Student.class, Long.valueOf(5));
// Note : Always use CLASS NAMES AND CLASS FIELDS IN JPA QUERY : NEVER USE DB TABLE NAMES AND FIELD
    TypedQuery<StudentV1> query = em.createQuery("SELECT s FROM Student s JOIN FETCH s.course WHERE s.uid = :uid", StudentV1.class);
    query.setParameter("uid", Long.valueOf(4));
    StudentV1 stuWithCourse = query.getSingleResult();
    System.err.println("Name   : " + stuWithCourse.getName());
    System.err.println("Course : " + stuWithCourse.getCourse().getName());
    em.getTransaction().commit();
    System.out.println("------------------ END : GETTING STUDENT -----------------------");
  }
  

  @Disabled
  @DisplayName("Get Student With Course : When Entity is present")
  @Test
  public void testGetStudent_LazyFetch() {
    System.out.println("------------------ START : GETTING STUDENT(LAZY FETCH) -----------------------");
    em.getTransaction().begin();

    // new instance BUT represents the existing record (has valid id )
    StudentV1 stuWithCourse = em.getReference(StudentV1.class, Long.valueOf(5));
// Note : Always use CLASS NAMES AND CLASS FIELDS IN JPA QUERY : NEVER USE DB TABLE NAMES AND FIELD    
    System.err.println("Name   : " + stuWithCourse.getName());
    System.err.println("Course : " + stuWithCourse.getCourse().getName());
    em.getTransaction().commit();
    System.out.println("------------------ END : GETTING STUDENT -----------------------");
  }
  
  @Disabled
  @DisplayName("Get Student With Course : When Entity is present")
  @Test
  public void testGetStudent_EagerFetch() {
    System.out.println("------------------ START : GETTING STUDENT(EAGER FETCH) -----------------------");
    em.getTransaction().begin();

    // new instance BUT represents the existing record (has valid id )
    StudentV1 stuWithCourse = em.getReference(StudentV1.class, Long.valueOf(5));
    System.err.println("Name   : " + stuWithCourse.getName());
    System.err.println("Course : " + stuWithCourse.getCourse().getName());
    
    // executes update on Course ( Since Default Cascade is ALL)
    stuWithCourse.getCourse().setName("New Computer Science");
    em.getTransaction().commit();
    System.out.println("------------------ END : GETTING STUDENT -----------------------");
  }



  @DisplayName("Get Student With Course : When Entity is present")
  @Test
  public void testFetchAllStudent_JoinFetch() {
    System.out.println("------------------ START : FETCHING ALL STUDENTS ( Join Fetch )-----------------------");
    em.getTransaction().begin();

    // Note : Multiple queries are generated
    // for all students : select * from students
    // UN-COMMENT THIS TO SEE THE EFFECT
//    List<Student> students = em.createQuery("from Student s", Student.class).getResultList();

    // for every student : select * from course where code = student[index].course_code
    List<StudentV1> students = em.createQuery("select student from Student as student JOIN FETCH student.course", StudentV1.class).getResultList();
//    System.out.println("NO OF STUDENTS : " + students.size());
    students.forEach((student) -> {
      System.out.println("Name : " + student.getName());
      System.out.println("Course : " + student.getCourse().getName());
    });
//    System.out.println("#########");
    em.getTransaction().commit();
    System.out.println("------------------ END : FETCHING ALL STUDENTS -----------------------");
  }
  
  
  @Disabled
  @DisplayName("Get Student With Course : When Entity is present")
  @Test
  public void testFetchAllStudent_EagerFetch() {
    System.out.println("------------------ START : FETCHING ALL STUDENTS ( Eager Fetch )-----------------------");
    em.getTransaction().begin();

    // Note : Multiple queries are generated
    // for all students : select * from students
    // UN-COMMENT THIS TO SEE THE EFFECT
//    List<Student> students = em.createQuery("from Student s", Student.class).getResultList();

    // for every student : select * from course where code = student[index].course_code
    List<StudentV1> students = em.createQuery("select student from Student as student", StudentV1.class).getResultList();
//    System.out.println("NO OF STUDENTS : " + students.size());
    students.forEach((student) -> {
      System.out.println("Name : " + student.getName());
      System.out.println("Course : " + student.getCourse().getName());
    });
//    System.out.println("#########");
    em.getTransaction().commit();
    System.out.println("------------------ END : FETCHING ALL STUDENTS -----------------------");
  }

  @AfterAll
  public static void tearDown() {
    em.close();
    factory.close();
  }

}
