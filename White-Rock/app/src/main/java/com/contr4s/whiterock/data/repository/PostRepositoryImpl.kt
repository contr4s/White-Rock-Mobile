package com.contr4s.whiterock.data.repository

import com.contr4s.whiterock.data.model.Post
import com.contr4s.whiterock.data.model.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor() : PostRepository {
    override fun getAllPosts(): Flow<List<Post>> = flow {
        emit(SampleData.posts)
    }
}
