package amhsn.retrofitroom.trueorfalse.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "QuestionTable")
public class Question {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "isCorrect")
    private int isCorrect;

    @ColumnInfo(name = "description")
    private String description;

    // Room Construction
    public Question(int id, String question, int isCorrect, String description) {
        this.id = id;
        this.question = question;
        this.isCorrect = isCorrect;
        this.description = description;
    }

    // Our Construction
    @Ignore
    public Question(String question, int isCorrect, String description) {
        this.question = question;
        this.isCorrect = isCorrect;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int isCorrect() {
        return isCorrect;
    }

    public void setCorrect(int correct) {
        isCorrect = correct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
