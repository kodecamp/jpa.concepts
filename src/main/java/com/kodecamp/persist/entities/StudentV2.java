package com.kodecamp.persist.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table( name = "STUDENT")
public class StudentV2 implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY) // AUTO-INCREMENT UID COLUMN
  private Long uid;

//  @ManyToOne()
  @JoinColumn(name = "fk_course")
  private CourseV2 course;
  private String name;
  private String address;
  private String rollNo;
  private String collegeCode;

  public StudentV2() {
//    System.out.println("Entity Created : Student");
  }
//  private String courseCode;

  // constructors
  public StudentV2(final Long uid,
    final String name,
    final String address,
    final String rollNo,
    final String collegeCode) {
    this.uid = uid;
    this.name = name;
    this.address = address;
    this.rollNo = rollNo;
    this.collegeCode = collegeCode;
//    this.courseCode = courseCode;
  }

  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public CourseV2 getCourse() {
    return course;
  }

  public void setCourse(CourseV2 course) {
    this.course = course;
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

}
