package rmit.ad.rmeet_dating_app.Chat;

public class ChatObject {
    private String message;
    private Boolean currentUser;
    public ChatObject(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String UserID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(){
        this.currentUser= currentUser;
    }
}
