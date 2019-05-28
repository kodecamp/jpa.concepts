package com.kodecamp.persist.entities;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
  name = "COURSE"
)
public class CourseV2 implements Serializable {

  @Id
  private String code;
  private String name;
  private Integer duration;

  @OneToMany(mappedBy = "course", fetch = EAGER, cascade = CascadeType.REFRESH)
  protected List<StudentV2> students;
  
  
  public CourseV2() {
    super();
  }


  public CourseV2(final String code, final String name, final Integer duration) {
    this.code = code;
    this.name = name;
    this.duration = duration;
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


  public List<StudentV2> getStudents() {
    return students;
  }


  public void setStudents(List<StudentV2> students) {
    this.students = students;
  }
  
}
