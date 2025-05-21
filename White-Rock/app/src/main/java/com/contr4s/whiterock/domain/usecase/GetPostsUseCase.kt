package com.contr4s.whiterock.domain.usecase

import com.contr4s.whiterock.data.model.Post
import com.contr4s.whiterock.data.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val repository: PostRepository) {
    operator fun invoke(): Flow<List<Post>> = repository.getAllPosts()
}
