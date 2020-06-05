package com.kraftwerk28.pocketcms

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.kraftwerk28.pocketcms.fragments.DBConnectFragment
import org.junit.Test

class ConnectFragmentUITest {
    @Test
    fun testConnectFragment() {
        with(launchFragment<DBConnectFragment>(null)) {
            onFragment {
                assert(
                    it.binding.connectionTitle.toString()
                        .startsWith("postgres://")
                )
            }
        }
    }
}