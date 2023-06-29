/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.testharness.TestHarness
import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.rules.GrantPostNotificationsPermissionRule
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.uitesthiltmanifest.HiltComponentActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@HiltAndroidTest
class NavigationUiTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Grant [android.Manifest.permission.POST_NOTIFICATIONS] permission.
     */
    @get:Rule(order = 2)
    val postNotificationsPermission = GrantPostNotificationsPermissionRule()

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    val userNewsResourceRepository = CompositeUserNewsResourceRepository(
        newsRepository = TestNewsRepository(),
        userDataRepository = TestUserDataRepository(),
    )

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun compactWidth_compactHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 400.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun mediumWidth_compactHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 400.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun expandedWidth_compactHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 400.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun extraLargeWidth_compactHeight_showsPermanentDrawer() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(1300.dp, 400.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertIsDisplayed()
    }

    @Test
    fun compactWidth_mediumHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 500.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun mediumWidth_mediumHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 500.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun expandedWidth_mediumHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 500.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun extraLargeWidth_mediumHeight_showsPermanentDrawer() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(1300.dp, 500.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertIsDisplayed()
    }

    @Test
    fun compactWidth_expandedHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 900.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun mediumWidth_expandedHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 900.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun expandedWidth_expandedHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 900.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertDoesNotExist()
    }

    @Test
    fun extraLargeWidth_expandedHeight_showsPermanentDrawer() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(1300.dp, 900.dp)) {
                BoxWithConstraints {
                    NiaApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                        networkMonitor = networkMonitor,
                        userNewsResourceRepository = userNewsResourceRepository,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationRail").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:navigationBar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("adaptiveScaffold:permanentDrawer").assertIsDisplayed()
    }
}
