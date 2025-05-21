package com.contr4s.whiterock

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appStartsWithBottomNavigationBar() {
        composeTestRule.onAllNodesWithText("Лента", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Скалодромы", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Трассы", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Друзья", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Профиль", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().assertIsDisplayed()
    }

    @Test
    fun navigateToGymsScreen() {
        composeTestRule.onAllNodesWithText("Скалодромы", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().performClick()
        
        composeTestRule.onNode(
            hasText("Скалодромы") and hasParent(hasTestTag("topAppBar")),
            useUnmergedTree = true
        ).assertIsDisplayed()
    }

    @Test
    fun navigateToRoutesScreen() {
        composeTestRule.onAllNodesWithText("Трассы", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().performClick()
        
        composeTestRule.onNode(
            hasText("Трассы") and hasParent(hasTestTag("topAppBar")),
            useUnmergedTree = true
        ).assertIsDisplayed()
    }

    @Test
    fun navigateToProfileScreen() {
        composeTestRule.onAllNodesWithText("Профиль", useUnmergedTree = true)
            .filter(hasAnyAncestor(hasTestTag("BottomNavigationBar")))
            .onFirst().performClick()
        
        composeTestRule.onNode(
            hasTestTag("topAppBar"),
            useUnmergedTree = true
        ).assertIsDisplayed()
    }
}
