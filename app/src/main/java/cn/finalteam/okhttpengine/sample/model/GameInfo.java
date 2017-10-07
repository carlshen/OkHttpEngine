package cn.finalteam.okhttpengine.sample.model;

/**
 * Desction:
 * Author:carlshen
 * Date:17/10/10 下午3:07
 */
public class GameInfo {
    private String gameName;
    private String packageName;
    private String url;
    private String coverUrl;

    public GameInfo(String gameName, String packageName, String url, String coverUrl) {
        this.gameName = gameName;
        this.packageName = packageName;
        this.url = url;
        this.coverUrl = coverUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
