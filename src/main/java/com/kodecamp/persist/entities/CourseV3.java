package com.kodecamp.persist.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


@Entity
@Table(
  name = "COURSE"
)
public class CourseV3 {

  @Id
  @JoinColumn(name="CODE")
  private String code;

  @Column(name="NAME")
  private String name;
  
  @Column(name="DURATION")
  private Integer duration;
  
  @Column(name="SHORT_CODE")
  private String shortCode;

//  @OneToMany(mappedBy = "course")
//  @JoinColumn(name = "fk_course")
//  private List<Student> students;

  
  public CourseV3(final String code, final String name, final Integer duration) {
    this.code = code;
    this.name = name;
    this.duration = duration;
  }
  
  public CourseV3() {
    super();
  }

  /**
   * <p>
   * This constructor creates a new copy of the object which is not a persistent entity
   * </p>
   * @param originalObj 
   */
  public CourseV3(final CourseV3 originalObj) {
      this.code = originalObj.code;
      this.name = originalObj.name;
      this.duration = originalObj.duration;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public String getShortCode() {
    return shortCode;
  }

  public void setShortCode(String shortCode) {
    this.shortCode = shortCode;
  }
  
  
}
