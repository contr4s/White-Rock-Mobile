package com.contr4s.whiterock.data.repository

import app.cash.turbine.test
import com.contr4s.whiterock.data.model.SampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class PostRepositoryImplTest {
    
    private lateinit var repository: PostRepositoryImpl
    
    @Before
    fun setup() {
        repository = PostRepositoryImpl()
    }
    
    @Test
    fun `getAllPosts emits posts from SampleData`() = runTest {
        repository.getAllPosts().test {
            val emittedPosts = awaitItem()
            assert(emittedPosts == SampleData.posts)
            assert(emittedPosts.isNotEmpty())
            awaitComplete()
        }
    }
}
