package com.example.a337910542.dormitory.bean;

/**
 * Created by 337910542 on 2017/11/25.
 */

public class StudentInformation {
    private String errCode;
    private String stuId;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    private String stuName;
    private String stuGender;
    private String stuVcode;
    private String stuRoom;
    private String stuBuilding;
    private String stuLocation;
    private String stuGrade;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuGender() {
        return stuGender;
    }

    public void setStuGender(String stuGender) {
        this.stuGender = stuGender;
    }

    public String getStuVcode() {
        return stuVcode;
    }

    public void setStuVcode(String stuVcode) {
        this.stuVcode = stuVcode;
    }

    public String getStuRoom() {
        return stuRoom;
    }

    public void setStuRoom(String stuRoom) {
        this.stuRoom = stuRoom;
    }

    public String getStuBuilding() {
        return stuBuilding;
    }

    public void setStuBuilding(String stuBuilding) {
        this.stuBuilding = stuBuilding;
    }

    public String getStuLocation() {
        return stuLocation;
    }

    public void setStuLocation(String stuLocation) {
        this.stuLocation = stuLocation;
    }

    public String getStuGrade() {
        return stuGrade;
    }

    public void setStuGrade(String stuGrade) {
        this.stuGrade = stuGrade;
    }

    @Override
    public String toString() {
        return "StudentInformation{" +
                "errCode='" + errCode + '\'' +
                ", stuId='" + stuId + '\'' +
                ", stuName='" + stuName + '\'' +
                ", stuGender='" + stuGender + '\'' +
                ", stuVcode='" + stuVcode + '\'' +
                ", stuRoom='" + stuRoom + '\'' +
                ", stuBuilding='" + stuBuilding + '\'' +
                ", stuLocation='" + stuLocation + '\'' +
                ", stuGrade='" + stuGrade + '\'' +
                '}';
    }
}
