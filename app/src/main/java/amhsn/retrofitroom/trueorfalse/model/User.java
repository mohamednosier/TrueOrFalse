package amhsn.retrofitroom.trueorfalse.model;

import java.util.Comparator;

public class User implements Comparable {

    public String status, email, name,
            current_Coins,
            profile_Pic,
            win_Coins,
            fcm_id,
            user_id,
            opponentName,
            opponentProfile,
            resut;
    public boolean online_status;
    public int num_of_wins,
            num_of_loss;

    public User() {
    }


    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentProfile() {
        return opponentProfile;
    }

    public void setOpponentProfile(String opponentProfile) {
        this.opponentProfile = opponentProfile;
    }

    public String getResut() {
        return resut;
    }

    public void setResut(String resut) {
        this.resut = resut;
    }

    public int getNum_of_wins() {
        return num_of_wins;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }

    //    public User(String email, String name, String profile_Pic, String num_of_wins, String num_of_loss, String fcm_id, String user_id, boolean online_status) {
//        this.email = email;
//        this.name = name;
//        this.profile_Pic = profile_Pic;
//        this.num_of_wins = num_of_wins;
//        this.num_of_loss = num_of_loss;
//        this.fcm_id = fcm_id;
//        this.user_id = user_id;
//        this.online_status = online_status;
//    }

    public User(String first_name,
                String email,
                int num_of_wins,
                boolean online_status,
                String profile_Pic,
                int num_of_loss,
                String fcm_id,
                String user_id) {
        this.name = first_name;
        this.email = email;
        this.num_of_wins = num_of_wins;
        this.online_status = online_status;
        this.profile_Pic = profile_Pic;
        this.num_of_loss = num_of_loss;
        this.fcm_id = fcm_id;
        this.user_id = user_id;
    }

    public User(String name, int num_of_wins) {
        this.name = name;
        this.num_of_wins = num_of_wins;
    }

    public User(String name, String user_id) {
        this.name = name;
        this.user_id = user_id;
    }




    @Override
    public int compareTo(Object o) {
        int numOfWins = ((User) o).getNum_of_wins();
        /* For Ascending order*/
        return (int) (this.num_of_wins - numOfWins);
    }
}
