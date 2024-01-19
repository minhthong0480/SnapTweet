package rmit.ad.snaptweet.Model;

public class User {


    private String id;
    private String userId;
    private String username;
    private String fullname;
    private String imageurl;
    private String bio;

    public User(String id, String userId, String username, String fullname, String imageurl, String bio) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
