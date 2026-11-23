package org.openedx.firebase

// Firebase limits for event names, parameter names, and string parameter values:
// https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics#logEvent(java.lang.String,android.os.Bundle)
internal const val MAX_NAME_LENGTH = 40
internal const val MAX_PARAMETER_VALUE_LENGTH = 100

private val invalidNameCharacters = Regex("[^a-zA-Z0-9_]")
private val repeatedUnderscores = Regex("_{2,}")

/**
 * Formats [name] as a Firebase event or parameter name: only letters, digits, and underscores,
 * starting with a letter, and at most [MAX_NAME_LENGTH] characters.
 *
 * The steps follow the iOS adapter (openedx-app-firebase-analytics-ios) so that both platforms
 * report the same names. Returns [name] unchanged if nothing valid remains, so that Firebase
 * still reports it: Firebase logs an `error` event in place of an event with an invalid name,
 * and a `firebase_error` parameter in place of an invalid parameter name.
 */
internal fun formatFirebaseName(name: String): String {
    val formatted = name
        .trim()
        .replace(invalidNameCharacters, "_")
        .take(MAX_NAME_LENGTH)
        .replace(repeatedUnderscores, "_")
        // Firebase requires names to start with a letter. The iOS adapter does not enforce this.
        .dropWhile { !it.isLetter() }
    return formatted.ifEmpty { name }
}

/**
 * Returns these parameters with Firebase-formatted keys and with each non-null value
 * converted to a string of at most [MAX_PARAMETER_VALUE_LENGTH] characters.
 */
internal fun Map<String, Any?>.toFirebaseParameters(): Map<String, String> =
    entries
        .mapNotNull { (key, value) ->
            value?.let { formatFirebaseName(key) to it.toString().take(MAX_PARAMETER_VALUE_LENGTH) }
        }
        .toMap()
