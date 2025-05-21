package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPosts(): Flow<List<Post>>
}
