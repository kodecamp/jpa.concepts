
**************************************************************
*Note : run from command line                                *    
*1. mvn install                                              *   
*2. mvn dependency:sources                                   *
**************************************************************


METHODS :

persist(object) -> INSERT STATEMENT -> id is updated in the object ( if id is auto generated { identity ( auto increment column ), sequence generator etc)
Note : Only one insert statement is executed

merge(object) -> first select is executed and record is fetched then dicided whether
a. insert is required
b. update is required

Note : Changes (updates or save) are only persisted at the time of transaction commit;


================================================================ COMMON RELATIONNSHIP RELATED QUERIES ==========
Relationship Mapping

Many students belong to one course

SELECT * FROM STUDENT; // HAS FOREIGN KEY ON COURSE -> OWNER OF THE RELATIONSHIP
SELECT * FROM COURSE;

CASE 1;
-- FETCH ALL STUDENTS WITH THEIR COURSE DETAILS
SELECT * FROM STUDENT INNER JOIN COURSE ON STUDENT.COURSE_CODE = COURSE.CODE;

CASE 2;
-- FETCH ONLY THOSE COURSES WHICH HAS AT LEAST ONE ENROLLED STUDENTS 
SELECT * FROM COURSE JOIN STUDENT ON COURSE.CODE = STUDENT.COURSE_CODE; 

CASE 3;
-- FETCH ALL COURSES WITH THEIR ENROLLED STUDENTS

=============================================================================================

============================================================== UNI-DIRECTIONAL : MANY-TO-ONE ================

UNI-DIRECTIONAL MANY-TO-ONE WITH JOIN_COLUMN('fk_course') { if not given takes automatically as COURSE_CODE }
Note : Relationship is UNI-DIRECTIONAL = There is not way to access STUDENTS FROM COURSE table 
Note : Course table does not contain the collection for STUDENTS ENTITIES.

Queries Generated(DDL)

UNI-DIRECTIONAL MANY-TO-ONE WITH JOIN_COLUMN('fk_course') { if not given takes automatically as COURSE_CODE
Note : Relationship is UNI-DIRECTIONAL = There is not way to access STUDENTS FROM COURSE table 
Note : Course table does not contain the collection for STUDENTS ENTITIES.

CREATE TABLE STUDENT (UID BIGINT IDENTITY NOT NULL, ADDRESS VARCHAR, COLLEGECODE VARCHAR, NAME VARCHAR, ROLLNO VARCHAR, fk_course VARCHAR, PRIMARY KEY (UID))
CREATE TABLE COURSE (CODE VARCHAR NOT NULL, DURATION INTEGER, NAME VARCHAR, PRIMARY KEY (CODE))
ALTER TABLE STUDENT ADD CONSTRAINT FK_STUDENT_fk_course FOREIGN KEY (fk_course) REFERENCES COURSE (CODE)

CODE

// See the query carefully
List<Student> students = em.createQuery("from Student s", Student.class).getResultList();

Note : Multiple queries are generated
    1. for all students : select * from students
    2. for every student : select * from course where code = student[index].course_code ( If already available with entity manager no fetch)

Optimization : Change the query to LEFT OUTER JOIN FETCH MODE
List<Student> students = em.createQuery("select student from Student as student LEFT OUTER JOIN FETCH student.course", Student.class).getResultList();

Note : Only one join query is generated
Note : Fetch Strategy EAGER is used for one-to-one and many-to-one SIDE ( SIDE WHICH CONTAINS THE FOREIGN KEY) IF WEAVING IS 
NOT ENABLED (SINCE LAZY LOADING REQUIRES BYTE CODE CHAGNES)

NOTE : It is better to use JOIN FETCH AT THIS SIDE OF ASSOCIATION OTHER WISE EAGER FETCH IS USED AND SO MANY SELECT QUERIES ARE GENERATED.
NOTE : Also take care of CASCADING. Do not return the original entity if you want to dis-allow the cascading(Use copy constructor for creating new instance and 
return it)

=============================================================================================
Side 1 : Every student can get the details of Course ( It is obvious since the student table has the foreigh key to Course)
studentObj.getCourse();

Side 2 : Every course has a collection of students since many students has the same course
courseObj.getStudents();

Let's add a collection to the CourseV2 class
Collection<StudentV2> students = new ArrayList<>();

Generated DDL
-- STUDENT TABLE IS CREATED WITH FOREIGN KEY
CREATE TABLE STUDENT (UID BIGINT IDENTITY NOT NULL, ADDRESS VARCHAR, COLLEGECODE VARCHAR, NAME VARCHAR, ROLLNO VARCHAR,
 fk_course VARCHAR, PRIMARY KEY (UID))

-- COURSE TABLE IS CREATED
CREATE TABLE COURSE (CODE VARCHAR NOT NULL, DURATION INTEGER, NAME VARCHAR, PRIMARY KEY (CODE))

NOTE** : MAPPING TABLE IS CREATED Since COURSES DOES NOT HAVE ANY IDEA ABOUT THE REFERENCED TABLE COLUMN SO IT MAINTAINS ITS OWN
TABLE TO MANAGE THE RELATIONSHIP (Since foreign key is not required for a relationship to work it is just a constraint on the 
specific column value of the specified table.It is not more than that.


-- MAPPING TABLE IS CREATED TO MANAGE THE RELATIONSHIP FROM COURSE TO STUDENT
-- SINCE IT IS NOT AWARE OF THE FOREIGN KEY COLUMN OF STUDENT TABLE
CREATE TABLE COURSE_STUDENT (CourseV2_CODE VARCHAR NOT NULL, students_UID BIGINT NOT NULL, 
PRIMARY KEY (CourseV2_CODE, students_UID))

Let's tell the eclipse-link to use fk_course column of STUDENT TABLE TO USE AS THE MAPPING COLUMN

@OneToMany()

Collection<StudentV2> students = new ArrayList<>();

Now it does not generate mapping table.
it will now use STUDENT TABLE'S fk_course column value for mapping

CREATE TABLE STUDENT (UID BIGINT IDENTITY NOT NULL, ADDRESS VARCHAR, COLLEGECODE VARCHAR, NAME VARCHAR, ROLLNO VARCHAR, fk_course VARCHAR, fk_coures VARCHAR, PRIMARY KEY (UID))
CREATE TABLE COURSE (CODE VARCHAR NOT NULL, DURATION INTEGER, NAME VARCHAR, PRIMARY KEY (CODE))
ALTER TABLE STUDENT ADD CONSTRAINT FK_STUDENT_fk_coures FOREIGN KEY (fk_coures) REFERENCES COURSE (CODE)

=============================================================================================



-- CREATES TABLE STUDENT WITH 'FOREIGN KEY' COLUMN
CREATE TABLE STUDENT (UID BIGINT IDENTITY NOT NULL, ADDRESS VARCHAR, COLLEGECODE VARCHAR, NAME VARCHAR, ROLLNO VARCHAR, fk_course VARCHAR, PRIMARY KEY (UID))
-- CREATES TABLE COURSE
CREATE TABLE COURSE (CODE VARCHAR NOT NULL, DURATION INTEGER, NAME VARCHAR, PRIMARY KEY (CODE))
-- CREATES TABLE COURSE_STUDENT (MAPPING TABLE) FOR RELATIONSHIP MANAGEMENT
CREATE TABLE COURSE_STUDENT (Course_CODE VARCHAR NOT NULL, students_UID BIGINT NOT NULL, PRIMARY KEY (Course_CODE, students_UID))
-- ALTERS TABLE STUDENT AND DEFINES 'FOREIGN KEY' CONSTRAINT
ALTER TABLE STUDENT ADD CONSTRAINT FK_STUDENT_fk_course FOREIGN KEY (fk_course) REFERENCES COURSE (CODE)
-- ALTERS MAPPING TABLE (ADDS FOREIGN KEY CONSTRAINTS)
ALTER TABLE COURSE_STUDENT ADD CONSTRAINT FK_COURSE_STUDENT_students_UID FOREIGN KEY (students_UID) REFERENCES STUDENT (UID)
ALTER TABLE COURSE_STUDENT ADD CONSTRAINT FK_COURSE_STUDENT_Course_CODE FOREIGN KEY (Course_CODE) REFERENCES COURSE (CODE)
  