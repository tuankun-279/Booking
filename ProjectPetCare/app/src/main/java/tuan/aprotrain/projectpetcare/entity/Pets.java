package tuan.aprotrain.projectpetcare.entity;

public class Pets {
    private long petId;
    private String petName;
    private String gender;
    private String breed;
    private String birthDate;
    private Float petHeight;
    private Float petWeight;
    private String color;
    private Boolean intact;
    private String notes;
    private long userId;

    public Pets(){}
    public Pets(long petId, String petName, String gender, String breed,
                String birthDate, Float petHeight, Float petWeight,
                String color, Boolean intact, String notes, long userId) {
        this.petId = petId;
        this.petName = petName;
        this.gender = gender;
        this.breed = breed;
        this.birthDate = birthDate;
        this.petHeight = petHeight;
        this.petWeight = petWeight;
        this.color = color;
        this.intact = intact;
        this.notes = notes;
        this.userId = userId;
    }

    public Pets(String petName) {
        this.petName = petName;
    }


    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Float getPetHeight() {
        return petHeight;
    }

    public void setPetHeight(Float petHeight) {
        this.petHeight = petHeight;
    }

    public Float getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(Float petWeight) {
        this.petWeight = petWeight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIntact() {
        return intact;
    }

    public void setIntact(Boolean intact) {
        this.intact = intact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
