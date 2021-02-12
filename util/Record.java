package util;

public class Record {
    public String patient;
    public String doctor;
    public String nurse;
    public String comment;

    public Record(String patient, String doctor, String nurse, String comment){
        this.patient = patient;
        this.doctor = doctor;
        this.nurse = nurse;
        this.comment = comment;
    }
}
