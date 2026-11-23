package org.openedx.firebase

import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseFormattingTest {

    @Test
    fun replacesInvalidCharactersWithUnderscores() {
        assertEquals("Video_Played", formatFirebaseName("Video:Played"))
        assertEquals("Course_Home_Video_Click", formatFirebaseName("Course Home.Video Click"))
        assertEquals("edx_bi_app_launch", formatFirebaseName("edx.bi.app.launch"))
    }

    @Test
    fun collapsesRepeatedUnderscores() {
        assertEquals("Course_Home_Video", formatFirebaseName("Course Home: Video"))
        assertEquals("Video_Played", formatFirebaseName("Video__Played"))
    }

    @Test
    fun keepsValidNamesUnchanged() {
        assertEquals("Sequential_Clicked", formatFirebaseName("Sequential_Clicked"))
        assertEquals("course_id", formatFirebaseName("course_id"))
    }

    @Test
    fun cutsNamesToMaximumLength() {
        assertEquals(
            "Content_Page_Show_Completed_Subsection_C",
            formatFirebaseName("Content Page:Show Completed Subsection Click"),
        )
    }

    @Test
    fun cutsNamesBeforeCollapsingUnderscores() {
        val name = "ab" + ":".repeat(10) + "c".repeat(MAX_NAME_LENGTH)

        assertEquals("ab_" + "c".repeat(28), formatFirebaseName(name))
    }

    @Test
    fun keepsUnderscoreWhenCutEndsOnOne() {
        val name = "a".repeat(MAX_NAME_LENGTH - 1) + ":b"

        assertEquals("a".repeat(MAX_NAME_LENGTH - 1) + "_", formatFirebaseName(name))
    }

    @Test
    fun trimsSurroundingWhitespace() {
        assertEquals("Video_Played", formatFirebaseName(" \tVideo:Played\n "))
        assertEquals("Video_Played", formatFirebaseName("Video:Played\u00A0"))
    }

    @Test
    fun removesLeadingCharactersThatAreNotLetters() {
        assertEquals("Video", formatFirebaseName("_Video"))
        assertEquals("Video", formatFirebaseName(":Video"))
        assertEquals("Video_Played", formatFirebaseName("1Video:Played"))
        assertEquals("abc", formatFirebaseName("12_3abc"))
    }

    @Test
    fun returnsOriginalNameWhenNothingValidRemains() {
        assertEquals("", formatFirebaseName(""))
        assertEquals("   ", formatFirebaseName("   "))
        assertEquals("%%%", formatFirebaseName("%%%"))
        assertEquals("123", formatFirebaseName("123"))
    }

    @Test
    fun formatsParameterKeys() {
        val parameters = mapOf("course id" to "course-v1:edX+DemoX+Demo", "speed" to "1.5")

        assertEquals(
            mapOf("course_id" to "course-v1:edX+DemoX+Demo", "speed" to "1.5"),
            parameters.toFirebaseParameters(),
        )
    }

    @Test
    fun cutsParameterValuesToMaximumLength() {
        val parameters = mapOf("block_id" to "a".repeat(MAX_PARAMETER_VALUE_LENGTH + 1))

        assertEquals(
            mapOf("block_id" to "a".repeat(MAX_PARAMETER_VALUE_LENGTH)),
            parameters.toFirebaseParameters(),
        )
    }

    @Test
    fun convertsParameterValuesToStringsAndDropsNullValues() {
        val parameters = mapOf("speed" to 1.5, "position" to 42L, "name" to null)

        assertEquals(mapOf("speed" to "1.5", "position" to "42"), parameters.toFirebaseParameters())
    }
}
