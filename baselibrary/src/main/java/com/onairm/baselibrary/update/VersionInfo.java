package com.onairm.baselibrary.update;

import com.onairm.baselibrary.utils.ImageUtils;
import com.onairm.baselibrary.utils.Utils;

/**
 * Created by android on 2017/3/1.
 */

public class VersionInfo {
    private String version;
    private String description;
    private int mustUpdate;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(int mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public String getUrl() {
        return ImageUtils.getUrl(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
