package com.luffy.testautomation.utils.helpers;

import java.io.IOException;

public interface Shell {
    String execute(String command) throws IOException;
}
