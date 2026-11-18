package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AuthResult
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.PrintProductId
import com.example.data.model.PrintSideOption
import com.example.data.model.UserProfile
import com.example.ui.CatalogSelectionState
import com.example.util.ExternalIntents
import com.example.util.MalwaniPrintConstants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Malwani Print", appName)
    }

    @Test
    fun `upi intent targets farhanupi at hdfcbank with full amount`() {
        val intent = ExternalIntents.createUpiPayIntent(
            amountInr = 1400,
            transactionNote = "Pamphlet Order",
            targetPackage = MalwaniPrintConstants.GPAY_PACKAGE
        )
        val uri = intent.data!!
        assertEquals("upi", uri.scheme)
        assertEquals("pay", uri.host)
        assertEquals("farhanupi@hdfcbank", uri.getQueryParameter("pa"))
        assertEquals("1400.00", uri.getQueryParameter("am"))
        assertEquals("INR", uri.getQueryParameter("cu"))
        assertEquals(MalwaniPrintConstants.GPAY_PACKAGE, intent.`package`)
    }

    @Test
    fun `whatsapp order summary matches required format`() {
        val profile = UserProfile(
            authPhone = "9876543210",
            name = "Ayaan Khan",
            contactNumber = "+91 9876543210",
            deliveryAddress = "Plot 12, Malwani Gate 6, Mumbai"
        )
        val order = CatalogSelectionState(
            activeProductId = PrintProductId.PAMPHLET,
            pamphletSizeId = "pamphlet_a5_1000",
            pamphletSide = PrintSideOption.FRONT_BACK
        ).toSelectedOrder()

        val message = ExternalIntents.buildOrderSummaryMessage(profile, order)
        assertTrue(message.contains("New Order on Malwani Print!"))
        assertTrue(message.contains("Customer: Ayaan Khan"))
        assertTrue(message.contains("Phone: +91 9876543210"))
        assertTrue(message.contains("Address: Plot 12, Malwani Gate 6, Mumbai"))
        assertTrue(message.contains("Product: Pamphlet - A5 Size 1000 Qty (Front/Back)"))
        assertTrue(message.contains("Amount Paid: ₹1,400 (Paid via UPI)"))
    }

    @Test
    fun `repository signs up signs in and persists user profile`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserPreferencesRepository(context)

        val signup = repo.signUpWithPhoneAndPin("9876543210", "2468")
        assertTrue(signup is AuthResult.Success)

        repo.saveProfile("Zoya Patel", "+91 9876543210", "Malwani Colony, Mumbai")
        val session = repo.sessionFlow.first()
        assertTrue(session.isLoggedIn)
        assertEquals("Zoya Patel", session.profile.name)
        assertTrue(session.profile.isComplete)
    }
}
