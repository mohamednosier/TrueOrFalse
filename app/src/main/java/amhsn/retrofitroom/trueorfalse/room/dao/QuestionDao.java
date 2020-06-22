package amhsn.retrofitroom.trueorfalse.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import amhsn.retrofitroom.trueorfalse.room.entity.Question;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM QuestionTable")
    LiveData<List<Question>> getAllQuestions();

    @Query("SELECT * FROM QuestionTable WHERE id = :id ")
    LiveData<Question> getQuestionById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Question> questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question questions);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Question questions);

    @Delete
    void delete(Question questions);

    @Query("DELETE FROM QuestionTable")
    void deleteAll();
}
