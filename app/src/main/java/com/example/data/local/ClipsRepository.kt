package com.example.data.local

import kotlinx.coroutines.flow.Flow

class ClipsRepository(
    private val dao: SavedClipDao,
    private val prefsRepo: KeyboardPreferencesRepository,
) {
    val allClips: Flow<List<SavedClipEntity>> = dao.observeAllClips()

    suspend fun seedDefaultsIfNeeded() {
        if (prefsRepo.hasSeededClips()) return
        if (dao.getClipCount() == 0) {
            val now = System.currentTimeMillis()
            val starters = listOf(
                SavedClipEntity(
                    content = "꧁༒☬ 𝕹𝖊𝖔𝖓 𝕲𝖆𝖒𝖊𝖗 ☬༒꧂ 🔥⚡",
                    category = "Name",
                    isPinned = true,
                    createdAt = now,
                ),
                SavedClipEntity(
                    content = "💔🥀 ভালোবাসার মানুষটা আজ অন্যের গল্পে ব্যস্ত 🥀💔",
                    category = "Caption",
                    isPinned = false,
                    createdAt = now - 1_000,
                ),
                SavedClipEntity(
                    content = "#reels #viral #trending #capcut #aesthetic #status #bangla #explore",
                    category = "Hashtags",
                    isPinned = true,
                    createdAt = now - 2_000,
                ),
                SavedClipEntity(
                    content = "─── ⋆⋅☆⋅⋆ ───\n✨ 𝓝𝓮𝔀 𝓡𝓮𝓮𝓵 𝓞𝓾𝓽 𝓝𝓸𝔀 ✨\n─── ⋆⋅☆⋅⋆ ───",
                    category = "Caption",
                    isPinned = false,
                    createdAt = now - 3_000,
                ),
            )
            dao.insertAll(starters)
        }
        prefsRepo.markClipsSeeded()
    }

    suspend fun saveClip(content: String, category: String, isPinned: Boolean = false) {
        val trimmed = content.trim()
        if (trimmed.isEmpty()) return
        dao.insertClip(
            SavedClipEntity(
                content = trimmed,
                category = category,
                isPinned = isPinned,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun togglePinned(clip: SavedClipEntity) {
        dao.updatePinned(clip.id, !clip.isPinned)
    }

    suspend fun deleteClip(id: Int) {
        dao.deleteClipById(id)
    }
}
