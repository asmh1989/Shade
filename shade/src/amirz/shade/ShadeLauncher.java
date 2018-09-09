package amirz.shade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.launcher3.AppInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherCallbacks;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.uioverrides.WallpaperColorInfo;
import com.google.android.libraries.gsa.launcherclient.LauncherClient;
import com.google.android.libraries.gsa.launcherclient.LauncherClientCallbacks;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ShadeLauncher extends Launcher {
    private final SearchLauncherCallbacks mCallbacks;

    public ShadeLauncher() {
        mCallbacks = new SearchLauncherCallbacks(this);
        setLauncherCallbacks(mCallbacks);
    }

    public SearchLauncherCallbacks getCallbacks() {
        return mCallbacks;
    }

    private static class OverlayCallbackImpl implements LauncherOverlay, LauncherClientCallbacks {
        private final Launcher mLauncher;

        private LauncherClient mClient;
        private LauncherOverlayCallbacks mLauncherOverlayCallbacks;
        private boolean mWasOverlayAttached = false;

        private OverlayCallbackImpl(Launcher launcher) {
            mLauncher = launcher;
        }

        private void setClient(LauncherClient client) {
            mClient = client;
        }

        @Override
        public void onServiceStateChanged(boolean overlayAttached, boolean hotwordActive) {
            if (overlayAttached != mWasOverlayAttached) {
                mWasOverlayAttached = overlayAttached;
                mLauncher.setLauncherOverlay(overlayAttached ? this : null);
            }
        }

        @Override
        public void onOverlayScrollChanged(float progress) {
            if (mLauncherOverlayCallbacks != null) {
                mLauncherOverlayCallbacks.onScrollChanged(progress);
            }
        }

        @Override
        public void onScrollInteractionBegin() {
            mClient.startMove();
        }

        @Override
        public void onScrollInteractionEnd() {
            mClient.endMove();
        }

        @Override
        public void onScrollChange(float progress, boolean rtl) {
            mClient.updateMove(progress);
        }

        @Override
        public void setOverlayCallbacks(LauncherOverlayCallbacks callbacks) {
            mLauncherOverlayCallbacks = callbacks;
        }
    }

    private static class SearchLauncherCallbacks implements LauncherCallbacks {
        private final Launcher mLauncher;

        private OverlayCallbackImpl mOverlayCallbacks;
        private LauncherClient mLauncherClient;
        private boolean mDeferCallbacks;

        private SearchLauncherCallbacks(Launcher launcher) {
            mLauncher = launcher;
        }

        public void deferCallbacksUntilNextResumeOrStop() {
            mDeferCallbacks = true;
        }

        public LauncherClient getLauncherClient() {
            return mLauncherClient;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            SharedPreferences prefs = Utilities.getPrefs(mLauncher);
            mOverlayCallbacks = new OverlayCallbackImpl(mLauncher);
            mLauncherClient = new LauncherClient(mLauncher, mOverlayCallbacks, getClientOptions(prefs));
            mOverlayCallbacks.setClient(mLauncherClient);
        }

        @Override
        public void onDetachedFromWindow() {
            mLauncherClient.onDetachedFromWindow();
        }

        @Override
        public void onAttachedToWindow() {
            mLauncherClient.onAttachedToWindow();
        }

        @Override
        public void onHomeIntent(boolean internalStateHandled) {
            mLauncherClient.hideOverlay(mLauncher.isStarted() && !mLauncher.isForceInvisible());
        }

        @Override
        public void onResume() {
            Handler handler = mLauncher.getDragLayer().getHandler();
            if (mDeferCallbacks) {
                if (handler == null) {
                    // Finish defer if we are not attached to window.
                    checkIfStillDeferred();
                } else {
                    // Wait one frame before checking as we can get multiple resume-pause events
                    // in the same frame.
                    handler.post(this::checkIfStillDeferred);
                }
            } else {
                mLauncherClient.onResume();
            }

        }

        @Override
        public void onPause() {
            if (!mDeferCallbacks) {
                mLauncherClient.onPause();
            }
        }

        @Override
        public void onStart() {
            if (!mDeferCallbacks) {
                mLauncherClient.onStart();
            }
        }

        @Override
        public void onStop() {
            if (mDeferCallbacks) {
                checkIfStillDeferred();
            } else {
                mLauncherClient.onStop();
            }
        }

        private void checkIfStillDeferred() {
            if (!mDeferCallbacks) {
                return;
            }
            if (!mLauncher.hasBeenResumed() && mLauncher.isStarted()) {
                return;
            }
            mDeferCallbacks = false;

            // Move the client to the correct state. Calling the same method twice is no-op.
            if (mLauncher.isStarted()) {
                mLauncherClient.onStart();
            }
            if (mLauncher.hasBeenResumed()) {
                mLauncherClient.onResume();
            } else {
                mLauncherClient.onPause();
            }
            if (!mLauncher.isStarted()) {
                mLauncherClient.onStop();
            }
        }

        @Override
        public void onDestroy() {
            mLauncherClient.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) { }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) { }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { }

        @Override
        public void dump(String prefix, FileDescriptor fd, PrintWriter w, String[] args) {
            mLauncherClient.dump(prefix, w);
        }

        @Override
        public boolean handleBackPressed() {
            return false;
        }

        @Override
        public void onTrimMemory(int level) { }

        @Override
        public void onLauncherProviderChange() { }

        @Override
        public void bindAllApplications(ArrayList<AppInfo> apps) { }

        @Override
        public boolean hasSettings() {
            return false;
        }

        @Override
        public boolean startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData) {
            return false;
        }

        private LauncherClient.ClientOptions getClientOptions(SharedPreferences prefs) {
            return new LauncherClient.ClientOptions(
                    true,
                    true, /* enableHotword */
                    true /* enablePrewarming */
            );
        }
    }

    @Override
    protected int getThemeRes(WallpaperColorInfo wallpaperColorInfo) {
        if (wallpaperColorInfo.isDark()) {
            return wallpaperColorInfo.supportsDarkText() ?
                    R.style.LauncherTheme_Dark_DarkText_Shade : R.style.LauncherTheme_Dark_Shade;
        } else {
            return wallpaperColorInfo.supportsDarkText() ?
                    R.style.LauncherTheme_DarkText_Shade : R.style.LauncherTheme_Shade;
        }
    }
}