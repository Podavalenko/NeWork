package ru.netology.nework.dao

import androidx.room.Dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.entity.PostEntity


@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("SELECT COUNT(*) FROM PostEntity")
    suspend fun count(): Int

    @Query("DELETE FROM PostEntity")
    suspend fun removeAll()

}

