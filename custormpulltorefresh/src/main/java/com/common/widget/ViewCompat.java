/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.common.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

public class ViewCompat {

	public static void postOnAnimation(View view, Runnable runnable) {
		SDK16.postOnAnimation(view, runnable);
	}

	public static void setBackground(View view, Drawable background) {
		SDK16.setBackground(view, background);
	}

	static class SDK16 {

		public static void postOnAnimation(View view, Runnable runnable) {
			view.postOnAnimation(runnable);
		}

		public static void setBackground(View view, Drawable background) {
			view.setBackground(background);
		}
	}
}