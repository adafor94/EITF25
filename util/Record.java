package util;

public class Record {
    public String patient;
    public String doctor;
    public String nurse;
    public String comment;
    public String division;

    public Record(String patient, String doctor, String division, String nurse, String comment){
        this.patient = patient;
        this.doctor = doctor;
        this.division = division;
        this.nurse = nurse;
        this.comment = comment;
    }

    public String printable() {
        return ("Patient: " + patient + "\n" + "Doctor: " + doctor + "\n" + "Nurse: " + nurse + "\n" + "Division: " + division + "\n" + "Comment: " + comment + "\n");
    }

    public void appendComment(String s) {
        comment = comment + "\n" + s;
    }
}
