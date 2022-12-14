package tuan.aprotrain.projectpetcare.entity;

public class User {

    private String userId;
    private String userName;
    private String email;
    private String phoneNo;
    //private String password;
    private String userToken;
    private Boolean adminStatus;
    public static String TABLE_NAME = "Users";
    public User(){}

    public User(String userId, String email){
        this.userId = userId;
        this.email = email;
        //this.password = password;
    }

    public User(String userId, String userName, String email, String phoneNo,
                 String userToken, Boolean adminStatus) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.userToken = userToken;
        this.adminStatus = adminStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Boolean getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Boolean adminStatus) {
        this.adminStatus = adminStatus;
    }
}
