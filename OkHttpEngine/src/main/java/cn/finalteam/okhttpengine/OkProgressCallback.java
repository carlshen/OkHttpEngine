package cn.finalteam.okhttpengine;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/4/19 上午11:31
 */
interface OkProgressCallback {
    void updateProgress(int progress, long networkSpeed, boolean done);
}
