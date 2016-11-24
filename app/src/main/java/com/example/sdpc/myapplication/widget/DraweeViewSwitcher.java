package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.sdpc.myapplication.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by sdpc on 16-11-11.
 */

public class DraweeViewSwitcher extends BaseViewSwitcher {
    private static final String TAG = "DraweeViewSwitcher";
    private Postprocessor alphaEdgePostprocessor = new BasePostprocessor() {
        @Override
        public String getName() {
            return "AlphaProcess0r";
        }

        @Override
        public void process(Bitmap sourceImg) {

            int width = sourceImg.getWidth();
            int height = sourceImg.getHeight();

            int[] argb = new int[width * height];

            sourceImg.getPixels(argb, 0, width, 0, 0, width, height);// 获得图片的ARGB值
            //定义一个完全现实的半径，超出半径的 ，越远，透明度越高
            //完全透明的点-----四个顶点，及距离为 Math.sqrt(width*width + height*height) /2
            //TODO 应该为百分比
            double r = height / 2 * 0.5;//完全显示的半径
            int max = (int) (Math.sqrt(width * width + height * height) / 2);
            //max ~ R线性渐变

            for (int i = 0; i < argb.length; i++) {
                // 计算透明图和透明范围
                int alpha = 255;
                //该点到中心距离。
                double l = Math.sqrt(Math.pow((i % width - width / 2), 2) + Math.pow((i / width - height / 2), 2));
                //透明度
                if (l >= r) {
                    //渐进式变换
                    alpha = (int) (255 * Math.pow((1.0 - (l - r) / (max - r) * 1.0), 2));
                }
                argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);

            }
            sourceImg.setPixels(argb, 0, width, 0, 0, width, height);


//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.desktop_manager_mask);
//                try {
//                    Canvas cv = new Canvas(sourceImg);
//                    cv.drawBitmap(bitmap, 0, 0, null);
//                    cv.save(Canvas.ALL_SAVE_FLAG);
//                    cv.restore();
//                } catch (Exception e) {
//                    bitmap = null;
//                    e.getStackTrace();
//                } finally {
//                    bitmap.recycle();
//                }

        }

    };

    public DraweeViewSwitcher(Context context) {
        super(context);
    }

    public DraweeViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        alphaEdgePostprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "AlphaProcess0r";
            }

            @Override
            public void process(Bitmap sourceImg) {

//                int width = sourceImg.getWidth();
//                int height = sourceImg.getHeight();
//
//                int[] argb = new int[width * height];
//
//                sourceImg.getPixels(argb, 0, width, 0, 0, width, height);// 获得图片的ARGB值
//                //定义一个完全现实的半径，超出半径的 ，越远，透明度越高
//                //完全透明的点-----四个顶点，及距离为 Math.sqrt(width*width + height*height) /2
//                //TODO 应该为百分比
//                double r = height / 2 * 0.5;//完全显示的半径
//                int max = (int) (Math.sqrt(width * width + height * height) / 2);
//                //max ~ R线性渐变
//
//                for (int i = 0; i < argb.length; i++) {
//                    // 计算透明图和透明范围
//                    int alpha = 255;
//                    //该点到中心距离。
//                    double l = Math.sqrt(Math.pow((i % width - width / 2), 2) + Math.pow((i / width - height / 2), 2));
//                    //透明度
//                    if (l >= r) {
//                        //渐进式变换
//                        alpha = (int) (255 * Math.pow((1.0 - (l - r) / (max - r) * 1.0), 2));
//                    }
//                    argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);
//
//                }
//                sourceImg.setPixels(argb, 0, width, 0, 0, width, height);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.desktop_manager_mask);
                try {
                    Canvas cv = new Canvas(sourceImg);
                    cv.drawBitmap(bitmap, 0, 0, null);
                    cv.save(Canvas.ALL_SAVE_FLAG);
                    cv.restore();
                } catch (Exception e) {
                    bitmap = null;
                    e.getStackTrace();
                } finally {
                    bitmap.recycle();
                }

            }

        };
    }

    public void setImageURI(Uri uri) {
        SimpleDraweeView image = (SimpleDraweeView) this.getNextView();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(alphaEdgePostprocessor)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setControllerListener(new ControllerListener<ImageInfo>() {
                            @Override
                            public void onSubmit(String id, Object callerContext) {
                                Log.d(TAG, "onSubmit");
                            }

                            @Override
                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                                Log.d(TAG, "onFinalImageSet");
                                showNext();
                            }

                            @Override
                            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                                Log.d(TAG, "onIntermediateImageSet");
                            }

                            @Override
                            public void onIntermediateImageFailed(String id, Throwable throwable) {
                                Log.d(TAG, "onIntermediateImageFailed");
                            }

                            @Override
                            public void onFailure(String id, Throwable throwable) {
                                Log.d(TAG, "onFailure");
                                showNext();
                            }

                            @Override
                            public void onRelease(String id) {
                                Log.d(TAG, "onRelease");
                            }
                        })
//                        .setOldController(image.getController())
                        .build();
        image.setController(controller);

        //TODO find the right time to showNext
//        if (mFirstTime) {
//        showNext();
//        }
    }
}
