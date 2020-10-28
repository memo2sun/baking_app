/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sunyounglee.bakingapp.ui;

import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import net.sunyounglee.bakingapp.IdlingResource.SimpleIdlingResource;
import net.sunyounglee.bakingapp.models.Recipe;

import java.util.List;

/**
 * Takes a String and returns it after a while via a callback.
 * <p>
 * This executes a long-running operation on a different thread that results in problems with
 * Espresso if an {@link IdlingResource} is not implemented and registered.
 */
class MessageDelayer {
    private static final int DELAY_MILLIS = 3000;

    interface DelayerCallback {
        void onDone(List<Recipe> recipeList);
    }

    /**
     * Takes a String and returns it after {@link #DELAY_MILLIS} via a {@link DelayerCallback}.
     *
     * @param recipeList the list that will be returned via the callback
     * @param callback   used to notify the caller asynchronously
     */
    static void processMessage(final List<Recipe> recipeList, final DelayerCallback callback,
                               @Nullable final SimpleIdlingResource idlingResource) {
        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        // Delay the execution, return message via callback.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone(recipeList);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }
}
