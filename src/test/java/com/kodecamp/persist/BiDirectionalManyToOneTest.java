package com.kodecamp.persist;

import com.kodecamp.persist.entities.*;
import java.util.*;
import javax.persistence.*;
import org.junit.jupiter.api.*;

@Disabled
public class BiDirectionalManyToOneTest {

  private static final String PERSISTENCE_UNIT_NAME = "CollegeApp";
  private static EntityManagerFactory factory;
  private static EntityManager em;

  @BeforeAll
  public static void setUp() {
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    em = factory.createEntityManager();
    createCourses();
    createStudents();
  }

  private static void createCourses() {
    System.out.println("------------------ START : CREATING COURSES -----------------------");
    // read the existing entries and write to console
    em.getTransaction().begin();
    Collection<CourseV2> courses = courses();
    courses
      .stream()
      .forEach((courseObj) -> em.persist(courseObj)); // 4 insert queries

    courses.forEach(System.out::println);

    em.getTransaction().commit();
    System.out.println("------------------ END : CREATING COURSES -----------------------");
  }
//    createCourseV2s();

  private static Collection<CourseV2> courses() {
    return Arrays.asList(
      new CourseV2("CSC001", "Computer Science", 4),
      new CourseV2("ME001", "Mechanical Engineering", 4),
      new CourseV2("BCA001", "Bachelor Of Computer Application", 4),
      new CourseV2("MCA001", "Master Of Computer Application", 4),
      new CourseV2("EE001", "Electrical Engineering", 4),
      new CourseV2("EC001", "Electronics Engineering", 4),
      new CourseV2("DEC001", "Diploma in Electronics Engineering", 4)
    );

  }

  private static void createStudents() {
    System.out.println("------------------ START : CREATING STUDENT -----------------------");
    // read the existing entries and write to console
    em.getTransaction().begin();
    Collection<StudentV2> newStudents = studentsWithNullIds();
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

  private static Collection<StudentV2> studentsWithNullIds() {

    CourseV2 mca = em.find(CourseV2.class, "MCA001");
    CourseV2 bca = em.find(CourseV2.class, "BCA001");
    CourseV2 csc = em.find(CourseV2.class, "CSC001");

    StudentV2 stu1 = new StudentV2(null, "Name 1", "Address 1", "ROLLNO 1", "College Code 1");
    stu1.setCourse(mca);

    StudentV2 stu2 = new StudentV2(null, "Name 2", "Address 2", "ROLLNO 2", "College Code 2");
    stu2.setCourse(mca);

    StudentV2 stu3 = new StudentV2(null, "Name 3", "Address 3", "ROLLNO 3", "College Code 3");
    stu3.setCourse(bca);

    StudentV2 stu4 = new StudentV2(null, "Name 4", "Address 4", "ROLLNO 4", "College Code 4");
    stu4.setCourse(csc);

    StudentV2 stu5 = new StudentV2(null, "Name 5", "Address 5", "ROLLNO 5", "College Code 5");
    stu5.setCourse(csc);
    return Arrays.asList(stu1, stu2, stu3, stu4, stu5);

  }

  @Disabled
  @DisplayName("Get StudentV2 With CourseV2 : When Entity is present")
  @Test
  public void testGetStudent() {
    System.out.println("------------------ START : GETTING STUDENT -----------------------");
    em.getTransaction().begin();

    // new instance BUT represents the existing record (has valid id )
    StudentV2 stuWithCourseV2 = em.getReference(StudentV2.class, Long.valueOf(5));
    System.err.println("@@@@@@ CourseV2 : " + stuWithCourseV2);
    em.getTransaction().commit();
    System.out.println("------------------ END : GETTING STUDENT -----------------------");
  }


  @DisplayName("Get StudentV2 With CourseV2 : When Entity is present")
  @Test
  public void testFetchAllStudent() {
    System.out.println("------------------ START : FETCHING ALL STUDENTS -----------------------");
    em.getTransaction().begin();

    // Note : Multiple queries are generated
    // for all students : select * from students
    // for every student : select * from course where code = student[index].course_code
//    List<StudentV2> students = em.createQuery("from StudentV2 student", StudentV2.class).getResultList();
    List<StudentV2> students = em.createQuery("select student from StudentV2 as student JOIN FETCH student.course", StudentV2.class).getResultList();
//    System.out.println("NO OF STUDENTS : " + students.size());
    students.forEach((student) -> {
      System.out.println("Name : " + student.getName());
      System.out.println("CourseV2 : " + student.getCourse().getName());
    });
//    System.out.println("#########");
    em.getTransaction().commit();
    System.out.println("------------------ END : FETCHING ALL STUDENTS -----------------------");
  }

  @Disabled
  @DisplayName("Get All Courses With Students")
  @Test
  public void testFetchAllCourses() {
    System.out.println("------------------ START : FETCHING ALL COURSES -----------------------");
    em.getTransaction().begin();

    List<CourseV2> courses = em.createQuery("select course from CourseV2 as course", CourseV2.class).getResultList();
//    em.detach(courses);
    for (CourseV2 course : courses) {
      System.out.println("Course Name : " + course.getName());
      List<StudentV2> students = course.getStudents();

      students.forEach((student) -> {
        System.out.println(student.getUid());
        System.out.println(student.getName());
        System.out.println(student.getRollNo());
        System.out.println(student.getCollegeCode());

      });
    }

    em.getTransaction().commit();
    System.out.println("------------------ END : FETCHING ALL COURSES -----------------------");
  }

  @AfterAll
  public static void tearDown() {
    em.close();
    factory.close();
  }

}
