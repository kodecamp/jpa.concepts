package com.kodecamp.persist.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
  name = "STUDENT"
)
public class StudentV1 {

  @Id
  @GeneratedValue(strategy = IDENTITY) // AUTO-INCREMENT UID COLUMN
  @Column(name="UID")
  private Long uid;
  
  @Column(name="NAME")
  private String name;
  
  @Column(name="ADDRESS")
  private String address;
  
  @Column(name="ROLL_NO")
  private String rollNo;
  
  @Column(name="COLLEGE_CODE")
  private String collegeCode;
  
  // NOTE : LAZY IS NOT DEFAULT FOR THIS SIDE OF ASSOCIATION
  @ManyToOne(optional = false)
  @JoinColumn(name = "fk_course_code",referencedColumnName = "code")
  private CourseV1 course;

  public StudentV1() {
//    System.out.println("Entity Created : Student");
  }
//  private String courseCode;

  // constructors
  public StudentV1(final Long uid,
    final String name,
    final String address,
    final String rollNo,
    final String collegeCode,
    final CourseV1 course) {
    this.uid = uid;
    this.name = name;
    this.address = address;
    this.rollNo = rollNo;
    this.collegeCode = collegeCode;
    this.course = course;
  }
  
  public CourseV1 getCourse() {
      return new CourseV1(this.course);
  }

  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getRollNo() {
    return rollNo;
  }

  public void setRollNo(String rollNo) {
    this.rollNo = rollNo;
  }

  public String getCollegeCode() {
    return collegeCode;
  }

  public void setCollegeCode(String collegeCode) {
    this.collegeCode = collegeCode;
  }

  public void setCourse(CourseV1 course) {
    this.course = course;
  }
  
}
