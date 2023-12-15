package joseph.profile.domain.usecases

import com.joseph.common.IdResourceString
import com.joseph.profile.domain.models.UserFieldValidatorCorrectState
import com.joseph.profile.domain.models.UserFieldValidatorDefaultState
import com.joseph.profile.domain.models.UserFieldValidatorErrorState
import com.joseph.profile.domain.models.UserFieldValidatorState
import com.joseph.profile.domain.usecases.EmailValidatorUseCaseImpl
import com.joseph.profile.domain.usecases.EmailValidatorUseCaseImpl.Companion.EMAIL_COM_SUFFIX
import com.joseph.profile.domain.usecases.EmailValidatorUseCaseImpl.Companion.EMAIL_RU_SUFFIX
import com.joseph.profile.domain.usecases.EmailValidatorUseCaseImpl.Companion.EMAIL_SYMBOL
import com.joseph.ui.core.R
import org.junit.Assert.*
import org.junit.Test


class EmailValidatorUseCaseImplTest {

    private fun createEmailValidatorErrorState(errorText: IdResourceString) =
        UserFieldValidatorErrorState(errorText)

    private val emptyLengthErrorText =
        IdResourceString(R.string.the_length_of_the_email_address_must_exceed_n_characters)

    private val emailNoneContainsSymbolErrorText =
        IdResourceString(R.string.the_email_address_must_contain)

    private val emailNoneContainsEmailSuffixErrorText =
        IdResourceString(R.string.invalid_format_for_entering_an_email_address)

    private val emailEmptyText = IdResourceString(R.string.whats_your_email)

    private val emailValidatorEmptyState = UserFieldValidatorDefaultState(emailEmptyText)

    private val emailLengthErrorState = createEmailValidatorErrorState(emptyLengthErrorText)

    private val emailContainsSymbolErrorState =
        createEmailValidatorErrorState(emailNoneContainsSymbolErrorText)

    private val emailContainsEmailSuffixErrorState =
        createEmailValidatorErrorState(emailNoneContainsEmailSuffixErrorText)

    private fun emptyEmail() = String()

    private fun emailMinLength() = "12345"

    private fun emailNoneContainSymbol() = "12345678"

    private fun emailNoneContainEmailSuffix() = "12345678@"

    private fun emailCorrect() = "12345678@gmai.com"

    /**
     * Should return default state when the email is empty
     */
    @Test
    fun checkEmailIfEmpty() = startEmailValidatorUseCaseTest(
        email = emptyEmail(),
        result = emailValidatorEmptyState
    )

    /**
     * Should return an error state when the length of the email is small
     */
    @Test
    fun checkEmailSmallLength() = startEmailValidatorUseCaseTest(
        email = emailMinLength(),
        result = emailLengthErrorState
    )

    /**
     * Should return error state when the email is not contains [EMAIL_SYMBOL]
     */
    @Test
    fun checkEmailContainSymbol() = startEmailValidatorUseCaseTest(
        email = emailNoneContainSymbol(),
        result = emailContainsSymbolErrorState
    )

    /**
     * Should return error state when the email is not contains [EMAIL_COM_SUFFIX] and [EMAIL_RU_SUFFIX]"
     */
    @Test
    fun checkEmailContainEmailSuffix() = startEmailValidatorUseCaseTest(
        email = emailNoneContainEmailSuffix(),
        result = emailContainsEmailSuffixErrorState
    )

    /**
     * Should return correct state when the email correct
     */
    @Test
    fun checkEmailCorrectState() = startEmailValidatorUseCaseTest(
        email = emailCorrect(),
        result = UserFieldValidatorCorrectState
    )

    private fun startEmailValidatorUseCaseTest(
        email: String,
        result: UserFieldValidatorState
    ) {
        val actual = when {
            email.isEmpty() -> emailValidatorEmptyState
            email.length <= EmailValidatorUseCaseImpl.EMAIL_MIN_LENGTH -> emailLengthErrorState
            !email.contains(EMAIL_SYMBOL) -> emailContainsSymbolErrorState
            !email.endsWith(EMAIL_COM_SUFFIX) && !email.endsWith(EMAIL_RU_SUFFIX) -> emailContainsEmailSuffixErrorState
            else -> UserFieldValidatorCorrectState
        }
        assertEquals(result, actual)
    }
}