package com.luffy.testautomation.appautomation.config;

import com.google.inject.Module;

public enum PlatformModule {
    IOS {
        @Override
        public Module getModule() {
            return new MyIosModule();
        }
    },

    ANDROID {
        @Override
        public Module getModule() {
            return new com.luffy.testautomation.appautomation.config.MyAndroidModule();
        }
    },

    IOSGRID {
        @Override
        public Module getModule() {
            return new MyIosGridModule();
        }
    },

    ANDROIDGRID {
        @Override
        public Module getModule() {
            return new MyAndroidGridModule();
        }
    };

    public abstract Module getModule();
}
