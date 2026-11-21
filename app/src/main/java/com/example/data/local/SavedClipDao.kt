package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedClipDao {
    @Query("SELECT * FROM saved_clips ORDER BY isPinned DESC, createdAt DESC")
    fun observeAllClips(): Flow<List<SavedClipEntity>>

    @Query("SELECT COUNT(*) FROM saved_clips")
    suspend fun getClipCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: SavedClipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clips: List<SavedClipEntity>)

    @Query("UPDATE saved_clips SET isPinned = :isPinned WHERE id = :id")
    suspend fun updatePinned(id: Int, isPinned: Boolean)

    @Query("DELETE FROM saved_clips WHERE id = :id")
    suspend fun deleteClipById(id: Int)
}
