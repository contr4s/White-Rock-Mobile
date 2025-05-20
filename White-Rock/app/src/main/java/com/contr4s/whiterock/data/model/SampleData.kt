package com.contr4s.whiterock.data.model

import java.util.UUID
import java.util.Date

object SampleData {

    private val USER1_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private val USER2_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12")
    private val USER3_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
    
    private val GYM1_ID = UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b11")
    private val GYM2_ID = UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b12")
    
    private val ROUTE1_ID = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c11")
    private val ROUTE2_ID = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c12")
    private val ROUTE3_ID = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c13")
    private val ROUTE4_ID = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c14")
    
    private val POST1_ID = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d11")
    private val POST2_ID = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d12")
    
    private val COMMENT1_ID = UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e11")
    private val COMMENT2_ID = UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e12")
    private val COMMENT3_ID = UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e13")
    private val COMMENT4_ID = UUID.fromString("e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e14")

    val CURRENT_USER_ID = USER1_ID

    private val _users = generateSampleUsers()
    private val _routes = generateSampleRoutes()
    private val _gyms = generateSampleGyms()
    private val _attempts = generateSampleRouteAttempts()
    private val _posts = generateSamplePosts()
    private val _comments = generateSampleComments()

    val users: List<User> get() = _users
    val routes: List<Route> get() = _routes
    val gyms: List<ClimbingGym> get() = _gyms
    val attempts: List<RouteAttempt> get() = _attempts
    val posts: List<Post> get() = _posts
    val comments: List<Comment> get() = _comments

    fun getUserById(id: UUID, users: List<User> = this.users): User? = users.find { it.id == id }
    fun getCurrentUser(users: List<User> = this.users): User = getUserById(CURRENT_USER_ID, users)!!
    
    private fun generateSampleUsers(): List<User> = listOf(
        User(
            id = USER1_ID,
            name = "Алексей Иванов",
            profilePictureUrl = "https://placehold.co/100x100/png?text=Alex",
            city = "Москва",
            friends = listOf(USER2_ID, USER3_ID)
        ),
        User(
            id = USER2_ID,
            name = "Мария Петрова",
            profilePictureUrl = "https://placehold.co/100x100/png?text=Maria",
            city = "Санкт-Петербург",
            friends = listOf(USER1_ID)
        ),
        User(
            id = USER3_ID,
            name = "Иван Сидоров",
            profilePictureUrl = "https://placehold.co/100x100/png?text=Ivan",
            city = "Екатеринбург", 
            friends = listOf(USER1_ID)
        )
    )
    
    private fun generateSampleRoutes(): List<Route> = listOf(
        Route(
            id = ROUTE1_ID,
            name = "Крутой старт",
            gymId = GYM1_ID,
            grade = "6A",
            color = "Красный",
            type = "Боулдеринг",
            imageUrl = "https://placehold.co/300x200/png?text=Route+1",
            description = "Сложный старт с динамическим движением",
            creationDate = Date().time - 7 * 24 * 60 * 60 * 1000,
            setter = "Михаил",
            rating = 4.5f
        ),
        Route(
            id = ROUTE2_ID,
            name = "Техничная проблема",
            gymId = GYM1_ID,
            grade = "7A",
            color = "Синий",
            type = "Боулдеринг",
            imageUrl = "https://placehold.co/300x200/png?text=Route+2",
            description = "Маршрут требует хорошей работы ног",
            creationDate = Date().time - 14 * 24 * 60 * 60 * 1000,
            setter = "Анна",
            rating = 4.8f
        ),
        Route(
            id = ROUTE3_ID,
            name = "Силовой потолок",
            gymId = GYM2_ID,
            grade = "6C",
            color = "Желтый",
            type = "Трудность",
            imageUrl = "https://placehold.co/300x200/png?text=Route+3",
            description = "Маршрут с сильной нагрузкой на предплечья",
            creationDate = Date().time - 3 * 24 * 60 * 60 * 1000,
            setter = "Сергей",
            rating = 4.2f
        ),
        Route(
            id = ROUTE4_ID,
            name = "Баланс",
            gymId = GYM2_ID,
            grade = "5C",
            color = "Зеленый",
            type = "Боулдеринг",
            imageUrl = "https://placehold.co/300x200/png?text=Route+4",
            description = "Маршрут на баланс и равновесие",
            creationDate = Date().time - 20 * 24 * 60 * 60 * 1000,
            setter = "Елена",
            rating = 4.0f
        )
    )
    
    fun getRoutesByGymId(gymId: UUID, routes: List<Route>): List<Route> = routes.filter { it.gymId == gymId }
    
    private fun generateSampleGyms(): List<ClimbingGym> {
        val gym1Routes = _routes.filter { it.gymId == GYM1_ID }
        val gym2Routes = _routes.filter { it.gymId == GYM2_ID }
        
        return listOf(
            ClimbingGym(
                id = GYM1_ID,
                name = "BouldRock",
                city = "Москва",
                address = "ул. Скалолазная, 15",
                description = "Современный боулдеринговый зал с большим разнообразием трасс",
                rating = 4.7,
                routes = gym1Routes,
                photoUrl = "https://placehold.co/600x400/png?text=Gym+1",
                workingHours = "Пн-Пт: 10:00-22:00, Сб-Вс: 10:00-20:00",
                phone = "+7 (999) 123-45-67",
                website = "https://bould-rock.ru",
                priceList = listOf("Разовое посещение - 800 руб.", "Абонемент на месяц - 5000 руб."),
                amenities = listOf("Раздевалка", "Душ", "Кафе", "Прокат снаряжения")
            ),
            ClimbingGym(
                id = GYM2_ID,
                name = "ВертикальМир",
                city = "Санкт-Петербург",
                address = "пр. Скальный, 42",
                description = "Большой скалодром с трассами на любой вкус",
                rating = 4.5,
                routes = gym2Routes,
                photoUrl = "https://placehold.co/600x400/png?text=Gym+2",
                workingHours = "Ежедневно: 09:00-22:00",
                phone = "+7 (999) 765-43-21",
                website = "https://vertical-world.ru",
                priceList = listOf("Разовое посещение - 700 руб.", "Абонемент на месяц - 4500 руб."),
                amenities = listOf("Раздевалка", "Душ", "Магазин снаряжения", "Тренажерный зал")
            )
        )
    }
    
    fun getGymById(id: UUID, gyms: List<ClimbingGym> = this.gyms): ClimbingGym? = gyms.find { it.id == id }
    fun getRouteById(id: UUID, routes: List<Route> = this.routes): Route? = routes.find { it.id == id }
    
    private fun generateSampleRouteAttempts(): List<RouteAttempt> {
        val route1 = _routes.find { it.id == ROUTE1_ID }!!
        val route2 = _routes.find { it.id == ROUTE2_ID }!!
        val route3 = _routes.find { it.id == ROUTE3_ID }!!
        val route4 = _routes.find { it.id == ROUTE4_ID }!!
        
        return listOf(
            RouteAttempt(
                route = route1,
                attempts = 3,
                completed = true
            ),
            RouteAttempt(
                route = route2,
                attempts = 5,
                completed = false
            ),
            RouteAttempt(
                route = route3,
                attempts = 1,
                completed = true
            ),
            RouteAttempt(
                route = route4,
                attempts = 2,
                completed = true
            )
        )
    }
    
    private fun generateSamplePosts(): List<Post> {
        val user1 = _users.find { it.id == USER1_ID }!!
        val user2 = _users.find { it.id == USER2_ID }!!
        val gym1 = _gyms.find { it.id == GYM1_ID }!!
        val gym2 = _gyms.find { it.id == GYM2_ID }!!
        
        return listOf(
            Post(
                id = POST1_ID,
                user = user1,
                gym = gym1,
                timestamp = Date().time - 2 * 24 * 60 * 60 * 1000,
                routesCompleted = listOf(_attempts[0], _attempts[1]),
                photoUrl = "https://placehold.co/400x300/png?text=Post+1",
                comment = "Отличная тренировка! Наконец-то прошел 'Крутой старт'!"
            ),
            Post(
                id = POST2_ID,
                user = user2,
                gym = gym2,
                timestamp = Date().time - 5 * 24 * 60 * 60 * 1000,
                routesCompleted = listOf(_attempts[2], _attempts[3]),
                photoUrl = "https://placehold.co/400x300/png?text=Post+2",
                comment = "Новые маршруты в ВертикальМире очень интересные!"
            )
        )
    }
    
    data class Comment(
        val id: UUID = UUID.randomUUID(),
        val user: User,
        val route: Route,
        val text: String,
        val timestamp: Long = System.currentTimeMillis(),
        val rating: Int? = null
    )
    
    private fun generateSampleComments(): List<Comment> {
        val user1 = _users.find { it.id == USER1_ID }!!
        val user2 = _users.find { it.id == USER2_ID }!!
        val user3 = _users.find { it.id == USER3_ID }!!
        val route1 = _routes.find { it.id == ROUTE1_ID }!!
        val route2 = _routes.find { it.id == ROUTE2_ID }!!
        val route3 = _routes.find { it.id == ROUTE3_ID }!!
        
        return listOf(
            Comment(
                id = COMMENT1_ID,
                user = user1,
                route = route1,
                text = "Отличная трасса! Интересные зацепы.",
                timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000,
                rating = 5
            ),
            Comment(
                id = COMMENT2_ID,
                user = user2,
                route = route1,
                text = "Сложный старт, но в целом хорошо.",
                timestamp = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000,
                rating = 4
            ),
            Comment(
                id = COMMENT3_ID,
                user = user3,
                route = route2,
                text = "Очень техничная трасса, требует хорошей работы корпуса.",
                timestamp = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000,
                rating = 5
            ),
            Comment(
                id = COMMENT4_ID,
                user = user1,
                route = route3,
                text = "Силовая трасса, но можно пройти техникой.",
                timestamp = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000,
                rating = 4
            )
        )
    }

    fun getCommentsByRouteId(routeId: UUID, comments: List<Comment> = this.comments): List<Comment> =
        comments.filter { it.route.id == routeId }
    
    fun getPostsByUserId(userId: UUID, posts: List<Post> = this.posts): List<Post> = posts.filter { it.user.id == userId }
    fun getPostsByGymId(gymId: UUID, posts: List<Post> = this.posts): List<Post> = posts.filter { it.gym.id == gymId }
    fun getPostsByRouteId(routeId: UUID, posts: List<Post> = this.posts): List<Post> =
        posts.filter { post -> post.routesCompleted.any { it.route.id == routeId } }

    fun getFriendsByUserId(userId: UUID, users: List<User> = this.users): List<User> {
        val user = getUserById(userId, users) ?: return emptyList()
        return user.friends.mapNotNull { friendId -> users.find { it.id == friendId } }
    }

    val userUiExtensions = mapOf(
        USER1_ID to mapOf(
            "username" to "alex_climber",
            "level" to "Продвинутый",
            "experience" to "3 года"
        ),
        USER2_ID to mapOf(
            "username" to "maria_rocks",
            "level" to "Средний",
            "experience" to "2 года"
        ),
        USER3_ID to mapOf(
            "username" to "ivan_climb",
            "level" to "Новичок",
            "experience" to "6 месяцев"
        )
    )
    
    fun getUserUsername(userId: UUID): String {
        return userUiExtensions[userId]?.get("username") ?: "user"
    }
    
    fun getUserLevel(userId: UUID): String {
        return userUiExtensions[userId]?.get("level") ?: "Неизвестно"
    }
    
    fun getUserExperience(userId: UUID): String {
        return userUiExtensions[userId]?.get("experience") ?: "Неизвестно"
    }
    
    fun getUserStatistics(userId: UUID, posts: List<Post>): UserStatistics {
        val userPosts = getPostsByUserId(userId, posts)
        val userCompletedRoutes = userPosts.flatMap { it.routesCompleted }.filter { it.completed }
        val routeGradeMap = mapOf(
            "5A" to 1, "5B" to 2, "5C" to 3, 
            "6A" to 4, "6A+" to 5, "6B" to 6, "6B+" to 7, "6C" to 8, "6C+" to 9,
            "7A" to 10, "7A+" to 11, "7B" to 12, "7B+" to 13, "7C" to 14, "7C+" to 15,
            "8A" to 16, "8A+" to 17, "8B" to 18, "8B+" to 19, "8C" to 20
        )
        
        val completedRouteGrades = userCompletedRoutes.map { it.route.grade }
        val averageGrade = if (completedRouteGrades.isNotEmpty()) {
            val gradeValues = completedRouteGrades.mapNotNull { routeGradeMap[it] }
            if (gradeValues.isNotEmpty()) {
                val avgValue = gradeValues.sum() / gradeValues.size
                routeGradeMap.entries.firstOrNull { it.value == avgValue }?.key ?: "Неизвестно"
            } else "Неизвестно"
        } else "Неизвестно"
        
        val hardestRoute = if (completedRouteGrades.isNotEmpty()) {
            val hardestGrade = completedRouteGrades.maxOfOrNull { routeGradeMap[it] ?: 0 }
            val hardestGradeKey = routeGradeMap.entries.firstOrNull { it.value == hardestGrade }?.key
            hardestGradeKey ?: "Неизвестно"
        } else "Неизвестно"
        
        val gymVisits = userPosts.groupBy { it.gym.name }.mapValues { it.value.size }
        val favoriteGym = gymVisits.maxByOrNull { it.value }?.key ?: "Неизвестно"
        
        return UserStatistics(
            userId = userId,
            totalRoutesClimbed = userCompletedRoutes.size,
            averageGrade = averageGrade,
            hardestRoute = hardestRoute,
            favoriteGym = favoriteGym
        )
    }

    fun getAllPosts(): List<Post> = posts
}