package client;

public class PlayerSetupData {

    private String username;
    private String selectedAvatar;
    private String selectedMap;
    private String finalMap;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSelectedAvatar() {
        return selectedAvatar;
    }

    public void setSelectedAvatar(String selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
    }

    public String getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(String selectedMap) {
        this.selectedMap = selectedMap;
    }

    public String getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(String finalMap) {
        this.finalMap = finalMap;
    }
}