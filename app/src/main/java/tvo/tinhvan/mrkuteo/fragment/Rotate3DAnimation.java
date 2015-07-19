package tvo.tinhvan.mrkuteo.fragment;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3DAnimation extends Animation {

    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mDepthZ;
    private final View mView;
    private final boolean mReverse;
    private Camera mCamera;

    public Rotate3DAnimation(float fromDegrees, float toDegrees, float depthZ, boolean reverse, View view) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mDepthZ = depthZ;
        mReverse = reverse;
        mView = view;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mView.getWidth()/2;
        final float centerY = mView.getHeight()/2;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateX(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
